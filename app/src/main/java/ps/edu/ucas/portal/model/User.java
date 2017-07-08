package ps.edu.ucas.portal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ayyad on 7/13/2015.
 */
public class User implements Serializable{

    @SerializedName("student_no")
    private String id;

    @SerializedName("user_full_name")
    private String fullName;

    @SerializedName("department_no")
    private String departmentNumber;

    @SerializedName("section_no")
    private String sectionNumber;


    @SerializedName("mail")
    private String mail;


    @SerializedName("user_name")
    private String username;


    @SerializedName("section_name")
    private String sectionName;


    @SerializedName("department_name")
    private String department_name;


    @SerializedName("success_hr")
    private String successHR;


    @SerializedName("study_hr")
    private String studyHR;


    @SerializedName("remain_hr")
    private String remainHR;



    @SerializedName("std_avg")
    private String stdAVG;



    @SerializedName("std_avg_grad")
    private String stdAvgGrad;


    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public String getMail() {
        return mail;
    }

    public String getUsername() {
        return username;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public String getSuccessHR() {
        return successHR;
    }

    public String getStudyHR() {
        return studyHR;
    }

    public String getRemainHR() {
        return remainHR;
    }

    public String getStdAVG() {
        return stdAVG;
    }

    public String getStdAvgGrad() {
        return stdAvgGrad;
    }
}
