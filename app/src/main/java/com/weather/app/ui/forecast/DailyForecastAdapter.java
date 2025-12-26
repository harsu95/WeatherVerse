package com.weather.app.ui.forecast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.app.R;
import com.weather.app.data.model.DailyForecast;

public class DailyForecastAdapter extends ListAdapter<DailyForecast, DailyForecastAdapter.ViewHolder> {

    public DailyForecastAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<DailyForecast> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<DailyForecast>() {
                @Override
                public boolean areItemsTheSame(@NonNull DailyForecast oldItem, @NonNull DailyForecast newItem) {
                    return oldItem.getDate().equals(newItem.getDate());
                }

                @Override
                public boolean areContentsTheSame(@NonNull DailyForecast oldItem, @NonNull DailyForecast newItem) {
                    return oldItem.equals(newItem);
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), position == 0);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDay;
        private final TextView tvIcon;
        private final TextView tvCondition;
        private final TextView tvHigh;
        private final TextView tvLow;
        private final View tempBar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvIcon = itemView.findViewById(R.id.tv_icon);
            tvCondition = itemView.findViewById(R.id.tv_condition);
            tvHigh = itemView.findViewById(R.id.tv_high);
            tvLow = itemView.findViewById(R.id.tv_low);
            tempBar = itemView.findViewById(R.id.temp_bar);
        }

        void bind(DailyForecast item, boolean isToday) {
            tvDay.setText(isToday ? "Today" : item.getDayName());
            tvIcon.setText(getWeatherEmoji(item.getWeatherCondition()));
            tvCondition.setText(item.getWeatherCondition());
            tvHigh.setText(String.format("%.0f°", item.getTempMax()));
            tvLow.setText(String.format("%.0f°", item.getTempMin()));
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
                case "snowy":
                    return "❄️";
                default:
                    return "☁️";
            }
        }
    }
}
