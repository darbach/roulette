package edu.cnm.deepdive.roulette.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

  private HomeViewModel homeViewModel;

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    homeViewModel =
        new ViewModelProvider(this).get(HomeViewModel.class);
    View root = inflater.inflate(R.layout.fragment_home, container, false);
    final TextView rouletteValue = root.findViewById(R.id.roulette_value);
    homeViewModel.getRouletteValue().observe(getViewLifecycleOwner(), new Observer<String>() {
      @Override
      public void onChanged(@Nullable String s) {
        rouletteValue.setText(s);
      }
    });
    root.findViewById(R.id.spin_wheel).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        homeViewModel.spinWheel();
      }
    });
    return root;
  }
}