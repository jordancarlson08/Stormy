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

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jordancarlson.stormy.R;
import me.jordancarlson.stormy.adapters.HourAdapter;
import me.jordancarlson.stormy.ui.MainActivity;
import me.jordancarlson.stormy.utils.Constants;
import me.jordancarlson.stormy.weather.Hour;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HourlyForecastFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HourlyForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HourlyForecastFragment extends Fragment {

    private Hour[] mHours;
    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HourlyForecastFragment.
     */
    public static HourlyForecastFragment newInstance(Parcelable[] hours) {
        HourlyForecastFragment fragment = new HourlyForecastFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(Constants.HOURLY_FORECAST, hours);
        fragment.setArguments(args);
        return fragment;
    }

    public HourlyForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Parcelable[] parcelables = getArguments().getParcelableArray(Constants.HOURLY_FORECAST);
            mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hourly_forecast, container, false);

        ButterKnife.inject(this, view);

        HourAdapter adapter = new HourAdapter(getActivity(), mHours);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        //if data is of a fixed size
        mRecyclerView.setHasFixedSize(true);

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
