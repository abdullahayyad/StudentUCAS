package ps.edu.ucas.portal.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import ps.edu.ucas.portal.R;

/**
 * Created by Ayyad on 8/15/2015.
 */
public class Transcript implements Serializable{

    @SerializedName("smsemester_id")
    private String semesterId;
    @SerializedName("semester_name")
    private String semesterName;
    @SerializedName("semester_gpa")
    private String semesterGPA;
    @SerializedName("avg_gpa")
    private String avgGPA;
    @SerializedName("semester_hours")
    private String semesterHours;
    @SerializedName("semester_succeed_hours")
    private String semesterSucceedHour;

    @SerializedName("events")
    private ArrayList<CourseMark> courseMarks;

    public String getSemesterId() {
        return semesterId;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public String getSemesterGPA() {
        return semesterGPA;
    }

    public String getAvgGPA() {
        return avgGPA;
    }

    public String getSemesterHours() {
        return semesterHours;
    }

    public String getSemesterSucceedHour() {
        return semesterSucceedHour;
    }

    public ArrayList<CourseMark> getCourseMarks() {
        return courseMarks;
    }

    public class CourseMark implements Serializable {

        @SerializedName("course_no")
        private String courseNO;

        @SerializedName("course_name")
        private String courseName;

        @SerializedName("course_hours")
        private String courseHour;

        @SerializedName("final_mark")
        private String finalMark;

        @SerializedName("midterm_mark")
        private String midtermMark;

        @SerializedName("student_mark")
        private String studentMark;

        @SerializedName("is_paid")
        private String isPaid;

        @SerializedName("is_passed")
        private String isPassed;


        public Boolean getIsPassed() {
            return isPassed.equalsIgnoreCase("1");
        }

        public int getStatusMessage(){
            if(midtermMark.equals("200")){
                return R.string.calculated_hundred;
            }
            else if(midtermMark.equals("12")){
                return R.string.withdrawal;
            }else if (midtermMark.equals("11")){
                return R.string.patchy;
            }
            return 0;
        }


        public int getSecoundMessage(){
            if(studentMark.indexOf("*")!=-1){
                return R.string.off_plan;
            }else
            if(studentMark.indexOf("Tr")!=-1) {
                return R.string.equation;
            }else
            if(studentMark.indexOf("W")!=-1 && studentMark.indexOf("W")<=3 && studentMark.length()<3){
                return R.string.withdrawal;
            }else
            if(studentMark.indexOf("R")!=-1){
                return R.string.replay;
            }else
            if(studentMark.indexOf("I")!=-1){
                return R.string.patchy;
            }

            return 0;
        }



        public boolean isPaid(){
            if(isPaid.equalsIgnoreCase("N")){
                return false;
            }else
                return true;
        }


        public String getCourseNO() {
            return courseNO;
        }

        public String getCourseName() {
            return courseName;
        }

        public String getCourseHour() {
            return courseHour;
        }

        public String getFinalMark() {
            return finalMark;
        }

        public String getMidtermMark() {
            return midtermMark;
        }

        public String getStudentMark() {
            return studentMark;
        }

        public int getStatusColor(){
            if(midtermMark.equals("200")){
                return R.color.warning_transcript;
            }
            else if(midtermMark.equals("12")){
                return  R.color.alarm_transcript;
            }else if (midtermMark.equals("11")){
                return  R.color.message_transcript;
            }
            return Color.TRANSPARENT;
        }


    }

}
