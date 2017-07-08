package ps.edu.ucas.portal.model;

import android.os.Parcel;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ps.edu.ucas.portal.R;


/**
 * Created by Ayyad on 8/1/2015.
 */
public class EventAcademicCalendar implements Serializable {


    @SerializedName("smsemester_id")
    private String smsemesterId;
    @SerializedName("semester_name")
    private String semesterName;
    @SerializedName("semester_status")
    private String semesterStatus;

    @SerializedName("events")
    private ArrayList<Event> events;

    public ArrayList<Event> getEvents() {
        return events;
    }

    public AcademicCalendarPages getSemester() {
        if (semesterStatus.equals("0")) {
            return AcademicCalendarPages.CURRENT;
        } else if (semesterStatus.equals("-1")) {
            return AcademicCalendarPages.PREVIOUS;
        } else if (semesterStatus.equals("1")) {
            return AcademicCalendarPages.NEXT;

        }
        return null;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public enum AcademicCalendarPages {
        PREVIOUS,
        CURRENT,
        NEXT
    }




    public static class Event implements Serializable{

        @SerializedName("event_date")
        private String eventDate;

        @SerializedName("event_day")
        private String eventDay;

        @SerializedName("event_title")
        private String eventTitle;




        public String getEventDay() {
            return eventDay;
        }

        public String getEventTitle() {
            return eventTitle;
        }


        public Date getEventDate(){
            Date date = null;
                    DateFormat dateFormater = new SimpleDateFormat("M/d/yy H:mm:ss a",Locale.US);
            try {
                date = dateFormater.parse(eventDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }

        public static int getColor(Event event) {
            Date date = new Date();

            if(date.before(event.getEventDate())){
                return R.color.seven;
            }else
                return R.color.two;
        }
        public static String getMonthName(int month){
           return new DateFormatSymbols().getMonths()[month];
        }
    }
}
