package ps.edu.ucas.portal.service;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ps.edu.ucas.portal.App;
import ps.edu.ucas.portal.model.Authentication;
import ps.edu.ucas.portal.model.StatusResult;
import ps.edu.ucas.portal.utils.UtilityUCAS;

import static com.android.volley.Request.Method.POST;
import static ps.edu.ucas.portal.service.WebService.RequestAPI.AUTHENTICATION_UPDATE;


public class WebService {
    private static final String TAG = "webService";

    public static String RESULT = "result";
    public static String RESPONSE_CODE = "status_code";
    public static String IS_CASH = "is_cash";

    private HashMap<String, Object> objectHashMap;
    private OnResponding onResponding;


    private RequestAPI requestAPI;
    private HashMap<String, String> requestParams;

    public static final String UCAS_MOBILE_URL = "https://mobile.ucas.edu.ps/";
    public static final String UCAS_RSS = "https://www.ucas.edu.ps/ucas_RSS/";
    private final static String DOMAIN_OAUTH2_URL = "https://oauth2.ucas.edu.ps/";
    private static final String CLIENT_ID = "1050b2e9-18c2-4de9-a8a1-08f70f0b0715";
    public static final String CLIENT_SECRET = "6BcmXspFcVZlcc2HC+Q6qz+Jbzhuy6qgXfqnFbFRdz4=";

    public enum RequestAPI {
        AUTHENTICATION(DOMAIN_OAUTH2_URL + "token", POST),
        AUTHENTICATION_UPDATE(DOMAIN_OAUTH2_URL + "token", POST),
        PROFILE(UCAS_MOBILE_URL + "student/student_info", POST),
        EVENT_COURSE_LAST(UCAS_MOBILE_URL + "student/table/last", POST),
        ACADEMIC_CALENDER(UCAS_MOBILE_URL + "student/academic_calender", POST),
        TRANSCRIPT(UCAS_MOBILE_URL + "student/transcript", POST),
        FINANCE_FILE(UCAS_MOBILE_URL + "student/financial_file", POST),

        LATEST_RSS(UCAS_RSS + "Latest_RSS.aspx", Request.Method.GET),
        NEWS_RSS(UCAS_RSS + "News_RSS.aspx", Request.Method.GET),
        ADVER_RSS(UCAS_RSS + "Adver_RSS.aspx", Request.Method.GET);
        private String value;
        private int requestMethod;

        RequestAPI(final String value, final int requestMethod) {
            this.value = value;
            this.requestMethod = requestMethod;
        }

        public String getValue() {
            return value;
        }

        public int getRequestMethod() {
            return requestMethod;
        }

        @Override
        public String toString() {
            return this.getValue();
        }
    }


    public void startRequest(RequestAPI requestAPI, HashMap<String, String> dataRequest, OnResponding onResponding) {
        this.onResponding = onResponding;
        this.requestAPI = requestAPI;
        this.requestParams = dataRequest;

        if (dataRequest == null)
            dataRequest = new HashMap<>();

        if (RequestAPI.NEWS_RSS != requestAPI && RequestAPI.LATEST_RSS != requestAPI && RequestAPI.ADVER_RSS != requestAPI)
            dataRequest.put("client_id", CLIENT_ID);

        getConnection(requestAPI, dataRequest);

    }


    public void startOldRequest(RequestAPI requestAPI, HashMap<String, String> dataRequest, OnResponding onResponding) {
        this.onResponding = onResponding;
        this.requestAPI = requestAPI;
        this.requestParams = dataRequest;

        if (dataRequest == null)
            dataRequest = new HashMap<>();

        if (RequestAPI.NEWS_RSS != requestAPI && RequestAPI.LATEST_RSS != requestAPI && RequestAPI.ADVER_RSS != requestAPI)
            dataRequest.put("client_id", CLIENT_ID);

        Log.d(TAG + ":URL", requestAPI.getValue());
        Log.d(TAG + ":DataRequest", dataRequest.toString());


        getURLConnection(requestAPI, dataRequest);

    }


