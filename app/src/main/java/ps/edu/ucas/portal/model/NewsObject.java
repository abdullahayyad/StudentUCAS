package ps.edu.ucas.portal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ayyad on 11/25/2015.
 */
public class NewsObject implements Parcelable {
    String title = "";
    String description = "";
    String link = "";
    String pubDate = "";
    String imageURL = "";

    public NewsObject(String title, String description, String link, String pubDate, String imageURL) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.imageURL = imageURL;
    }


    public NewsObject(){

    }

    public NewsObject(Parcel parcel) {

        title = parcel.readString();
        description = parcel.readString();
        link = parcel.readString();
        pubDate = parcel.readString();
        imageURL = parcel.readString();

    }


    @SuppressWarnings("unused")
    private void readFromParcel(Parcel parcel) {
        title = parcel.readString();
        description = parcel.readString();
        link = parcel.readString();
        pubDate = parcel.readString();
        imageURL = parcel.readString();


    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeString(pubDate);
        dest.writeString(imageURL);

    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getImageURL() {
        return imageURL;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPubDate(String pubDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
            Date d = sdf.parse(pubDate);
            sdf.applyPattern("M/d/yyyy");
            this.pubDate = sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            this.pubDate = pubDate;
        }

    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public static final Creator<NewsObject> CREATOR = new Creator<NewsObject>() {
        @Override
        public NewsObject createFromParcel(Parcel source) {
            return new NewsObject(source);
        }

        @Override
        public NewsObject[] newArray(int size) {
            return new NewsObject[size];
        }
    };


}
