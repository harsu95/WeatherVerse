package com.weather.app.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.app.R;
import com.weather.app.data.model.LocationResult;

public class LocationResultAdapter extends ListAdapter<LocationResult, LocationResultAdapter.ViewHolder> {

    private final OnLocationClickListener listener;

    public interface OnLocationClickListener {
        void onLocationClick(LocationResult location);
    }

    public LocationResultAdapter(OnLocationClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<LocationResult> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<LocationResult>() {
                @Override
                public boolean areItemsTheSame(@NonNull LocationResult oldItem, @NonNull LocationResult newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull LocationResult oldItem, @NonNull LocationResult newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationResult item = getItem(position);
        holder.bind(item, listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvSubtitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
        }

        void bind(LocationResult item, OnLocationClickListener listener) {
            tvName.setText(item.getName());
            
            StringBuilder subtitle = new StringBuilder();
            if (item.getState() != null && !item.getState().isEmpty()) {
                subtitle.append(item.getState());
            }
            if (item.getCountry() != null && !item.getCountry().isEmpty()) {
                if (subtitle.length() > 0) subtitle.append(", ");
                subtitle.append(item.getCountry());
            }
            tvSubtitle.setText(subtitle.toString());

            itemView.setOnClickListener(v -> listener.onLocationClick(item));
        }
    }
}
