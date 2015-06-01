package me.jordancarlson.stormy.ui;

import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

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
import me.jordancarlson.stormy.R;
import me.jordancarlson.stormy.adapters.DrawerAdapter;
import me.jordancarlson.stormy.fragments.AlertDialogFragment;
import me.jordancarlson.stormy.fragments.CurrentForecastFragment;
import me.jordancarlson.stormy.fragments.DailyForecastFragment;
import me.jordancarlson.stormy.fragments.HourlyForecastFragment;
import me.jordancarlson.stormy.utils.Constants;
import me.jordancarlson.stormy.utils.ToolbarUtil;
import me.jordancarlson.stormy.weather.Current;
import me.jordancarlson.stormy.weather.Day;
import me.jordancarlson.stormy.weather.Forecast;
import me.jordancarlson.stormy.weather.Hour;


public class MainActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        CurrentForecastFragment.OnFragmentInteractionListener,
        HourlyForecastFragment.OnFragmentInteractionListener,
        DailyForecastFragment.OnFragmentInteractionListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private String mTitles[] = {"Current", "Hourly", "Daily", "Settings"};
    private int mIcons[] = {R.drawable.ic_home, R.drawable.ic_clock, R.drawable.ic_calendar ,R.drawable.ic_settings};
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @InjectView(R.id.DrawerLayout) DrawerLayout Drawer;
    @InjectView(R.id.RecyclerView) RecyclerView mRecyclerView;
    @InjectView(R.id.toolbar) Toolbar mToolbar;
    private Forecast mForecast;
    private String mLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        ToolbarUtil.setupToolbar(this);

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new DrawerAdapter(this, mTitles, mIcons, "");
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, mToolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        initLocationService();

        Log.d(TAG, "On Create ");

    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

    private void initLocationService() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000) //10 seconds
                .setFastestInterval(1 * 1000); // 1 second
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        bundle.putString(Constants.ALERT_TITLE, getString(R.string.error_title));
        bundle.putString(Constants.ALERT_BODY, getString(R.string.error_body));
        bundle.putString(Constants.ALERT_BUTTON, getString(R.string.ok));

        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private void alertUserAboutNetworkError() {
        AlertDialogFragment dialog = new AlertDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.ALERT_TITLE, getString(R.string.error_title));
        bundle.putString(Constants.ALERT_BODY, getString(R.string.network_unavailable));
        bundle.putString(Constants.ALERT_BUTTON, getString(R.string.ok));

        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        getForecast(latitude, longitude);
        getCityState(latitude, longitude);
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

                            initFragment();
                            // todo: Send to fragment

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter = new DrawerAdapter(MainActivity.this, mTitles, mIcons, mLocation, mForecast);
                                    mRecyclerView.setAdapter(mAdapter);
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

    private Forecast parseForecastDetails (String jsonData) throws JSONException{
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentForecast(jsonData));
        forecast.setHourForecast(getHourlyForecast(jsonData));
        forecast.setDayForecast(getDailyForecast(jsonData));

        return forecast;
    }
    private Current getCurrentForecast(String jsonData) throws JSONException {
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

        return current;
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
    private Day[] getDailyForecast(String jsonData) throws JSONException{
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


                            // todo: make this update the fragment asynchronously
                          runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
//                                  mCityTextView.setText(mLocation);
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

        Log.d(TAG, city + ", " + state);

        if (!city.isEmpty() && !state.isEmpty()) {
            return city + ", " + state;
        } else if (city.isEmpty()) {
            return state;
        } else if (state.isEmpty()){
            return city;
        } else {
            return "Unknown City";
        }

    }

    private void initFragment() {

        if (!mLocation.isEmpty()) {

            CurrentForecastFragment fragment = CurrentForecastFragment.newInstance(mForecast, mLocation);

            // Start fragment with bundle
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentFragment, fragment)
                    .commit();
        }
    }

}
