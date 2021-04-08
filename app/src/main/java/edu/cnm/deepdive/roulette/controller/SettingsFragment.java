package edu.cnm.deepdive.roulette.controller;

import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
import edu.cnm.deepdive.roulette.R;

public class SettingsFragment extends PreferenceFragmentCompat {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.preferences, rootKey);
    forceStartingPotIncrement();
  }

  private void forceStartingPotIncrement() {
    SeekBarPreference startingPot = findPreference(getString(R.string.starting_pot_key));
    int increment = getResources().getInteger(R.integer.starting_pot_increment);
    //noinspection ConstantConditions
    startingPot.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange(Preference preference, Object newValue) {
        int value = (Integer) newValue;
        boolean save = true;
        if (value % increment != 0) {
          value = Math.round((float) value / increment) * increment;
          startingPot.setValue(value);
          save = false;
        }
        return save;
      }
    });
  }
}