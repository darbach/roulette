package edu.cnm.deepdive.roulette.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.databinding.FragmentStatisticsBinding;
import edu.cnm.deepdive.roulette.model.view.ValueCount;
import edu.cnm.deepdive.roulette.viewmodel.StatisticsViewModel;

public class StatisticsFragment extends Fragment {

  private StatisticsViewModel statisticsViewModel;
  private FragmentStatisticsBinding binding;



  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentStatisticsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    statisticsViewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);
    statisticsViewModel.getCounts().observe(getViewLifecycleOwner(), (counts) -> {
      ArrayAdapter<ValueCount> adapter =
          new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, counts);
      binding.countsList.setAdapter(adapter);
    });
  }
}