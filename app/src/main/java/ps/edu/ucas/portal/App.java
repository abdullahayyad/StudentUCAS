package ps.edu.ucas.portal;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import ps.edu.ucas.portal.utils.AppSharedPreferences;


/**
 * Created by ayyad on 1/11/17.
 */

public class App extends Application{

    public static final String TAG = App.class.getSimpleName();

    private static App app;

    private AppSharedPreferences sharedPreferences;

    private RequestQueue requestQueue;
    private ImageLoader mImageLoader;

    private double lon;
    private double lat;



    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        sharedPreferences = new AppSharedPreferences(getApplicationContext());

    }

    public static synchronized App getInstance(){
        return app;
    }

    public synchronized AppSharedPreferences getSharedPreferences(){
        return new AppSharedPreferences(getApplicationContext());
    }


    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }





}
