package ps.edu.ucas.portal.ui;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.AlarmEvent;
import ps.edu.ucas.portal.service.alarm.AlarmAlertBroadcastReceiver;
import ps.edu.ucas.portal.service.alarm.AlarmService;
import ps.edu.ucas.portal.service.alarm.AlarmServiceBroadcastReciever;


/**
 * Created by Hazem Lababidi on 9/21/2015.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String MY_NOTIFICATIONS_SETTING = "notification";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
         if(key.equals(MY_NOTIFICATIONS_SETTING)){
             if(sharedPreferences.getBoolean(MY_NOTIFICATIONS_SETTING,true)) {
                 sendBroadcast(new Intent(this, AlarmServiceBroadcastReciever.class));
             }else{
                 if(isMyServiceRunning(AlarmService.class))
                     stopService(new Intent(this, AlarmService.class));
                 else{
                     if(!isMyServiceRunning(AlarmService.class))
                         startService(new Intent(this, AlarmService.class));
                 }
                 Intent myIntent = new Intent(getApplicationContext(), AlarmAlertBroadcastReceiver.class);
                 myIntent.putExtra("AlarmEvent", new AlarmEvent());
                 PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                 AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                 alarmManager.cancel(pendingIntent);
             }
         }



    }



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}
