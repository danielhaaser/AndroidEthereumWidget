package layout;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.danielhaaser.ethereumwidget.R;

/**
 * The configuration screen for the {@link EthereumPriceWidget EthereumPriceWidget} AppWidget.
 */
public class EthereumPriceWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "layout.EthereumPriceWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
//    EditText mAppWidgetText;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = EthereumPriceWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
//            String widgetText = mAppWidgetText.getText().toString();
//            saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            EthereumPriceWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId, CurrencyPair.BTC_ETH, Exchange.POLONIEX, null);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public EthereumPriceWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.ethereum_price_widget_configure);

        // Populate spinners with options
        Spinner exchangesSpinner = (Spinner) findViewById(R.id.exchanges);
        Spinner currencyPairsSpinner = (Spinner) findViewById(R.id.currency_pairs);
        Spinner timeIntervalSpinner = (Spinner) findViewById(R.id.time_intervals);

        ArrayAdapter exchangesAdapter = ArrayAdapter.createFromResource(this, R.array.exchanges, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter currencyPairsAdapter = ArrayAdapter.createFromResource(this, R.array.currency_pairs, android.R.layout.simple_spinner_dropdown_item);

        exchangesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyPairsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        exchangesSpinner.setAdapter(exchangesAdapter);
        currencyPairsSpinner.setAdapter(exchangesAdapter);


        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

//        mAppWidgetText.setText(loadTitlePref(EthereumPriceWidgetConfigureActivity.this, mAppWidgetId));
    }
}

