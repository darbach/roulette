package edu.cnm.deepdive.roulette.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.roulette.model.pojo.SpinWithPayout;
import edu.cnm.deepdive.roulette.service.HistoryRepository;
import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

  private final HistoryRepository historyRepository;

  public HistoryViewModel(@NonNull Application application) {
    super(application);
    historyRepository = new HistoryRepository(application);
  }

  public LiveData<List<SpinWithPayout>> getHistory() {
    return historyRepository.getAll();
  }

}