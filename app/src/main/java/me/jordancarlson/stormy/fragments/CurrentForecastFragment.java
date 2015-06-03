package me.jordancarlson.stormy.fragments;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jordancarlson.stormy.R;
import me.jordancarlson.stormy.weather.Current;
import me.jordancarlson.stormy.weather.Forecast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentForecastFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class CurrentForecastFragment extends Fragment {

    private static final String TAG = CurrentForecastFragment.class.getSimpleName();
    private static final String CURRENT = "CURRENT";
    private static final String LOCATION = "LOCATION";

    private OnFragmentInteractionListener mListener;
    private Current mCurrent;
    private String mLocation;

    @InjectView(R.id.timeLabel) TextView mTimeLabel;
    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.humidityValue) TextView mHumidityValue;
    @InjectView(R.id.precipValue) TextView mPrecipValue;
    @InjectView(R.id.summaryLabel) TextView mSummaryLabel;
    @InjectView(R.id.iconImageView) ImageView mIconImageView;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CurrentForecastFragment.
     */
    public static CurrentForecastFragment newInstance(Forecast forecast, String location) {
        CurrentForecastFragment fragment = new CurrentForecastFragment();
        Bundle args = new Bundle();
        args.putParcelable(CURRENT, forecast.getCurrent());
        args.putString(LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    public CurrentForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            Parcelable parcelable = args.getParcelable(CURRENT);
            mCurrent = (Current) parcelable;
            mLocation = args.getString(LOCATION);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_forecast, container, false);
        ButterKnife.inject(this, view);

        updateDisplay();
        updateLocationLabel(mLocation);

        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void updateLocationLabel(String location) {
        mLocationLabel.setText(location);
    }
    private void updateDisplay() {
        Current current = mCurrent;
        Drawable drawable = getResources().getDrawable(current.getIconLgId());

        mIconImageView.setImageDrawable(drawable);
        mTemperatureLabel.setText(current.getTemperature() + "°");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecip() + "%");
        mSummaryLabel.setText(current.getSummary());
    }


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


}
