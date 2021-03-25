package edu.cnm.deepdive.roulette.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.roulette.adapter.WagerSpotAdapter.Holder;
import edu.cnm.deepdive.roulette.databinding.ItemWagerSpaceBinding;
import edu.cnm.deepdive.roulette.model.dto.WagerSpot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WagerSpotAdapter extends RecyclerView.Adapter<Holder> {

  private final Context context;
  private final List<WagerSpot> wagerSpots;
  private final OnClickListener onClickListener;
  private final OnLongClickListener onLongClickListener;
  private final Map<WagerSpot, Integer> wagers;
  private int maxWager = 100;

  public WagerSpotAdapter(
      Context context, List<WagerSpot> wagerSpots,
      OnClickListener onClickListener, OnLongClickListener onLongClickListener) {
    this.context = context;
    this.wagerSpots = wagerSpots;
    this.onClickListener = onClickListener;
    this.onLongClickListener = onLongClickListener;
    Resources res = context.getResources();
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
    return wagerSpots.size();
  }

  public Map<WagerSpot, Integer> getWagers() {
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
      WagerSpot spot = wagerSpots.get(position);
      itemView.setBackgroundColor( //itemView field from super constructor
          ContextCompat.getColor(context, spot.getColorResource()));
      binding.value.setText(spot.getName());
      binding.wager.setMax(maxWager);
      //noinspection ConstantConditions
      binding.wager.setProgress(wagers.getOrDefault(spot, 0));
      itemView.setOnClickListener((v) ->
          onClickListener.onClick(v, position, spot));
      itemView.setOnLongClickListener((v) -> {
        onLongClickListener.onLongClick(v, position, spot);
        return true;
      });
    }

  }

  @FunctionalInterface
  public interface OnClickListener {

    void onClick(View view, int position, WagerSpot spot);

  }

  @FunctionalInterface
  public interface OnLongClickListener {

    void onLongClick(View view, int position, WagerSpot spot);

  }

}
