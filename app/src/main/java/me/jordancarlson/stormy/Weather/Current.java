package me.jordancarlson.stormy.weather;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import me.jordancarlson.stormy.R;

/**
 * Created by jcarlson on 3/23/15.
 */
public class Current {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecip;
    private String mSummary;
    private String mState;
    private String mCity;
    public Drawable mBgDrawable;
    private String mBgColor;

    public String getBgColor() {
        if(mTemperature < 20){
            //dark blue
            return "#FF4682B4";
        } else if (mTemperature < 40) {
            // light blue
            return "#FF87CEFA";
        } else if (mTemperature < 60) {
        // light green
            return "#FF98FB98";
        } else if (mTemperature < 80) {
            // light yellow
            return "#FFF0E68C";
        } else if (mTemperature < 100) {
            // light orange
            return "#FFF19F11";
        } else {
            // Orange
            return "#FFFF8C00";
        }
    }

    public Drawable getBgDrawable(Resources resources) {
        if(mTemperature < 20){
            //dark blue
            return resources.getDrawable(R.drawable.bg_coldest);
        } else if (mTemperature < 40) {
            // light blue
            return resources.getDrawable(R.drawable.bg_cold);
        } else if (mTemperature < 60) {
            // light green
            return resources.getDrawable(R.drawable.bg_cool);
        } else if (mTemperature < 80) {
            // light yellow
            return resources.getDrawable(R.drawable.bg_warm);
        } else if (mTemperature < 100) {
            // light orange
            return resources.getDrawable(R.drawable.bg_hot);
        } else {
            // Orange
            return resources.getDrawable(R.drawable.bg_hottest);
        }
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    private String mTimezone;

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
        Date dateTime = new Date(getTime() * 1000);
        String time = formatter.format(dateTime);
        return time;
    }

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getPrecip() {
        double precipPercent = mPrecip * 100;
        return (int) Math.round(precipPercent);
    }

    public void setPrecip(double precip) {
        mPrecip = precip;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }
}
