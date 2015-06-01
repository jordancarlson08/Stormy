package me.jordancarlson.stormy.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jordancarlson.stormy.R;
import me.jordancarlson.stormy.adapters.DayAdapter;
import me.jordancarlson.stormy.fragments.CurrentForecastFragment;
import me.jordancarlson.stormy.utils.Constants;
import me.jordancarlson.stormy.weather.Day;

public class DailyForecastActivity extends Activity {

    private Day[] mDays;
    @InjectView(android.R.id.list) ListView mListView;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;
    @InjectView(android.R.id.empty) TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast_activiyt);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(Constants.DAILY_FORECAST);

        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        DayAdapter adapter = new DayAdapter(this, mDays);

        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmptyTextView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dayOfTheWeek = mDays[position].getDayOfTheWeek();
                String conditions = mDays[position].getSummary();
                String maxTemp = mDays[position].getMaxTemp()+"";

                String message = String.format("On %s the high will be %s and it will be %s",
                        dayOfTheWeek,
                        maxTemp,
                        conditions);

                Toast.makeText(DailyForecastActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

        String location = intent.getStringExtra(Constants.LOCATION_NAME);

        mLocationLabel.setText(location);

    }
}
