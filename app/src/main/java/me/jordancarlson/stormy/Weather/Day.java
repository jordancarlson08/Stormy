package me.jordancarlson.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jcarlson on 3/25/15.
 */
public class Day implements Parcelable {
    private long mTime;
    private String mSummary;
    private double mMaxTemp;
    private double mMinTemp;
    private String mIcon;
    private String mTimezone;

    public Day () {

    }
    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getMaxTemp() {
        return (int) Math.round(mMaxTemp);
    }

    public void setMaxTemp(double maxTemp) {
        mMaxTemp = maxTemp;
    }

    public double getMinTemp() {
        return mMinTemp;
    }

    public void setMinTemp(double minTemp) {
        mMinTemp = minTemp;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }

    public String getDayOfTheWeek() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
        Date dateTime = new Date(mTime * 1000);

        return formatter.format(dateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mTime);
        dest.writeString(mSummary);
        dest.writeDouble(mMaxTemp);
        dest.writeString(mIcon);
        dest.writeString(mTimezone);
    }

    private Day(Parcel in) {
        mTime = in.readLong();
        mSummary = in.readString();
        mMaxTemp = in.readDouble();
        mIcon = in.readString();
        mTimezone = in.readString();
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Day> CREATOR = new Parcelable.Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
