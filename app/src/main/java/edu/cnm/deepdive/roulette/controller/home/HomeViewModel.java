package edu.cnm.deepdive.roulette.controller.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.security.SecureRandom;
import java.util.Random;

public class HomeViewModel extends ViewModel {

  private final MutableLiveData<String> rouletteValue;
  private final Random rng;

  public HomeViewModel() {
    rouletteValue = new MutableLiveData<>("00");
    rng = new SecureRandom();
  }

  public LiveData<String> getRouletteValue() {
    return rouletteValue;
  }

  public void spinWheel() {
    int selection = rng.nextInt(38);
    rouletteValue.setValue((selection < 37) ? String.valueOf(selection) : "00");
  }
}