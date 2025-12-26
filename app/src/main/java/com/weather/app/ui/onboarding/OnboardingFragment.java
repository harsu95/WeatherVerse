package com.weather.app.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.weather.app.R;
import com.weather.app.databinding.FragmentOnboardingBinding;
import com.weather.app.opengl.WeatherGlobeView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OnboardingFragment extends Fragment {

    private FragmentOnboardingBinding binding;
    private WeatherGlobeView globeView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                             @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Add 3D globe
        setup3DGlobe();
        
        // Get Started button click
        binding.btnGetStarted.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_onboarding_to_dashboard);
        });
        
        // Login text click
        binding.tvLogin.setOnClickListener(v -> {
            // TODO: Navigate to login screen
        });
    }

    private void setup3DGlobe() {
        // Create OpenGL globe view
        globeView = new WeatherGlobeView(requireContext());
        globeView.setWeatherCondition("Clear");
        globeView.setTemperature(22f);
        globeView.setCloudCoverage(30f);
        
        // Add to container
        FrameLayout container = binding.glContainer;
        container.addView(globeView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (globeView != null) {
            globeView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (globeView != null) {
            globeView.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
