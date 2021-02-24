package edu.cnm.deepdive.roulette.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.roulette.model.view.ValueCount;
import edu.cnm.deepdive.roulette.service.SpinRepository;
import java.util.List;

public class StatisticsViewModel extends AndroidViewModel {

  private final SpinRepository repository;

  public StatisticsViewModel(Application application) {
    super(application);
    repository = new SpinRepository(application);
  }

  public LiveData<List<ValueCount>> getCounts() {
    return repository.getCounts();
  }

}