package edu.cnm.deepdive.roulette.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.databinding.ItemValueCountBinding;
import edu.cnm.deepdive.roulette.model.view.ValueCount;
import java.util.List;

public class ValueCountAdapter extends ArrayAdapter<ValueCount> {

  public ValueCountAdapter(@NonNull Context context, @NonNull List<ValueCount> counts) {
    super(context, R.layout.item_value_count, counts);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    ItemValueCountBinding binding = (convertView != null)
        ? ItemValueCountBinding.bind(convertView)
        : ItemValueCountBinding.inflate(LayoutInflater.from(getContext()), parent, false);
    ValueCount item = getItem(position);
    binding.pocket.setText(item.getValue());
    binding.count.setText(String.valueOf(item.getCount()));
    // TODO Set percent text.
    return binding.getRoot();
  }
}
