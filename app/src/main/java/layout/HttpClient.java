package layout;

import android.content.Context;

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

    public void getPoloniexEthPrice() {
        String url = "https://poloniex.com/public?command=returnTicker";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject btcEth = response.getJSONObject("BTC_ETH");
                    String lastValue = btcEth.getString("last");
                    System.out.println(lastValue);
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
}
