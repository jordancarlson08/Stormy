package me.jordancarlson.stormy.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.jordancarlson.stormy.R;
import me.jordancarlson.stormy.utils.ToolbarUtil;
import me.jordancarlson.stormy.weather.Current;
import me.jordancarlson.stormy.weather.Day;
import me.jordancarlson.stormy.weather.Forecast;
import me.jordancarlson.stormy.weather.Hour;


public class MainActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String LOCATION_NAME = "LOCATION_NAME";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    private static final String TITLE = "title";
    private static final String BUTTON = "button";
    private static final String BODY = "body";


    private double mLat;
    private double mLng;
    private Forecast mForecast;
    @InjectView(R.id.timeLabel) TextView mTimeLabel;
    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.humidityValue) TextView mHumidityValue;
    @InjectView(R.id.precipValue) TextView mPrecipValue;
    @InjectView(R.id.summaryLabel) TextView mSummaryLabel;
    @InjectView(R.id.iconImageView) ImageView mIconImageView;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;
    @InjectView(R.id.relativeLayout) RelativeLayout mLayout;
    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        ToolbarUtil.setupToolbar(this);

        // Location Stuff
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000) //10 seconds
                .setFastestInterval(1 * 1000); // 1 second

        // Swipe to refresh
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getForecast(mLat, mLng);
                        updateLocationLabel(mLocation);
                        swipeView.setRefreshing(false);
                    }
                }, 1);
            }
        });

        // Navigation Drawer




        Log.d(TAG, "Main UI ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void getCityState(double latitude, double longitude){
        String apiUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true";

        if (isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mLocation = getLocationName(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateLocationLabel(mLocation);
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            alertUserAboutNetworkError();
        }
    }

    private String getLocationName(String jsonData) throws JSONException{
        String state = "";
        String city = "";
        JSONObject geocode = new JSONObject(jsonData);
        JSONArray results_list = geocode.getJSONArray("results");
        JSONObject result = (JSONObject) results_list.get(0);
        JSONArray address_components = result.getJSONArray("address_components");

        for (int i=0; i < address_components.length(); i++) {
            JSONObject component = (JSONObject) address_components.get(i);
            JSONArray types = component.getJSONArray("types");

            for (int x=0; x < types.length(); x++) {
                String type = (String) types.get(x);
                if (type.equalsIgnoreCase("administrative_area_level_1")) {
                    state = component.getString("short_name");
                } else if (type.equalsIgnoreCase("locality")) {
                    city = component.getString("short_name");
                }

            }
        }

        Log.d(TAG, city +", "+ state);

        if (!city.isEmpty() && !state.isEmpty()) {
            return city + ", " + state;
        } else if (city.isEmpty()) {
            return state;
        } else if (state.isEmpty()){
            return city;
        } else {
            return "Unknow City";
        }

    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "cb418599d67f172b250a72119e8a601d";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            alertUserAboutNetworkError();
        }
    }

    private void updateLocationLabel(String location) {
        mLocationLabel.setText(location);
    }
    private void updateDisplay() {
        Current current = mForecast.getCurrent();
        Drawable drawable = getResources().getDrawable(current.getIconId());

        mIconImageView.setImageDrawable(drawable);
        mTemperatureLabel.setText(current.getTemperature()+"");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecip()+"%");
        mSummaryLabel.setText(current.getSummary());
//        mLayout.setBackgroundColor(Color.parseColor(current.getBgColor()));
        mLayout.setBackground(current.getBgDrawable(getResources()));

    }
    private Forecast parseForecastDetails (String jsonData) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourForecast(getHourlyForecast(jsonData));
        forecast.setDayForecast(getDailForecast(jsonData));

        return forecast;
    }

    private Day[] getDailForecast(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days =  new Day[data.length()];

        for (int i=0; i < data.length(); i++) {

            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();

            day.setSummary(jsonDay.getString("summary"));
            day.setIcon(jsonDay.getString("icon"));
            day.setMaxTemp(jsonDay.getDouble("temperatureMax"));
            day.setMinTemp(jsonDay.getDouble("temperatureMin"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timezone);

            days[i] = day;
        }

        return days;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for (int i=0; i < data.length(); i++) {

            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setSummary(jsonHour.getString("summary"));
            hour.setTimezone(timezone);

            hours[i] = hour;

        }

        return hours;

    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();

        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecip(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimezone(timezone);


        /* Remove after testing

        Random generator = new Random();
        double randomValue = 1 + (120 - 1) * generator.nextDouble();

        current.setTemperature(randomValue);

        */

        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString(TITLE, getString(R.string.error_title));
        bundle.putString(BODY, getString(R.string.error_body));
        bundle.putString(BUTTON, getString(R.string.ok));

        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "error_dialog");
    }
    private void alertUserAboutNetworkError() {
        AlertDialogFragment dialog = new AlertDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString(TITLE, getString(R.string.error_title));
        bundle.putString(BODY, getString(R.string.network_unavailable));
        bundle.putString(BUTTON, getString(R.string.ok));

        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        mLat = location.getLatitude();
        mLng = location.getLongitude();

        getForecast(mLat, mLng);
        getCityState(mLat, mLng);

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, ">>>>>>>>>>Location services connected!");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, ">>>>>>>>>>Location services suspended. Please reconnect!");
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @OnClick(R.id.daily)
    public void startDailyActivity(View view){
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDayForecast());
        intent.putExtra(LOCATION_NAME, mLocation);
        startActivity(intent);
    }

    @OnClick(R.id.hourly)
    public void startHourlyActivity(View view){
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourForecast());
        startActivity(intent);
    }


}
