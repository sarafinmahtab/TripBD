package com.sarafinmahtab.tripbd;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Arafin on 7/25/2017.
 */

public class MySingleton {

//    Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
//    Network network = new BasicNetwork(new HurlStack());
//    requestQueue = new RequestQueue(cache, network);
//    requestQueue.start();

    private static MySingleton myInstance;
    private static Context myContext;

    private RequestQueue requestQueue;

    private MySingleton(Context myCtx) {
        myContext = myCtx;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(myContext.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getMyInstance(Context context) {
        if(myInstance == null) {
            myInstance = new MySingleton(context);
        }
        return myInstance;
    }

    public<T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }
}
