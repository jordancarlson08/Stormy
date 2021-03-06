package me.jordancarlson.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jcarlson on 3/25/15.
 */
public class Hour implements Parcelable{
    private long mTime;
    private String mSummary;
    private double mTemperature;
    private String mIcon;
    private String mTimezone;

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getHour(){
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
        Date date = new Date(mTime*1000);

        return formatter.format(date);
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mTime);
        dest.writeString(this.mSummary);
        dest.writeDouble(this.mTemperature);
        dest.writeString(this.mIcon);
        dest.writeString(this.mTimezone);
    }

    public Hour() {
    }

    private Hour(Parcel in) {
        this.mTime = in.readLong();
        this.mSummary = in.readString();
        this.mTemperature = in.readDouble();
        this.mIcon = in.readString();
        this.mTimezone = in.readString();
    }

    public static final Creator<Hour> CREATOR = new Creator<Hour>() {
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };
}
