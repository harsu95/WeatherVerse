package com.weather.app.ui.dashboard;

import android.widget.ImageView;
import android.widget.TextView;

import com.weather.app.R;
import com.weather.app.databinding.FragmentDashboardBinding;
import com.weather.app.databinding.ItemMetricCardBinding;

/**
 * Helper class to bind metric card views in the dashboard.
 * Used because include tags create separate binding objects.
 */
public class MetricCardBinder {

    public static void bindHumidity(FragmentDashboardBinding binding, int humidity, String dewPoint) {
        // The card_humidity is included, we need to access it properly
        // Note: In actual implementation, you would access these through the binding
        // This is a helper to show the pattern
    }

    public static void bindPressure(FragmentDashboardBinding binding, double pressure, String trend) {
        // Bind pressure values
    }

    public static void bindWind(FragmentDashboardBinding binding, double speed, String direction) {
        // Bind wind values
    }

    public static void bindUvIndex(FragmentDashboardBinding binding, String level) {
        // Bind UV index values
    }
}
