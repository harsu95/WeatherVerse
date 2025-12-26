package com.weather.app.ui.alerts;

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

public class AlertsAdapter extends ListAdapter<WeatherAlert, AlertsAdapter.ViewHolder> {

    public AlertsAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<WeatherAlert> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<WeatherAlert>() {
                @Override
                public boolean areItemsTheSame(@NonNull WeatherAlert oldItem, @NonNull WeatherAlert newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull WeatherAlert oldItem, @NonNull WeatherAlert newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle());
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alert, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final View alertIndicator;
        private final TextView tvTitle;
        private final TextView tvDescription;
        private final TextView tvValidUntil;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            alertIndicator = itemView.findViewById(R.id.alert_indicator);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvValidUntil = itemView.findViewById(R.id.tv_valid_until);
        }

        void bind(WeatherAlert alert) {
            tvTitle.setText(alert.getTitle());
            tvDescription.setText(alert.getDescription());
            tvValidUntil.setText(alert.getValidUntil());
            
            // Set indicator color based on severity
            int color;
            switch (alert.getSeverity()) {
                case "warning":
                    color = itemView.getContext().getColor(R.color.uv_color);
                    break;
                case "caution":
                    color = itemView.getContext().getColor(R.color.sunny);
                    break;
                default:
                    color = itemView.getContext().getColor(R.color.primary);
                    break;
            }
            alertIndicator.setBackgroundColor(color);
        }
    }
}
