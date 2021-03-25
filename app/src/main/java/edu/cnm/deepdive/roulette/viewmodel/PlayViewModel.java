package edu.cnm.deepdive.roulette.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.model.pojo.SpinWithWagers;
import edu.cnm.deepdive.roulette.service.ConfigurationRepository;
import edu.cnm.deepdive.roulette.service.PreferenceRepository;
import edu.cnm.deepdive.roulette.service.SpinRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PlayViewModel extends AndroidViewModel implements LifecycleObserver {

  public static final int POCKETS_ON_WHEEL = 38;

  private final MutableLiveData<String> rouletteValue;
  private final MutableLiveData<Integer> pocketIndex;
  private final MutableLiveData<Long> currentPot;
  private final MutableLiveData<Map<String, Integer>> wagers;
  private final MutableLiveData<Integer> maxWager;
  private final MutableLiveData<Throwable> throwable;
  private final Random rng;
  private final String[] pocketValues;
  private final SpinRepository spinRepository;
  private final PreferenceRepository preferenceRepository;
  private final ConfigurationRepository configurationRepository;
  private final CompositeDisposable pending;

  public PlayViewModel(@NonNull Application application) {
    super(application);
    pocketValues = application.getResources().getStringArray(R.array.pocket_values);
    rouletteValue = new MutableLiveData<>(pocketValues[0]);
    pocketIndex = new MutableLiveData<>();
    currentPot = new MutableLiveData<>();
    wagers = new MutableLiveData<>(new HashMap<>());
    throwable = new MutableLiveData<>();
    rng = new SecureRandom();
    spinRepository = new SpinRepository(application);
    preferenceRepository = new PreferenceRepository(application);
    configurationRepository = ConfigurationRepository.getInstance();
    maxWager = new MutableLiveData<>(preferenceRepository.getMaximumWager());
    pending = new CompositeDisposable();
    observeMaxWager();
    newGame();
  }

  public LiveData<String> getRouletteValue() {
    return rouletteValue;
  }

  public LiveData<Integer> getPocketIndex() {
    return pocketIndex;
  }

  public LiveData<Long> getCurrentPot() {
    return currentPot;
  }

  public LiveData<Map<String, Integer>> getWagers() {
    return wagers;
  }

  public LiveData<Integer> getMaxWager() {
    return maxWager;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void spinWheel() {
    int selection = rng.nextInt(POCKETS_ON_WHEEL);
    pocketIndex.setValue(selection);
    rouletteValue.setValue(pocketValues[selection]);
    SpinWithWagers spin = new SpinWithWagers();
    spin.setValue(pocketValues[selection]);
    pending.add(
        spinRepository
            .save(spin)
            .subscribe(
                (spinWithWagers) -> {
                },
                this::handleThrowable //method ref has instance or class name left, method right
            )
    );
  }

  public void newGame() {
    currentPot.setValue((long) preferenceRepository.getStartingPot());
    pocketIndex.setValue(0);
    rouletteValue.setValue(pocketValues[0]);
  }

  @SuppressWarnings("ConstantConditions")
  public void incrementWager(String spaceValue) {
    Map<String, Integer> wagers = this.wagers.getValue();
    int currentWager = wagers.getOrDefault(spaceValue, 0);
    if (currentWager < maxWager.getValue()) {
      wagers.put(spaceValue, 1 + wagers.getOrDefault(spaceValue, 0));
      this.wagers.setValue(wagers);
      currentPot.setValue(currentPot.getValue() - 1);
    }
  }

  @SuppressWarnings("ConstantConditions")
  public void clearWager(String spaceValue) {
    Map<String, Integer> wagers = this.wagers.getValue();
    int currentWager = wagers.getOrDefault(spaceValue, 0);
    if (currentWager > 0) {
      wagers.remove(spaceValue);
      this.wagers.setValue(wagers);
      currentPot.setValue(currentWager + currentPot.getValue());
    }
  }

  private void observeMaxWager() {
    //noinspection ResultOfMethodCallIgnored
    preferenceRepository
        .maximumWager()
        .subscribe(this::adjustMaxWager);
  }

  @SuppressWarnings("ConstantConditions")
  private void adjustMaxWager(int maxWager) {
    Map<String, Integer> wagers = this.wagers.getValue();
    int excess = 0;
    for (String key : wagers.keySet()) {
      int wager = wagers.get(key);
      if (wagers.get(key) > maxWager) {
        excess += wager - maxWager;
        wagers.put(key, maxWager);
      }
    }
    if (excess > 0) {
      this.wagers.postValue(wagers);
      currentPot.setValue(currentPot.getValue() + excess);
    }
    this.maxWager.postValue(maxWager);
  }

  private void handleThrowable(Throwable throwable) {
    Log.e(getClass().getName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }
}