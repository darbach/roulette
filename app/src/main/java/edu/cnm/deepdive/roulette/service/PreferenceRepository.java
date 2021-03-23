package edu.cnm.deepdive.roulette.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import androidx.preference.PreferenceManager;
import edu.cnm.deepdive.roulette.R;
import io.reactivex.Observable;

public class PreferenceRepository {

  private final Context context;
  private final SharedPreferences preferences;
  private final Resources resources;

  public PreferenceRepository(Context context) {
    this.context = context;
    preferences = PreferenceManager.getDefaultSharedPreferences(context);
    resources = context.getResources();
  }

  public int getMaximumWager() {
    return preferences.getInt(resources.getString(R.string.maximum_wager_key),
        resources.getInteger(R.integer.maximum_wager_default));
  }

  public int getStartingPot() {
    return preferences.getInt(resources.getString(R.string.starting_pot_key),
        resources.getInteger(R.integer.starting_pot_default));
  }

  public boolean isWagerRiding() {
    return preferences.getBoolean(resources.getString(R.string.wager_riding_key),
        resources.getBoolean(R.bool.wager_riding_default));
  }

  public Observable<Integer> maximumWager() {
    return Observable.create((emitter) -> {
      OnSharedPreferenceChangeListener listener = (prefs, key) -> {
        if (key.equals(resources.getString(R.string.maximum_wager_key))) {
          emitter.onNext(prefs.getInt(key, resources.getInteger(R.string.maximum_wager_key)));
        }
      };
      preferences.registerOnSharedPreferenceChangeListener(listener);
    });
  }

}
