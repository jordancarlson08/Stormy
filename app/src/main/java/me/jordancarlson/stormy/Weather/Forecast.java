package me.jordancarlson.stormy.weather;

import me.jordancarlson.stormy.R;

/**
 * Created by jcarlson on 3/25/15.
 */
public class Forecast {
    private Current mCurrent;
    private Hour[] mHourForecast;
    private Day[] mDayForecast;

    public Current getCurrent()  {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Hour[] getHourForecast() {
        return mHourForecast;
    }

    public void setHourForecast(Hour[] hourForecast) {
        mHourForecast = hourForecast;
    }

    public Day[] getDayForecast() {
        return mDayForecast;
    }

    public void setDayForecast(Day[] dayForecast) {
        mDayForecast = dayForecast;
    }

    public static int getIconId (String iconString) {

        int iconId = R.drawable.clear_day;

        if (iconString.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (iconString.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (iconString.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (iconString.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (iconString.equals("sleet")) {
            iconId = R.drawable.snow;
        }
        else if (iconString.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (iconString.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (iconString.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }
    public static int getIconLgId (String iconString) {

        int iconId = R.drawable.clear_day_lg;

        if (iconString.equals("clear-day")) {
            iconId = R.drawable.clear_day_lg;
        }
        else if (iconString.equals("clear-night")) {
            iconId = R.drawable.clear_night_lg;
        }
        else if (iconString.equals("rain")) {
            iconId = R.drawable.rain_lg;
        }
        else if (iconString.equals("snow")) {
            iconId = R.drawable.snow_lg;
        }
        else if (iconString.equals("sleet")) {
            iconId = R.drawable.snow_lg;
        }
        else if (iconString.equals("wind")) {
            iconId = R.drawable.wind_lg;
        }
        else if (iconString.equals("fog")) {
            iconId = R.drawable.fog_lg;
        }
        else if (iconString.equals("cloudy")) {
            iconId = R.drawable.cloudy_lg;
        }
        else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy_lg;
        }
        else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night_lg;
        }

        return iconId;
    }
}
