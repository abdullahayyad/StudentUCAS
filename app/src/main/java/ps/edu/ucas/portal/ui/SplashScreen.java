package ps.edu.ucas.portal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.User;
import ps.edu.ucas.portal.service.WebService;
import ps.edu.ucas.portal.service.alarm.AlarmService;
import ps.edu.ucas.portal.service.alarm.AlarmServiceBroadcastReciever;
import ps.edu.ucas.portal.utils.UtilityUCAS;

import static ps.edu.ucas.portal.service.WebService.RESULT;


public class SplashScreen extends AppCompatActivity implements WebService.OnResponding {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
         Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    if(UtilityUCAS.isLogin()) {
                        new WebService().startRequest(WebService.RequestAPI.PROFILE, null, SplashScreen.this);
                    }else{
                        Intent i = new Intent(SplashScreen.this,LeaderboardActivity.class);
                        startActivity(i);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.run();



        sendBroadcast(new Intent(this, AlarmServiceBroadcastReciever.class));
    }

    @Override
    public void onResponding(WebService.RequestAPI requestAPI, WebService.StatusConnection statusConnection, HashMap<String, Object> objectResult) {
            switch (statusConnection) {
                case SUCCESS:
                  if (requestAPI == WebService.RequestAPI.PROFILE) {
                        try {
                            JSONObject ObJsonObject = new JSONObject(objectResult.get(RESULT).toString());
                            User user = new Gson().fromJson(String.valueOf(ObJsonObject), User.class);
                            UtilityUCAS.setUserData(user);

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {

                        }
                    }

                    break;
            }
        Intent i = new Intent(this,MainContainerActivity.class);
        startActivity(i);
        finish();
    }






}
