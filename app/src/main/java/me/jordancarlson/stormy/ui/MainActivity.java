package me.jordancarlson.stormy.ui;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.jordancarlson.stormy.R;
import me.jordancarlson.stormy.adapters.DrawerAdapter;
import me.jordancarlson.stormy.fragments.CurrentForecastFragment;
import me.jordancarlson.stormy.fragments.HourlyForecastFragment;
import me.jordancarlson.stormy.utils.ToolbarUtil;



public class MainActivity extends ActionBarActivity implements
        CurrentForecastFragment.OnFragmentInteractionListener,
        HourlyForecastFragment.OnFragmentInteractionListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private String mTitles[] = {"Current", "Hourly", "Weekly", "Settings"};
    private int mIcons[] = {R.drawable.ic_home, R.drawable.ic_clock, R.drawable.ic_calendar ,R.drawable.ic_settings};

    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @InjectView(R.id.DrawerLayout) DrawerLayout Drawer;
    @InjectView(R.id.RecyclerView) RecyclerView mRecyclerView;
    @InjectView(R.id.toolbar) Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        ToolbarUtil.setupToolbar(this);

        String city = "Provo, UT";

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new DrawerAdapter(this, mTitles, mIcons, city);
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

        CurrentForecastFragment fragment = CurrentForecastFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentFragment, fragment)
                .addToBackStack(null)
                .commit();


        //todo: get swipe to refresh working
//        // Swipe to refresh
//        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeView.setRefreshing(true);
//                (new Handler()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        getForecast(mLat, mLng);
////                        updateLocationLabel(mLocation);
//                        swipeView.setRefreshing(false);
//                    }
//                }, 1);
//            }
//        });


        Log.d(TAG, "On Create ");

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
