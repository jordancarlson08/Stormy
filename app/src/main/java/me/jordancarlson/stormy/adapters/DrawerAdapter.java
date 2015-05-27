package me.jordancarlson.stormy.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.jordancarlson.stormy.R;
import me.jordancarlson.stormy.fragments.CurrentForecastFragment;
import me.jordancarlson.stormy.fragments.HourlyForecastFragment;
import me.jordancarlson.stormy.weather.Hour;

/**
 * Created by jcarlson on 3/26/15.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;
    private String[] mTitles;
    private int[] mIcons;
    private FragmentActivity mContext;
    private Activity mActivity;

    private String mCity;

    public DrawerAdapter(Activity activity, String[] titles, int[] icons, String city) {
        mContext = (FragmentActivity) activity;
        mActivity = activity;
        mTitles = titles;
        mIcons = icons;
        mCity = city;
    }

    @Override
    public DrawerAdapter.DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.drawer_list_item, parent, false);
            DrawerViewHolder viewHolderItem = new DrawerViewHolder(view, viewType);
            return viewHolderItem;
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header, parent, false);
            DrawerViewHolder viewHolderHeader = new DrawerViewHolder(view, viewType);
            return viewHolderHeader;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(DrawerAdapter.DrawerViewHolder holder, int position) {
        if (holder.mHolderId == TYPE_ITEM) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.mTitleTextView.setText(mTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.mIconImageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons
        }
        else{
            holder.mCityTextView.setText(mCity);
        }

    }

    @Override
    public int getItemCount() {
        return mTitles.length+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public class DrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public int mHolderId;

        public TextView mCityTextView;
        public TextView mTitleTextView;
        public ImageView mIconImageView;


        public DrawerViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == TYPE_ITEM) {
                mTitleTextView = (TextView) itemView.findViewById(R.id.rowText);
                mIconImageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                itemView.setOnClickListener(this);
                mHolderId = TYPE_ITEM;
            } else {
                mCityTextView = (TextView) itemView.findViewById(R.id.cityTextView);
                mHolderId = TYPE_HEADER;
            }


        }

        @Override
        public void onClick(View v) {
            TextView rowText = (TextView) v.findViewById(R.id.rowText);

            switch (rowText.getText().toString()) {
                case "Current":
                    CurrentForecastFragment currentForecastFragment = CurrentForecastFragment.newInstance();

                    mContext.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contentFragment, currentForecastFragment)
                            .commit();

                    break;

                case "Hourly":
//                    HourlyForecastFragment hourlyForecastFragment = HourlyForecastFragment.newInstance(mForecast.getHourForecast());
//
//                    mContext.getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.contentFragment, hourlyForecastFragment)
//                            .commit();
                    break;

                case "Weekly":
                    break;

                case "Settings":
                    break;

                default:
                    CurrentForecastFragment defaultFragment = CurrentForecastFragment.newInstance();

                    mContext.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contentFragment, defaultFragment)
                            .commit();
                    break;
            }
            DrawerLayout drawerLayout = (DrawerLayout) mActivity.findViewById(R.id.DrawerLayout);
            drawerLayout.closeDrawers();
        }
    }

}
