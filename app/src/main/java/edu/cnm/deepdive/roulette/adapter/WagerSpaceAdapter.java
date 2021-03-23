package edu.cnm.deepdive.roulette.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.adapter.WagerSpaceAdapter.Holder;
import edu.cnm.deepdive.roulette.databinding.ItemWagerSpaceBinding;
import edu.cnm.deepdive.roulette.service.PreferenceRepository;
import java.util.HashMap;
import java.util.Map;

public class WagerSpaceAdapter extends RecyclerView.Adapter<Holder>{

  private final Context context;
  private final OnClickListener onClickListener;
  private final OnLongClickListener onLongClickListener;
  private final int[] spaceColors;
  private final String[] spaceValues;
  private final Map<String, Integer> wagers;
  private int maxWager = 100;

  public WagerSpaceAdapter(
      Context context, OnClickListener onClickListener, OnLongClickListener onLongClickListener) {
    this.context = context;
    this.onClickListener = onClickListener;
    this.onLongClickListener = onLongClickListener;
    Resources res = context.getResources();
    spaceColors = res.getIntArray(R.array.space_colors);
    spaceValues = res.getStringArray(R.array.space_values);
    wagers = new HashMap<>();
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemWagerSpaceBinding binding =
        ItemWagerSpaceBinding.inflate(LayoutInflater.from(context), parent, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return spaceValues.length;
  }

  public Map<String, Integer> getWagers() {
    return wagers;
  }

  public int getMaxWager() {
    return maxWager;
  }

  public void setMaxWager(int maxWager) {
    this.maxWager = maxWager;
  }

  class Holder extends RecyclerView.ViewHolder {

    private final ItemWagerSpaceBinding binding;

    private Holder(@NonNull ItemWagerSpaceBinding binding) {
      super(binding.getRoot()); //sets super.itemView field
      this.binding = binding;
    }

    private void bind(int position) {
      itemView.setBackgroundColor(spaceColors[position]);//itemView field from super constructor
      binding.value.setText(spaceValues[position]);
      binding.wager.setMax(maxWager);
      //noinspection ConstantConditions
      binding.wager.setProgress(wagers.getOrDefault(spaceValues[position], 0));
      itemView.setOnClickListener((v) ->
          onClickListener.onClick(v, position, spaceValues[position]));
      itemView.setOnLongClickListener((v) -> {
        onLongClickListener.onLongClick(v, position, spaceValues[position]);
        return true;
      });
    }

  }

  @FunctionalInterface
  public interface OnClickListener {

    void onClick(View view, int position, String value);

  }

  @FunctionalInterface
  public interface OnLongClickListener {

    void onLongClick(View view, int position, String value);

  }

}
