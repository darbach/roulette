package edu.cnm.deepdive.roulette.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup;
import androidx.recyclerview.widget.LinearLayoutManager;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.adapter.WagerSpaceAdapter;
import edu.cnm.deepdive.roulette.databinding.FragmentWagerBinding;
import edu.cnm.deepdive.roulette.viewmodel.PlayViewModel;
import java.util.Map;

public class WagerFragment extends Fragment {

  public static final int FULL_WIDTH = 6;
  public static final int ZERO_SPACE_WIDTH = 3;
  public static final int NORMAL_SPACE_WIDTH = 2;
  private FragmentWagerBinding binding;
  private PlayViewModel viewModel;
  private WagerSpaceAdapter adapter;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentWagerBinding.inflate(inflater, container, false);
    GridLayoutManager layoutManager = //create a layout manager
        new GridLayoutManager(getContext(), FULL_WIDTH, LinearLayoutManager.VERTICAL, false);
    layoutManager.setSpanSizeLookup(new WagerSpanLookup());//attach SpanSizeLookup to layoutmanager
    binding.wagerSpaces.setLayoutManager(layoutManager);//attach to RecyclerView
    adapter = new WagerSpaceAdapter(
        getContext(),
        (view, position, value) -> viewModel.incrementWager(value),
        (view, position, value) -> { //long press
          PopupMenu menu = new PopupMenu(getContext(), view);
          MenuInflater menuInflater = menu.getMenuInflater();
          menuInflater.inflate(R.menu.wager_actions, menu.getMenu());
          menu.show();
        }
    );
    binding.wagerSpaces.setAdapter(//set WagerAdapter for RecyclerView
        adapter);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(getActivity()).get(PlayViewModel.class);//attach to viewModel
    viewModel.getWagers().observe(getViewLifecycleOwner(), (wagers) -> {
      Map<String, Integer> oldWagers = adapter.getWagers();
      oldWagers.clear();
      oldWagers.putAll(wagers);
      adapter.notifyDataSetChanged();
    });
    viewModel.getMaxWager().observe(getViewLifecycleOwner(),(maxWager) -> {
      adapter.setMaxWager(maxWager);
      adapter.notifyDataSetChanged();
    });
    // TODO Observe viewModel as appropriate.
  }

  private static class WagerSpanLookup extends SpanSizeLookup {

    @Override
    public int getSpanSize(int position) {
      return (position <= 1) ? ZERO_SPACE_WIDTH : NORMAL_SPACE_WIDTH;
    }
  }

}