package com.igmi.rampage;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class VolleySingleton extends Application {
    private static VolleySingleton ourInstance;
    public static final String TAG = VolleySingleton.class
            .getSimpleName();
    private RequestQueue requestQueue;

    public static synchronized VolleySingleton getInstance() {
        return ourInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
    }
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext().getApplicationContext());

        }
        return requestQueue;
    }


    public <T> void addToRequestque(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }
    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;
        }

        return false;
    }
}
