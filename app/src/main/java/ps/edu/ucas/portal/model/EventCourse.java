package ps.edu.ucas.portal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import ps.edu.ucas.portal.R;

/**
 * Created by Ayyad on 8/16/2015.
 */
public class EventCourse implements Serializable,Cloneable {

    @SerializedName("CourseId")
    private String courseId;

    @SerializedName("CourseName")
    private String courseName;

    @SerializedName("Teacher")

    private String teacherName;
    @SerializedName("BranchNo")
    private String branchNo;

    @SerializedName("Days")
    private ArrayList<CourseTime> courseTimes;

    public void setCourseTimes(ArrayList<CourseTime> courseTimes) {
        this.courseTimes = courseTimes;
    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    private int color = 0;


    public void setColor(int color) {
        this.color = color;
    }

    public String getCourseId() {
        return courseId;
    }


    public String getCourseName() {
        return courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public ArrayList<CourseTime> getCourseTimes() {
        return courseTimes;
    }



    public class CourseTime implements Serializable{


        @SerializedName("DayOfWeek")
        private String dayOfWeekName;

        @SerializedName("DayOfWeekNumber")
        private String dayOfWeekNumber;

        @SerializedName("RoomNo")
        private String hallName;

        @SerializedName("StartHour")
        private String startHour;

        @SerializedName("StartMinute")
        private String startMinute;

        @SerializedName("EndHour")
        private String endHour;

        @SerializedName("EndMinute")
        private String endMinute;

        private Day day = Day.SUNDAY;


        public Day getDay() {

            return Day.getDay(Integer.parseInt(dayOfWeekNumber));
        }

        public String getCourseTime() {
            return this.startHour + ":" + (this.startMinute.equals("0") ? "00" : startMinute) + " - " + this.endHour + ":" + (this.endMinute.equals("0") ? "00" : endMinute);
        }

        public int getDayOfWeekNumber() {
            return Integer.parseInt(dayOfWeekNumber);
        }

        public String getHallName() {
            return hallName;
        }

        public int getStartHour() {
            return Integer.parseInt(startHour);
        }

        public int getStartMinute() {
            return Integer.parseInt(startMinute);
        }

        public int getEndHour() {
            return Integer.parseInt(endHour);
        }

        public int getEndMinute() {
            return Integer.parseInt(endMinute);
        }

        @Override
        public String toString() {
            return "CourseTime{" +
                    "dayOfWeekName='" + dayOfWeekName + '\'' +
                    ", dayOfWeekNumber='" + dayOfWeekNumber + '\'' +
                    ", hallName='" + hallName + '\'' +
                    ", startHour='" + startHour + '\'' +
                    ", startMinute='" + startMinute + '\'' +
                    ", endHour='" + endHour + '\'' +
                    ", endMinute='" + endMinute + '\'' +
                    ", day=" + day +
                    '}';
        }
    }

    public int getColor(int index){
        int color  = 0;
        switch (index){
            case 1 :
                color = R.color.one; break;
            case 2 :
                color = R.color.two; break;
            case 3 :
                color = R.color.three;break;
            case 4 :
                color = R.color.four;break;
            case 5 :
                color = R.color.five;break;
            case 6 :
                color = R.color.six;break;
            case 7 :
                color = R.color.seven;break;
            case 8 :
                color = R.color.eight;break;
            case 9 :
                color = R.color.nine;break;
            case 10 :
                color = R.color.ten;break;
        }
        this.color = color;
        return color;
    }

    @Override
    public String toString() {
        return "EventCourse{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", branchNo='" + branchNo + '\'' +
                ", courseTimes=" + courseTimes +
                ", color=" + color +
                '}';
    }

    public int getColor() {
        return color;
    }
}
