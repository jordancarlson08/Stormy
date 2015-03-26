package me.jordancarlson.stormy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.jordancarlson.stormy.R;
import me.jordancarlson.stormy.weather.Hour;

/**
 * Created by jcarlson on 3/26/15.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private Hour[] mHours;

    public HourAdapter(Hour[] hours) {
        mHours = hours;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_item, parent, false);

        return new HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int posiiton) {
        holder.bindHour(mHours[posiiton]);

    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {

        public TextView mTimeLabel;
        public TextView mSummaryLabel;
        public ImageView mIconImageView;
        public TextView mTemperatureLabel;

        public HourViewHolder(View itemView) {
            super(itemView);

            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
            mTemperatureLabel = (TextView) itemView.findViewById(R.id.temperatureLabel);
            mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
        }

        public void bindHour(Hour hour) {
            mTimeLabel.setText(hour.getHour());
            mSummaryLabel.setText(hour.getSummary());
            mTemperatureLabel.setText(hour.getTemperature()+"");
            mIconImageView.setImageResource(hour.getIconId());
        }
    }

}