    private void getConnection(final RequestAPI url, final HashMap<String, String> dataRequest) {

        Log.d(TAG + ":URL", url.getValue());
        Log.d(TAG + ":DataRequest", dataRequest.toString());


        objectHashMap = initializeHashMap();
        StringRequest strReq = new StringRequest(url.getRequestMethod(),
                url.getValue(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG + ":Result", response.toString());
                objectHashMap.put(RESULT, response);
                try {
                    sendRequest(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                VolleyLog.d(TAG, ":Error: " + error.getMessage());
                VolleyLog.d(TAG, ":Error Message: " + message);
                try {
                    objectHashMap.put(RESPONSE_CODE, error.networkResponse.statusCode);
                } catch (Exception e) {

                }


                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        Log.e(TAG + ":Result Error", res.toString());
                        objectHashMap.put(RESULT, response);

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }
                try {
                    if (response != null) {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        Log.d(TAG + ":Result Error", res);
                        objectHashMap.put(RESULT, res);

                    }
                    sendRequest(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (dataRequest != null)
                    return dataRequest;
                else
                    return super.getParams();
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                if (RequestAPI.NEWS_RSS != url && RequestAPI.LATEST_RSS != url && RequestAPI.ADVER_RSS != url && RequestAPI.AUTHENTICATION != url && AUTHENTICATION_UPDATE != url) {
                    headers.put("Accept", "application/json; charset=UTF-8");
                    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    headers.put("Authorization", "Bearer " + UtilityUCAS.getAuthentication().getAccessToken());
                    Log.d(TAG + "Authorization", "Bearer " + UtilityUCAS.getAuthentication().getAccessToken() + "");
                }
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                objectHashMap.put(RESPONSE_CODE, response.statusCode);
                Log.d(TAG + ":response", response.toString());
                Log.d(TAG + ":response", response.statusCode + "");

                return super.parseNetworkResponse(response);

            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };
//        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

//        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        int x=2;// retry count
//        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
//                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
 //       strReq.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0, 1.0f));

        App.getInstance().addToRequestQueue(strReq);
    }


    private void sendRequest(RequestAPI requestAPI) throws JSONException {
        Log.e("sendRequest","sendRequest");
        switch ((int) objectHashMap.get(RESPONSE_CODE)) {
            case 0:
                if (RequestAPI.AUTHENTICATION != requestAPI && AUTHENTICATION_UPDATE != requestAPI) {
                    if (!UtilityUCAS.getCashData(requestAPI.name()).equals("")) {
                        objectHashMap.put(RESULT, UtilityUCAS.getCashData(requestAPI.name()));
                        objectHashMap.put(IS_CASH, true);
                        onResponding.onResponding(requestAPI, StatusConnection.SUCCESS, objectHashMap);
                    } else
                        onResponding.onResponding(requestAPI, StatusConnection.NO_CONNECTION, objectHashMap);
                }else
                    onResponding.onResponding(requestAPI, StatusConnection.NO_CONNECTION, objectHashMap);

                break;
            case 200:
            case 201:
                if (RequestAPI.AUTHENTICATION_UPDATE == requestAPI) {
                    try {
                        JSONObject ObJsonObject = new JSONObject(objectHashMap.get(RESULT).toString());
                        Authentication authentication = new Gson().fromJson(String.valueOf(ObJsonObject), Authentication.class);
                        UtilityUCAS.setAuthentication(authentication);
                        startRequest(this.requestAPI, this.requestParams,this.onResponding);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                } else {
                    if (RequestAPI.AUTHENTICATION != requestAPI)
                        UtilityUCAS.setCashData(requestAPI.name(), objectHashMap.get(RESULT).toString());
                   //on responding interface is null
                    onResponding.onResponding(requestAPI, StatusConnection.SUCCESS, objectHashMap);
                }
                break;
            case 401:

                if (objectHashMap.get(RESULT) != null) {
                    JSONObject ObJsonObject = new JSONObject(objectHashMap.get(RESULT).toString());
                    StatusResult statusResult = new Gson().fromJson(String.valueOf(ObJsonObject), StatusResult.class);
                    Log.e(TAG + ":test", statusResult.toString());
                    if (statusResult != null) {
                        switch (statusResult.getCodeError()) {
                            case INVALID_OR_EXPIRED:
                                HashMap<String, String> params = new HashMap<>();
                                params.put("grant_type", "refresh_token");
                                params.put("refresh_token", UtilityUCAS.getAuthentication().getRefreshToken());
                                params.put("client_secret", CLIENT_SECRET);
                                params.put("client_id", CLIENT_ID);
                                // objectHashMap = initializeHashMap();
                                //new WebService()
                                getConnection(RequestAPI.AUTHENTICATION_UPDATE, params);

                                //fixed hashmap is null
//problem goes here  status code null pointer excption
                                /**
                                 * 06-27 03:05:01.695 13370-13434/ps.edu.ucas.portal E/Volley: [218] NetworkDispatcher.run: Unhandled exception java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.Object java.util.HashMap.put(java.lang.Object, java.lang.Object)' on a null object reference
                                 java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.Object java.util.HashMap.put(java.lang.Object, java.lang.Object)' on a null object reference
                                 at ps.edu.ucas.portal.service.WebService$3.parseNetworkResponse(WebService.java:207)
                                 at com.android.volley.NetworkDispatcher.run(NetworkDispatcher.java:123)
                                 06-27 03:05:01.695 13370-13370/ps.edu.ucas.portal D/Volley: [1] 2.onErrorResponse: webService
                                 06-27 03:05:01.695 13370-13370/ps.edu.ucas.portal D/Volley: [1] 2.onErrorResponse: webService
                                 06-27 03:05:01.696 13370-13370/ps.edu.ucas.portal W/System.err: java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.Object java.util.HashMap.get(java.lang.Object)' on a null object reference
                                 06-27 03:05:01.696 13370-13370/ps.edu.ucas.portal W/System.err:     at ps.edu.ucas.portal.service.WebService.sendRequest(WebService.java:235)
                                 06-27 03:05:01.696 13370-13370/ps.edu.ucas.portal W/System.err:     at ps.edu.ucas.portal.service.WebService.access$100(WebService.java:36)
                                 06-27 03:05:01.696 13370-13370/ps.edu.ucas.portal W/System.err:     at ps.edu.ucas.portal.service.WebService$2.onErrorResponse(WebService.java:174)
                                 06-27 03:05:01.696 13370-13370/ps.edu.ucas.portal W/System.err:     at com.android.volley.Request.deliverError(Request.java:564)
                                 06-27 03:05:01.696 13370-13370/ps.edu.ucas.portal W/System.err:     at com.android.volley.ExecutorDelivery$ResponseDeliveryRunnable.run(ExecutorDelivery.java:101)
                                 06-27 03:05:01.696 13370-13370/ps.edu.ucas.portal W/System.err:     at android.os.Handler.handleCallback(Handler.java:739)
                                 06-27 03:05:01.696 13370-13370/ps.edu.ucas.portal W/System.err:     at android.os.Handler.dispatchMessage(Handler.java:95)
                                 06-27 03:05:01.696 13370-13370/ps.edu.ucas.portal W/System.err:     at android.os.Looper.loop(Looper.java:148)
                                 06-27 03:05:01.696 13370-13370/ps.edu.ucas.portal W/System.err:     at android.app.ActivityThread.main(ActivityThread.java:5417)
                                 06-27 03:05:01.696 13370-13370/ps.edu.ucas.portal W/System.err:     at java.lang.reflect.Method.invoke(Native Method)
                                 06-27 03:05:01.696 13370-13370/ps.edu.ucas.portal W/System.err:     at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:726)
                                 06-27 03:05:01.696 13370-13370/ps.edu.ucas.portal W/System.err:     at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:616)
                                 *
                                 */
                                break;
                            case MESSAGE_ERROR:
                            case DENIED:
                            case OTHER:
                            case UNKNOWING:
                                onResponding.onResponding(requestAPI, StatusConnection.NO_PRIVILEGE, objectHashMap);
                                break;
                        }

                    } else
                        onResponding.onResponding(requestAPI, StatusConnection.NO_PRIVILEGE, objectHashMap);
                } else
                    onResponding.onResponding(requestAPI, StatusConnection.NO_PRIVILEGE, objectHashMap);
                break;
            case 400:
            case 404:
            case 409:
            case 410:
                onResponding.onResponding(requestAPI, StatusConnection.OTHER, objectHashMap);
                break;
            default:
                onResponding.onResponding(requestAPI, StatusConnection.OTHER, objectHashMap);

        }
    }


    private void getURLConnection(final RequestAPI requestAPI, final HashMap<String, String> dataRequest) {
        objectHashMap = initializeHashMap();

        final Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                try {
                    sendRequest(requestAPI);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(requestAPI.getValue());

                    urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod(requestAPI.getRequestMethod() == POST ? "POST" : "GET");
                    urlConnection.setConnectTimeout(100000);
                    urlConnection.setReadTimeout(100000);

                    urlConnection.connect();
                    int responseCode = urlConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        Log.d(TAG, "URL CONNECTION");
                        Log.d(TAG + ":URL", requestAPI.getValue());
                        objectHashMap.put(RESULT, sb.toString());
                        objectHashMap.put(RESPONSE_CODE, responseCode);
                    } else {
                        objectHashMap.put(RESPONSE_CODE, responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    objectHashMap.put(RESPONSE_CODE, 0);

                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                        Log.d(TAG + ":code", ""+(int) objectHashMap.get(RESPONSE_CODE));
                        Log.d(TAG, objectHashMap.get(RESULT).toString());
                        mHandler.sendMessage(new Message());


                }


            }
        });

        thread.start();



    }


    public interface OnResponding {
        void onResponding(RequestAPI requestAPI, StatusConnection statusConnection, HashMap<String, Object> objectResult);
    }


    public enum StatusConnection {
        SUCCESS, NO_PRIVILEGE, NO_CONNECTION, OTHER;
    }


    private HashMap<String, Object> initializeHashMap() {
        HashMap<String, Object> result = new HashMap();
        result.put(RESPONSE_CODE, 0);
        result.put(RESULT, null);
        result.put(IS_CASH, false);

        return result;


    }

}