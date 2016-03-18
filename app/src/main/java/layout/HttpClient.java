package layout;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by daniel on 3/15/16.
 */

interface HttpClientCallback {
    void receivedDataForCurrencyPairAtExchange(String data, CurrencyPair currencyPair, Exchange exchange);
}

enum Exchange {
    POLONIEX
}

enum CurrencyPair {
    BTC_ETH,
    USDT_ETH
}

public class HttpClient {

    private static HttpClient mInstance;
    RequestQueue mRequestQueue;
    private static Context mContext;

    private HttpClient(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized HttpClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new HttpClient(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void getCurrencyPriceAtExchange(final CurrencyPair currencyPair, final Exchange exchange, final HttpClientCallback callback) {

        String url = urlForExchange(exchange);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String lastValue = null;

                try {
                    switch (exchange) {
                        case POLONIEX:
                            String token = stringTokenForCurrencyAtExchange(currencyPair, exchange);
                            JSONObject btcEth = response.getJSONObject(token);
                            lastValue = btcEth.getString("last");
                            break;
                    }

                    callback.receivedDataForCurrencyPairAtExchange(lastValue, currencyPair, exchange);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        this.addToRequestQueue(jsonObjectRequest);
    }

    private String urlForExchange(Exchange exchange) {

        String url = "";

        switch (exchange) {
            case POLONIEX: url = "https://poloniex.com/public?command=returnTicker";
                break;
        }

        return url;
    }

    private String stringTokenForCurrencyAtExchange(CurrencyPair currencyPair, Exchange exchange) {

        String token = "";

        switch (exchange) {
            case POLONIEX:
                switch (currencyPair) {
                    case BTC_ETH: token = "BTC_ETH"; break;
                    case USDT_ETH: token = "USDT_ETH"; break;
                }
                break;
        }

        return token;
    }
}
