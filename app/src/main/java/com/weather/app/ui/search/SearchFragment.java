package com.weather.app.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.weather.app.R;
import com.weather.app.databinding.FragmentSearchBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private LocationResultAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        setupUI();
        setupRecyclerView();
        observeViewModel();
    }

    private void setupUI() {
        // Back button
        binding.btnBack.setOnClickListener(v -> 
            Navigation.findNavController(v).navigateUp()
        );

        // Search input
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    viewModel.searchLocations(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Clear button
        binding.btnClear.setOnClickListener(v -> {
            binding.etSearch.setText("");
            viewModel.clearResults();
        });
    }

    private void setupRecyclerView() {
        adapter = new LocationResultAdapter(location -> {
            // Save location and navigate back
            viewModel.selectLocation(location);
            Navigation.findNavController(requireView()).navigateUp();
        });
        
        binding.rvResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvResults.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getSearchResults().observe(getViewLifecycleOwner(), results -> {
            adapter.submitList(results);
            binding.tvNoResults.setVisibility(
                results.isEmpty() ? View.VISIBLE : View.GONE
            );
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
