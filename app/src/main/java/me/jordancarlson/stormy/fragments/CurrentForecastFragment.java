package me.jordancarlson.stormy.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import me.jordancarlson.stormy.ui.DailyForecastActivity;
import me.jordancarlson.stormy.ui.MainActivity;
import me.jordancarlson.stormy.weather.Current;
import me.jordancarlson.stormy.weather.Day;
import me.jordancarlson.stormy.weather.Forecast;
import me.jordancarlson.stormy.weather.Hour;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentForecastFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//public class CurrentForecastFragment extends Fragment implements
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        LocationListener {

public class CurrentForecastFragment extends Fragment {

//    public static final String DAILY_FORECAST = "DAILY_FORECAST";
//    public static final String LOCATION_NAME = "LOCATION_NAME";
//    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";

    private static final String TAG = CurrentForecastFragment.class.getSimpleName();
//    private static final String TITLE = "title";
//    private static final String BUTTON = "button";
//    private static final String BODY = "body";
//    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private Activity mActivity;
    private FragmentActivity mContext;
    private OnFragmentInteractionListener mListener;
    private Current mCurrent;
    private String mLocation;
//    private GoogleApiClient mGoogleApiClient;
//    private LocationRequest mLocationRequest;
//    private Forecast mForecast;
//    private String mLocation;
//    private double mLat;
//    private double mLng;

    @InjectView(R.id.timeLabel) TextView mTimeLabel;
    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.humidityValue) TextView mHumidityValue;
    @InjectView(R.id.precipValue) TextView mPrecipValue;
    @InjectView(R.id.summaryLabel) TextView mSummaryLabel;
    @InjectView(R.id.iconImageView) ImageView mIconImageView;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;
//    @InjectView(R.id.relativeLayout) RelativeLayout mLayout;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CurrentForecastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentForecastFragment newInstance(Forecast forecast, String location) {
        CurrentForecastFragment fragment = new CurrentForecastFragment();
        Bundle args = new Bundle();
        args.putParcelable("CURRENT", forecast.getCurrent());
        args.putString("LOCATION", location);
        fragment.setArguments(args);
        return fragment;
    }

    public CurrentForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = super.getActivity();
        if (getArguments() != null) {
            Bundle args = getArguments();
            Parcelable parcelable = args.getParcelable("CURRENT");
            mCurrent = (Current) parcelable;
            mLocation = args.getString("LOCATION");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_forecast, container, false);
        ButterKnife.inject(this, view);
//        InitLocationService();

        updateDisplay();
        updateLocationLabel(mLocation);

        return view;
    }

//    private void InitLocationService() {
//        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//
//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(10 * 1000) //10 seconds
//                .setFastestInterval(1 * 1000); // 1 second
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        mContext = (FragmentActivity) activity;
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        mActivity = super.getActivity();
//        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (mGoogleApiClient.isConnected()){
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//            mGoogleApiClient.disconnect();
//        }
    }

    private void updateLocationLabel(String location) {
        mLocationLabel.setText(location);
    }
    private void updateDisplay() {
        Current current = mCurrent;
        Drawable drawable = getResources().getDrawable(current.getIconId());

        mIconImageView.setImageDrawable(drawable);
        mTemperatureLabel.setText(current.getTemperature()+"");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecip() + "%");
        mSummaryLabel.setText(current.getSummary());
//        mLayout.setBackgroundColor(Color.parseColor(current.getBgColor()));
//        mLayout.setBackground(current.getBgDrawable(getResources()));

    }

