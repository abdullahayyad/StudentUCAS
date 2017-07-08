package ps.edu.ucas.portal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ayyad on 11/24/2015.
 */
public class Financial implements Serializable {

    @SerializedName("smsemester_id")
    private String semesterId;
    @SerializedName("semester_name")
    private String semesterName;
    @SerializedName("total_balance")
    private String totalBalance;
    @SerializedName("semester_balance")
    private String semesterBalance;
    @SerializedName("transactions")
    private ArrayList<TransactionsFinancial> transactionsFinancial;

    public String getSemesterId() {
        return semesterId;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public String getSemesterBalance() {
        return semesterBalance;
    }

    public ArrayList<TransactionsFinancial> getTransactionsFinancial() {
        return transactionsFinancial;
    }

    public class TransactionsFinancial implements Serializable {
        @SerializedName("serial")
        private String serial;
        @SerializedName("record_number")
        private String recordNumber;
        @SerializedName("debit")
        private String debit;
        @SerializedName("credit")
        private String credit;
        @SerializedName("date")
        private String date;
        @SerializedName("description")
        private String description;



        public String getDate(){
            String dateString = "";
            SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy H:mm:ss a", Locale.US);
            try {
                Date d = sdf.parse(date);
                sdf.applyPattern("M/d/yyyy");
                dateString = sdf.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return dateString;
        }


        public String getSerial() {
            return serial;
        }

        public String getRecordNumber() {
            return recordNumber;
        }

        public String getDebit() {
            return debit;
        }

        public String getCredit() {
            return credit;
        }

        public String getDescription() {
            return description;
        }
    }




}