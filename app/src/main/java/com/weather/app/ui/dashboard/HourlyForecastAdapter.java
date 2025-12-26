package com.weather.app.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.app.R;
import com.weather.app.data.model.HourlyForecast;

public class HourlyForecastAdapter extends ListAdapter<HourlyForecast, HourlyForecastAdapter.ViewHolder> {

    public HourlyForecastAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<HourlyForecast> DIFF_CALLBACK = 
        new DiffUtil.ItemCallback<HourlyForecast>() {
            @Override
            public boolean areItemsTheSame(@NonNull HourlyForecast oldItem, @NonNull HourlyForecast newItem) {
                return oldItem.getTime().equals(newItem.getTime());
            }

            @Override
            public boolean areContentsTheSame(@NonNull HourlyForecast oldItem, @NonNull HourlyForecast newItem) {
                return oldItem.equals(newItem);
            }
        };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyForecast item = getItem(position);
        holder.bind(item, position == 0);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTime;
        private final TextView tvIcon;
        private final TextView tvTemp;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvIcon = itemView.findViewById(R.id.tv_icon);
            tvTemp = itemView.findViewById(R.id.tv_temp);
        }

        void bind(HourlyForecast item, boolean isNow) {
            tvTime.setText(isNow ? "Now" : item.getTime());
            tvIcon.setText(getWeatherEmoji(item.getWeatherCondition()));
            tvTemp.setText(String.format("%.0f°", item.getTemperature()));
            
            // Highlight current hour
            if (isNow) {
                itemView.setBackgroundResource(R.drawable.bg_hourly_current);
            } else {
                itemView.setBackgroundResource(R.drawable.bg_glass_panel);
            }
        }

        private String getWeatherEmoji(String condition) {
            if (condition == null) return "☁️";
            switch (condition.toLowerCase()) {
                case "clear":
                case "sunny":
                    return "☀️";
                case "partly cloudy":
                    return "⛅";
                case "cloudy":
                case "overcast":
                    return "☁️";
                case "rainy":
                    return "🌧️";
                case "stormy":
                    return "⛈️";
                default:
                    return "☁️";
            }
        }
    }
}