//    private void getCityState(double latitude, double longitude){
//        String apiUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true";
//
//        if (isNetworkAvailable()) {
//
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder()
//                    .url(apiUrl)
//                    .build();
//
//            Call call = client.newCall(request);
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//                    alertUserAboutError();
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    try {
//                        String jsonData = response.body().string();
//                        Log.v(TAG, jsonData);
//                        if (response.isSuccessful()) {
//                            mLocation = getLocationName(jsonData);
//                            mActivity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    updateLocationLabel(mLocation);
//                                }
//                            });
//
//                        } else {
//                            alertUserAboutError();
//                        }
//                    } catch (IOException | JSONException e) {
//                        Log.e(TAG, "Exception caught: ", e);
//                    }
//                }
//            });
//        } else {
//            alertUserAboutNetworkError();
//        }
//    }

//    private String getLocationName(String jsonData) throws JSONException{
//        String state = "";
//        String city = "";
//        JSONObject geocode = new JSONObject(jsonData);
//        JSONArray results_list = geocode.getJSONArray("results");
//        JSONObject result = (JSONObject) results_list.get(0);
//        JSONArray address_components = result.getJSONArray("address_components");
//
//        for (int i=0; i < address_components.length(); i++) {
//            JSONObject component = (JSONObject) address_components.get(i);
//            JSONArray types = component.getJSONArray("types");
//
//            for (int x=0; x < types.length(); x++) {
//                String type = (String) types.get(x);
//                if (type.equalsIgnoreCase("administrative_area_level_1")) {
//                    state = component.getString("short_name");
//                } else if (type.equalsIgnoreCase("locality")) {
//                    city = component.getString("short_name");
//                }
//
//            }
//        }
//
//        Log.d(TAG, city + ", " + state);
//
//        if (!city.isEmpty() && !state.isEmpty()) {
//            return city + ", " + state;
//        } else if (city.isEmpty()) {
//            return state;
//        } else if (state.isEmpty()){
//            return city;
//        } else {
//            return "Unknown City";
//        }
//
//    }

//    private void getForecast(double latitude, double longitude) {
//        String apiKey = "cb418599d67f172b250a72119e8a601d";
//        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;
//
//        if (isNetworkAvailable()) {
//
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder()
//                    .url(forecastUrl)
//                    .build();
//
//            Call call = client.newCall(request);
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//                    alertUserAboutError();
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//
//                    try {
//                        String jsonData = response.body().string();
//                        Log.v(TAG, jsonData);
//                        if (response.isSuccessful()) {
//                            mForecast = parseForecastDetails(jsonData);
//                            mActivity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    updateDisplay();
//                                }
//                            });
//                        } else {
//                            alertUserAboutError();
//                        }
//                    } catch (IOException | JSONException e) {
//                        Log.e(TAG, "Exception caught: ", e);
//                    }
//                }
//            });
//        } else {
//            alertUserAboutNetworkError();
//        }
//    }

//    private Forecast parseForecastDetails (String jsonData) throws JSONException{
//        Forecast forecast = new Forecast();
//        forecast.setCurrent(getCurrentDetails(jsonData));
//        forecast.setHourForecast(getHourlyForecast(jsonData));
//        forecast.setDayForecast(getDailForecast(jsonData));
//
//        return forecast;
//    }

//    private Day[] getDailForecast(String jsonData) throws JSONException{
//        JSONObject forecast = new JSONObject(jsonData);
//        String timezone = forecast.getString("timezone");
//
//        JSONObject daily = forecast.getJSONObject("daily");
//        JSONArray data = daily.getJSONArray("data");
//
//        Day[] days =  new Day[data.length()];
//
//        for (int i=0; i < data.length(); i++) {
//
//            JSONObject jsonDay = data.getJSONObject(i);
//            Day day = new Day();
//
//            day.setSummary(jsonDay.getString("summary"));
//            day.setIcon(jsonDay.getString("icon"));
//            day.setMaxTemp(jsonDay.getDouble("temperatureMax"));
//            day.setMinTemp(jsonDay.getDouble("temperatureMin"));
//            day.setTime(jsonDay.getLong("time"));
//            day.setTimezone(timezone);
//
//            days[i] = day;
//        }
//
//        return days;
//    }
//    private Hour[] getHourlyForecast(String jsonData) throws JSONException{
//        JSONObject forecast = new JSONObject(jsonData);
//        String timezone = forecast.getString("timezone");
//
//        JSONObject hourly = forecast.getJSONObject("hourly");
//        JSONArray data = hourly.getJSONArray("data");
//
//        Hour[] hours = new Hour[data.length()];
//
//        for (int i=0; i < data.length(); i++) {
//
//            JSONObject jsonHour = data.getJSONObject(i);
//            Hour hour = new Hour();
//
//            hour.setTemperature(jsonHour.getDouble("temperature"));
//            hour.setTime(jsonHour.getLong("time"));
//            hour.setIcon(jsonHour.getString("icon"));
//            hour.setSummary(jsonHour.getString("summary"));
//            hour.setTimezone(timezone);
//
//            hours[i] = hour;
//
//        }
//
//        return hours;
//
//    }
//
//    private Current getCurrentDetails(String jsonData) throws JSONException {
//        JSONObject forecast = new JSONObject(jsonData);
//        String timezone = forecast.getString("timezone");
//        JSONObject currently = forecast.getJSONObject("currently");
//
//        Current current = new Current();
//
//        current.setHumidity(currently.getDouble("humidity"));
//        current.setTime(currently.getLong("time"));
//        current.setIcon(currently.getString("icon"));
//        current.setPrecip(currently.getDouble("precipProbability"));
//        current.setSummary(currently.getString("summary"));
//        current.setTemperature(currently.getDouble("temperature"));
//        current.setTimezone(timezone);
//
//        return current;
//    }

//    private boolean isNetworkAvailable() {
//        ConnectivityManager manager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
//        boolean isAvailable = false;
//        if (networkInfo != null && networkInfo.isConnected()) {
//            isAvailable = true;
//        }
//        return isAvailable;
//    }
//
//    private void alertUserAboutError() {
//        AlertDialogFragment dialog = new AlertDialogFragment();
//
//        Bundle bundle = new Bundle();
//        bundle.putString(TITLE, getString(R.string.error_title));
//        bundle.putString(BODY, getString(R.string.error_body));
//        bundle.putString(BUTTON, getString(R.string.ok));
//
//        dialog.setArguments(bundle);
//        dialog.show(mContext.getFragmentManager(), "error_dialog");
//    }
//
//    private void alertUserAboutNetworkError() {
//        AlertDialogFragment dialog = new AlertDialogFragment();
//
//        Bundle bundle = new Bundle();
//        bundle.putString(TITLE, getString(R.string.error_title));
//        bundle.putString(BODY, getString(R.string.network_unavailable));
//        bundle.putString(BUTTON, getString(R.string.ok));
//
//        dialog.setArguments(bundle);
//        dialog.show(mContext.getFragmentManager(), "error_dialog");
//    }

//    private void handleNewLocation(Location location) {
//        Log.d(TAG, location.toString());
//
//        mLat = location.getLatitude();
//        mLng = location.getLongitude();
//
//        getForecast(mLat, mLng);
//        getCityState(mLat, mLng);
//
//    }

//    @Override
//    public void onConnected(Bundle bundle) {
////        Log.i(TAG, ">>>>>>>>>>Location services connected!");
////        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
////        if (location == null) {
////            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
////        } else {
////            handleNewLocation(location);
////        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.i(TAG, ">>>>>>>>>>Location services suspended. Please reconnect!");
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        handleNewLocation(location);
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        if (connectionResult.hasResolution()) {
//            try {
//                connectionResult.startResolutionForResult(mActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
//            } catch (IntentSender.SendIntentException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
//        }
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @OnClick(R.id.daily)
    public void startDailyActivity(View view){
//        Intent intent = new Intent(mActivity, DailyForecastActivity.class);
//        intent.putExtra(MainActivity.DAILY_FORECAST, mForecast.getDayForecast());
//        intent.putExtra(MainActivity.LOCATION_NAME, mLocation);
//        startActivity(intent);
    }

    @OnClick(R.id.hourly)
    public void startHourlyActivity(View view) {

//        HourlyForecastFragment fragment = HourlyForecastFragment.newInstance(mForecast.getHourForecast());
//
//        mContext.getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.contentFragment, fragment)
//                .addToBackStack(null)
//                .commit();
//
    }

}
