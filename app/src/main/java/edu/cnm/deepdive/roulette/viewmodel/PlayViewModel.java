package edu.cnm.deepdive.roulette.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import edu.cnm.deepdive.roulette.model.dto.ColorDto;
import edu.cnm.deepdive.roulette.model.dto.PocketDto;
import edu.cnm.deepdive.roulette.model.dto.WagerSpot;
import edu.cnm.deepdive.roulette.model.entity.Wager;
import edu.cnm.deepdive.roulette.model.pojo.SpinWithWagers;
import edu.cnm.deepdive.roulette.service.ConfigurationRepository;
import edu.cnm.deepdive.roulette.service.PreferenceRepository;
import edu.cnm.deepdive.roulette.service.SpinRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayViewModel extends AndroidViewModel implements LifecycleObserver {

  private final MutableLiveData<WagerSpot> rouletteValue;
  private final MutableLiveData<Integer> pocketIndex;
  private final MutableLiveData<Long> currentPot;
  private final MutableLiveData<Map<WagerSpot, Integer>> wagers;
  private final MutableLiveData<List<WagerSpot>> wagerSpots;
  private final MutableLiveData<List<PocketDto>> pockets;
  private final MutableLiveData<Integer> maxWager;
  private final MutableLiveData<Throwable> throwable;
  private final Random rng;
  private final SpinRepository spinRepository;
  private final PreferenceRepository preferenceRepository;
  private final ConfigurationRepository configurationRepository;
  private final CompositeDisposable pending;

  private Map<WagerSpot, Integer> pendingWagers; //whatever is going back on the table
  private long pendingCurrentPot; //whatever is going back in the pot

  public PlayViewModel(@NonNull Application application) {
    super(application);
    spinRepository = new SpinRepository(application);
    preferenceRepository = new PreferenceRepository(application);
    configurationRepository = ConfigurationRepository.getInstance();
    pockets = new MutableLiveData<>(configurationRepository.getPockets());
    rouletteValue = new MutableLiveData<>();
    pocketIndex = new MutableLiveData<>();
    currentPot = new MutableLiveData<>();
    wagers = new MutableLiveData<>(new HashMap<>());
    wagerSpots = new MutableLiveData<>(ConfigurationRepository.getInstance().getWagerSpots());
    throwable = new MutableLiveData<>();
    rng = new SecureRandom();
    maxWager = new MutableLiveData<>(preferenceRepository.getMaximumWager());
    pending = new CompositeDisposable();
    observeMaxWager();
    newGame();
  }

  public LiveData<WagerSpot> getRouletteValue() {
    return rouletteValue;
  }

  public LiveData<Integer> getPocketIndex() {
    return pocketIndex;
  }

  public LiveData<Long> getCurrentPot() {
    return currentPot;
  }

  public LiveData<Map<WagerSpot, Integer>> getWagers() {
    return wagers;
  }

  public LiveData<List<WagerSpot>> getWagerSpots() {
    return wagerSpots;
  }

  public LiveData<List<PocketDto>> getPockets() {
    return pockets;
  }

  public LiveData<Integer> getMaxWager() {
    return maxWager;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void spinWheel() {
    SpinWithWagers spin = generateOutcome();
    pending.add( //store outcome in database
        spinRepository
            .save(spin)
            .subscribe(
                this::update, //success
                this::handleThrowable //method ref has instance or class name left, method right
            )
    );
  }

  @SuppressWarnings("ConstantConditions")
  private SpinWithWagers generateOutcome() {
    List<PocketDto> pockets = this.pockets.getValue();
    int selection = rng.nextInt(pockets.size());
    PocketDto pocket = pockets.get(selection); //result of the spin
    ColorDto color = pocket.getColorDto();
    Map<WagerSpot, Integer> wagers = this.wagers.getValue();
    SpinWithWagers spin = new SpinWithWagers();
    spin.setValue(pocket.getName()); //set display name of result in UI
    pendingCurrentPot = currentPot.getValue();
    pendingWagers = new HashMap<>();
    boolean letItRide = preferenceRepository.isWagerRiding();
    int maxWager = this.maxWager.getValue();
    for (Map.Entry<WagerSpot, Integer> wagerEntry : wagers.entrySet()) {
      WagerSpot spot = wagerEntry.getKey();
      int amount = wagerEntry.getValue();
      if (amount > 0) {
        int payout = 0;
        Wager wager = new Wager();
        wager.setAmount(amount);
        if (spot instanceof PocketDto) {
          wager.setValue(spot.getName());
        } else {
          wager.setColor(((ColorDto) spot).getColor());
        }
        if (spot.equals(pocket) || spot.equals(color)) {
          payout = amount * spot.getPayout();
        }
        wager.setPayout(payout);
        spin.getWagers().add(wager);
        if (letItRide) { //update scratch paper calculations
          if (payout > maxWager) {
            int difference = payout - maxWager;
            pendingCurrentPot += difference;
            pendingWagers.put(spot, maxWager);
          } else {
            pendingWagers.put(spot, payout);
          }
        } else {
          pendingCurrentPot += payout;
        }
      }
    }
    pocketIndex.setValue(selection);
    rouletteValue.setValue(pocket);
    return spin;
  }

  @SuppressWarnings("ConstantConditions")
  public void newGame() {
    currentPot.setValue((long) preferenceRepository.getStartingPot());//reset pot
    pocketIndex.setValue(0); //reset wheel
    rouletteValue.setValue(pockets.getValue().get(0));
    Map<WagerSpot, Integer> wagers = this.wagers.getValue();//reset wagers
    wagers.clear();
    this.wagers.setValue(wagers);
  }

  @SuppressWarnings("ConstantConditions")
  public void incrementWager(WagerSpot spot) {
    Map<WagerSpot, Integer> wagers = this.wagers.getValue();
    int currentWager = wagers.getOrDefault(spot, 0);
    if (currentWager < maxWager.getValue()) {
      wagers.put(spot, 1 + wagers.getOrDefault(spot, 0));
      this.wagers.setValue(wagers);
      currentPot.setValue(currentPot.getValue() - 1);
    }
  }

  @SuppressWarnings("ConstantConditions")
  public void clearWager(WagerSpot spot) {
    Map<WagerSpot, Integer> wagers = this.wagers.getValue();
    int currentWager = wagers.getOrDefault(spot, 0);
    if (currentWager > 0) {
      wagers.remove(spot);
      this.wagers.setValue(wagers);
      currentPot.setValue(currentWager + currentPot.getValue());
    }
  }

  @SuppressWarnings("ConstantConditions")
  private void update(SpinWithWagers spin) {
    this.currentPot.postValue(pendingCurrentPot);
    this.wagers.postValue(pendingWagers);
  }

  @SuppressLint("CheckResult")
  private void observeMaxWager() {
    //noinspection ResultOfMethodCallIgnored
    preferenceRepository
        .maximumWager()
        .subscribe(this::adjustMaxWager);
  }

  @SuppressWarnings("ConstantConditions")
  private void adjustMaxWager(int maxWager) {
    Map<WagerSpot, Integer> wagers = this.wagers.getValue();
    int excess = 0;
    for (WagerSpot spot : wagers.keySet()) {
      int wager = wagers.get(spot);
      if (wagers.get(spot) > maxWager) {
        excess += wager - maxWager;
        wagers.put(spot, maxWager);
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