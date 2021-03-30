package edu.cnm.deepdive.roulette.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.adapter.HistoryAdapter.Holder;
import edu.cnm.deepdive.roulette.databinding.ItemHistoryBinding;
import edu.cnm.deepdive.roulette.model.pojo.SpinWithPayout;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<Holder> {

  private final List<SpinWithPayout> spins;
  private final LayoutInflater inflater;
  private final DateFormat dateFormatter;
  private final DateFormat timeFormatter;
  private final String positiveFormat;
  private final String differenceFormat;
  private final int netPositive;
  private final int netNegative;
  private final int netZero;

  public HistoryAdapter(@NonNull Context context, @NonNull List<SpinWithPayout> spins) {
    this.spins = spins;
    inflater = LayoutInflater.from(context);
    dateFormatter = android.text.format.DateFormat.getDateFormat(context);
    timeFormatter = android.text.format.DateFormat.getTimeFormat(context);
    positiveFormat = context.getString(R.string.positive_format);
    differenceFormat = context.getString(R.string.difference_format);
    netPositive = ContextCompat.getColor(context, R.color.netPositive);
    netNegative = ContextCompat.getColor(context, R.color.netNegative);
    netZero = ContextCompat.getColor(context, R.color.netZero);
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(ItemHistoryBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return spins.size();
  }

  class Holder extends RecyclerView.ViewHolder {

    private static final String DATE_TIME_COMBINATION_FORMAT = "%1$s %2$s";
    private final ItemHistoryBinding binding;

    private Holder(@NonNull ItemHistoryBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      SpinWithPayout spin = spins.get(position);
      Date timestamp = spin.getTimestamp();
      int totalWager = spin.getTotalWager();
      int totalPayout = spin.getTotalPayout();
      int netPayout = totalPayout - totalWager;
      binding.timestamp.setText(String.format(DATE_TIME_COMBINATION_FORMAT,
          dateFormatter.format(timestamp), timeFormatter.format(timestamp)));
      binding.value.setText(spin.getValue());
      binding.totalWager.setText(String.format(positiveFormat, totalWager));
      binding.totalPayout.setText(String.format(positiveFormat, totalPayout));
      binding.netPayout.setText(String.format(differenceFormat, netPayout));
      if (netPayout > 0) {
        itemView.setBackgroundColor(netPositive);
      } else if (netPayout < 0) {
        itemView.setBackgroundColor(netNegative);
      } else { //(netPayout == 0)
        itemView.setBackgroundColor(netZero);
      }
    }

  }

}
