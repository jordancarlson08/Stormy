package me.jordancarlson.stormy.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jordancarlson.stormy.R;
import me.jordancarlson.stormy.adapters.DayAdapter;
import me.jordancarlson.stormy.adapters.HourAdapter;
import me.jordancarlson.stormy.ui.MainActivity;
import me.jordancarlson.stormy.weather.Day;
import me.jordancarlson.stormy.weather.Hour;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailyForecastFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyForecastFragment extends Fragment {
    private Day[] mDays;
    private String mLocation;
    @InjectView(android.R.id.list) ListView mListView;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;
    @InjectView(android.R.id.empty) TextView mEmptyTextView;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HourlyForecastFragment.
     */
    public static DailyForecastFragment newInstance(Parcelable[] days, String location) {
        DailyForecastFragment fragment = new DailyForecastFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(MainActivity.DAILY_FORECAST, days);
        args.putString("LOCATION", location);
        fragment.setArguments(args);
        return fragment;
    }

    public DailyForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Parcelable[] parcelables = getArguments().getParcelableArray(MainActivity.DAILY_FORECAST);
            mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);
            mLocation = getArguments().getString("LOCATION");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_forecast, container, false);

        ButterKnife.inject(this, view);

        DayAdapter adapter = new DayAdapter(getActivity(), mDays);

        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmptyTextView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dayOfTheWeek = mDays[position].getDayOfTheWeek();
                String conditions = mDays[position].getSummary();
                String maxTemp = mDays[position].getMaxTemp() + "";

                String message = String.format("On %s the high will be %s and it will be %s",
                        dayOfTheWeek,
                        maxTemp,
                        conditions);

//                Toast.makeText(DailyForecastFragment.this, message, Toast.LENGTH_LONG).show();
            }
        });

        mLocationLabel.setText(mLocation);

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
