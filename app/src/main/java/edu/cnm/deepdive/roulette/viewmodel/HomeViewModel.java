package edu.cnm.deepdive.roulette.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.roulette.R;
import java.security.SecureRandom;
import java.util.Random;

public class HomeViewModel extends AndroidViewModel {

  public static final int POCKETS_ON_WHEEL = 38;
  private final MutableLiveData<String> rouletteValue;
  private final MutableLiveData<Integer> pocketIndex;
  private final Random rng;
  private final String[] pocketValues;

  public HomeViewModel(@NonNull Application application) {
    super(application);
    pocketValues = application.getResources().getStringArray(R.array.pocket_values);
    rouletteValue = new MutableLiveData<>("00");
    pocketIndex = new MutableLiveData<>();
    rng = new SecureRandom();
  }

  public LiveData<String> getRouletteValue() {
    return rouletteValue;
  }

  public MutableLiveData<Integer> getPocketIndex() {
    return pocketIndex;
  }

  public void spinWheel() {
    int selection = rng.nextInt(POCKETS_ON_WHEEL);
    pocketIndex.setValue(selection);
    rouletteValue.setValue(pocketValues[selection]);
  }
}