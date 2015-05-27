package me.jordancarlson.stormy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.jordancarlson.stormy.R;
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
    private Context mContext;

    private String mCity;

    public DrawerAdapter(Context context, String[] titles, int[] icons, String city) {
        mContext = context;
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

//            String time = mTimeLabel.getText().toString();
//            String temp = mTemperatureLabel.getText().toString();
//            String summary = mSummaryLabel.getText().toString();
//
//            String message = String.format("At %s it will be %s and %s",
//                    time,
//                    temp,
//                    summary);
            Toast.makeText(mContext, "Clicked!", Toast.LENGTH_SHORT).show();
        }
    }

}
