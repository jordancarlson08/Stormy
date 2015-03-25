package me.jordancarlson.stormy.weather;

/**
 * Created by jcarlson on 3/25/15.
 */
public class Forecast {
    private Current mCurrent;
    private Hour[] mHourForecast;
    private Day[] mDayForecast;

    public Current getCurrent() {
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
}
