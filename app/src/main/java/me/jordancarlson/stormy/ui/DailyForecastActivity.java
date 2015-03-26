package me.jordancarlson.stormy.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jordancarlson.stormy.R;
import me.jordancarlson.stormy.adapters.DayAdapter;
import me.jordancarlson.stormy.weather.Day;

public class DailyForecastActivity extends ListActivity {

    private Day[] mDays;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast_activiyt);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);

        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        DayAdapter adapter = new DayAdapter(this, mDays);

        this.setListAdapter(adapter);

        String location = intent.getStringExtra(MainActivity.LOCATION_NAME);

        mLocationLabel.setText(location);

    }
}
