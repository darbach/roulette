package edu.cnm.deepdive.roulette.service;

import android.content.Context;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.roulette.model.dao.SpinDao;
import edu.cnm.deepdive.roulette.model.pojo.SpinWithPayout;
import java.util.List;

public class HistoryRepository {

  private final Context context;
  private final SpinDao spinDao;

  public HistoryRepository(Context context) {
    this.context = context;
    spinDao = RouletteDatabase.getInstance().getSpinDao();
  }

  public LiveData<List<SpinWithPayout>> getAll() {
    return spinDao.selectHistory();
  }

}
