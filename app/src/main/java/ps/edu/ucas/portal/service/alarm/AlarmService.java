package ps.edu.ucas.portal.service.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import ps.edu.ucas.portal.model.AlarmEvent;
import ps.edu.ucas.portal.model.EventAcademicCalendar;
import ps.edu.ucas.portal.service.WebService;
import ps.edu.ucas.portal.ui.fragment.PagesFragment;
import ps.edu.ucas.portal.utils.SortingDate;

import static ps.edu.ucas.portal.service.WebService.RESULT;
import static ps.edu.ucas.portal.service.WebService.RequestAPI.ACADEMIC_CALENDER;

/**
 * Created by ayyad on 6/30/17.
 */

public class AlarmService extends Service implements WebService.OnResponding {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new WebService().startRequest(ACADEMIC_CALENDER, null, this);
        return START_NOT_STICKY;
    }


    @Override
    public void onResponding(WebService.RequestAPI requestAPI, WebService.StatusConnection statusConnection, HashMap<String, Object> objectResult) {

        switch (statusConnection) {
            case SUCCESS:
                if (requestAPI == ACADEMIC_CALENDER) {
                    try {
                        Type collectionType = new TypeToken<ArrayList<EventAcademicCalendar>>() {
                        }.getType();
                        JsonParser parser = new JsonParser();
                        JsonArray element = (JsonArray) parser.parse(objectResult.get(RESULT).toString());
                        JsonElement responseWrapper = element.getAsJsonArray();
                        ArrayList<EventAcademicCalendar> academicCalendars = new Gson().fromJson(responseWrapper, collectionType);
                        init(academicCalendars);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }


    public AlarmEvent getNext(ArrayList<EventAcademicCalendar> academicCalendars) {
        ArrayList<AlarmEvent> alarmEvents = new ArrayList<>();
        Date date = new Date();
        for (EventAcademicCalendar academicCalendar : academicCalendars)
            for (EventAcademicCalendar.Event event : academicCalendar.getEvents())
                if (date.before(event.getEventDate())) {
                    if (getDiffDay(date, event.getEventDate()) >= 3) {
                        AlarmEvent alarmEvent = new AlarmEvent(event.getEventDate(), event.getEventTitle(), AlarmEvent.Day.THREE, PagesFragment.ACADEMIC_CALENDAR);
                        alarmEvents.add(alarmEvent);
                    }

                    if (getDiffDay(date, event.getEventDate()) >= 7) {
                        AlarmEvent alarmEvent = new AlarmEvent(event.getEventDate(), event.getEventTitle(), AlarmEvent.Day.SEVEN, PagesFragment.ACADEMIC_CALENDAR);
                        alarmEvents.add(alarmEvent);
                    }
                }

//        Log.e("Before Alarm", alarmEvents.toString());


        Collections.sort(alarmEvents, new SortingDate());


//        Log.e("After Alarm", alarmEvents.toString());


        return alarmEvents.get(0);

    }


    private long getDiffDay(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diffTime = endTime - startTime;
        long diffDays = diffTime / (1000 * 60 * 60 * 24);
        return diffDays;
    }


    public void init(ArrayList<EventAcademicCalendar> academicCalendars) {
        Log.d(this.getClass().getSimpleName(), "onStartCommand()");
        AlarmEvent alarm = getNext(academicCalendars);
        if (null != alarm) {
            alarm.schedule(getApplicationContext());
            Log.d(this.getClass().getSimpleName(), alarm.getTimeUntilNextAlarmMessage());

        } else {
            Intent myIntent = new Intent(getApplicationContext(), AlarmAlertBroadcastReceiver.class);
            myIntent.putExtra("AlarmEvent", new AlarmEvent());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            alarmManager.cancel(pendingIntent);
        }
    }
}
