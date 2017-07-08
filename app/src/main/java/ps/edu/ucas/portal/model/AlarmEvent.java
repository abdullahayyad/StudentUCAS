package ps.edu.ucas.portal.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import ps.edu.ucas.portal.service.alarm.AlarmAlertBroadcastReceiver;
import ps.edu.ucas.portal.ui.fragment.PagesFragment;

/**
 * Created by ayyad on 6/28/17.
 */

public class AlarmEvent implements Serializable{
    private Date date;
    private String title;
    private Day day;
    private PagesFragment type;

    public AlarmEvent(Date date, String title,Day day,PagesFragment type) {
        this.date = date;
        this.title = title;
        this.day = day;
        this.type = type;
    }

    public PagesFragment getType() {
        return type;
    }

    public Day getDay() {
        return day;
    }

    public AlarmEvent() {
    }

    public Date getDate() {
        return date;
    }


    public Long getTimeInMidiSeconds() {
        long diff = 1000*60*60*24;
        if(day == Day.SEVEN)
                diff = diff * 7;
        else
            diff = diff * 3;

        return date.getTime() - diff;
    }


    public String getTitle() {
        return title;
    }

    public void schedule(Context context) {

        Intent myIntent = new Intent(context, AlarmAlertBroadcastReceiver.class);
        myIntent.putExtra("alarm", this);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, getTimeInMidiSeconds(), pendingIntent);
    }


    @Override
    public String toString() {
        return "AlarmEvent{" +
                "date=" + date +
                ", title='" + title + '\'' +
                ", timeAlarm='" + getTimeInMidiSeconds() + '\'' +
                '}';
    }



    public enum Day {
        THREE,SEVEN
    }



    public String getTimeUntilNextAlarmMessage(){
        long timeDifference = getTimeInMidiSeconds() - System.currentTimeMillis();
        long days = timeDifference / (1000 * 60 * 60 * 24);
        long hours = timeDifference / (1000 * 60 * 60) - (days * 24);
        long minutes = timeDifference / (1000 * 60) - (days * 24 * 60) - (hours * 60);
        long seconds = timeDifference / (1000) - (days * 24 * 60 * 60) - (hours * 60 * 60) - (minutes * 60);
        String alert = "Alarm will sound in ";
        if (days > 0) {
            alert += String.format(
                    "%d days, %d hours, %d minutes and %d seconds", days,
                    hours, minutes, seconds);
        } else {
            if (hours > 0) {
                alert += String.format("%d hours, %d minutes and %d seconds",
                        hours, minutes, seconds);
            } else {
                if (minutes > 0) {
                    alert += String.format("%d minutes, %d seconds", minutes,
                            seconds);
                } else {
                    alert += String.format("%d seconds", seconds);
                }
            }
        }
        return alert;
    }



}
