package layout;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.support.v4.widget.ExploreByTouchHelper;
import android.widget.RemoteViews;

import com.danielhaaser.ethereumwidget.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link EthereumPriceWidgetConfigureActivity EthereumPriceWidgetConfigureActivity}
 */
public class EthereumPriceWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, CurrencyPair currencyPair, Exchange exchange, String data) {

        CharSequence widgetText = EthereumPriceWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ethereum_price_widget);
        if (data != null) {
            views.setTextViewText(R.id.appwidget_text, data);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {

        CurrencyPair currencyPair = CurrencyPair.BTC_ETH;
        Exchange exchange = Exchange.POLONIEX;

        HttpClient.getInstance(context).getCurrencyPriceAtExchange(currencyPair, exchange, new HttpClientCallback() {
            @Override
            public void receivedDataForCurrencyPairAtExchange(String data, CurrencyPair currencyPair, Exchange exchange) {

                // There may be multiple widgets active, so update all of them
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId, currencyPair, exchange, data);
                }
            }
        });
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            EthereumPriceWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

