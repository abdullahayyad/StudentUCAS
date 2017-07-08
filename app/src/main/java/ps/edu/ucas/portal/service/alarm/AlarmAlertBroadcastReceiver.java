package ps.edu.ucas.portal.service.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.AlarmEvent;
import ps.edu.ucas.portal.ui.SettingsActivity;
import ps.edu.ucas.portal.ui.SplashScreen;
import ps.edu.ucas.portal.ui.fragment.PagesFragment;
import ps.edu.ucas.portal.utils.UtilityUCAS;


/**
 * Created by ayyad on 7/1/17.
 */

public class AlarmAlertBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!UtilityUCAS.isLogin())
            return;
        PreferenceManager.setDefaultValues(context, R.xml.preference, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (!sharedPreferences.getBoolean(SettingsActivity.MY_NOTIFICATIONS_SETTING,true))
            return;



        final AlarmEvent alarm = (AlarmEvent) intent.getExtras().getSerializable("alarm");
        showNotification(context,alarm);

        Log.e("alarmed","alarmd");


        Intent mathAlarmServiceIntent = new Intent(
                context,
                AlarmServiceBroadcastReciever.class);
        context.sendBroadcast(mathAlarmServiceIntent, null);


    }





    public void showNotification(Context context, AlarmEvent alarmEvent) {
        Intent resultIntent = new Intent(context, SplashScreen.class);
         PendingIntent pi = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
         NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
         mBuilder.setSmallIcon(R.drawable.ic_luncher);
            if (alarmEvent.getType() == PagesFragment.ACADEMIC_CALENDAR)
                mBuilder.setContentTitle(context.getResources().getString(R.string.academic_calender));
            else if (alarmEvent.getType() == PagesFragment.EVENT_DAY)
                mBuilder.setContentTitle(context.getResources().getString(R.string.exam_student));
            mBuilder.setContentIntent(pi);
            mBuilder.setAutoCancel(true);
        String msg = "";
        if (alarmEvent.getDay() == AlarmEvent.Day.SEVEN)
            msg = context.getResources().getString(R.string.message_event_academic_calender_day_7) + " " + alarmEvent.getTitle();
        else if (alarmEvent.getDay() == AlarmEvent.Day.THREE) {
            //mBuilder .setContentText(context.getResources().getString(R.string.message_event_academic_calender_day_3) + " " + message);
            msg = context.getResources().getString(R.string.message_event_academic_calender_day_3) + " " + alarmEvent.getTitle();

        }
        mBuilder.setContentText(msg);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));


        //Vibration
        mBuilder.setVibrate(new long[]{500, 500, 500});
        //LED
        mBuilder.setLights(Color.BLUE, 3000, 1000);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Sound
        mBuilder.setSound(alarmSound);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);


        if (alarmEvent.getDay() == AlarmEvent.Day.SEVEN)
            mNotificationManager.notify(123, mBuilder.build());
        if (alarmEvent.getDay() == AlarmEvent.Day.THREE)
            mNotificationManager.notify(1234, mBuilder.build());


    }


}
