package edu.cnm.deepdive.roulette.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.Lifecycle.Event;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.model.pojo.SpinWithWagers;
import edu.cnm.deepdive.roulette.service.SpinRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import java.security.SecureRandom;
import java.util.Random;

public class PlayViewModel extends AndroidViewModel implements LifecycleObserver {

  public static final int POCKETS_ON_WHEEL = 38;

  private final MutableLiveData<String> rouletteValue;
  private final MutableLiveData<Integer> pocketIndex;
  private final MutableLiveData<Throwable> throwable;
  private final Random rng;
  private final String[] pocketValues;
  private final SpinRepository repository;
  private final CompositeDisposable pending;

  public PlayViewModel(@NonNull Application application) {
    super(application);
    pocketValues = application.getResources().getStringArray(R.array.pocket_values);
    rouletteValue = new MutableLiveData<>("00");
    pocketIndex = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    rng = new SecureRandom();
    repository = new SpinRepository(application);
    pending = new CompositeDisposable();
  }

  public LiveData<String> getRouletteValue() {
    return rouletteValue;
  }

  public LiveData<Integer> getPocketIndex() {
    return pocketIndex;
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
        repository
            .save(spin)
            .subscribe(
                (spinWithWagers) -> {
                },
                this::handleThrowable //method ref has instance or class name left, method right
            )
    );
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