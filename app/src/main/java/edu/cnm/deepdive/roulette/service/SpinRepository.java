package edu.cnm.deepdive.roulette.service;

import android.content.Context;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.roulette.model.dao.SpinDao;
import edu.cnm.deepdive.roulette.model.dao.WagerDao;
import edu.cnm.deepdive.roulette.model.entity.Spin;
import edu.cnm.deepdive.roulette.model.entity.Wager;
import edu.cnm.deepdive.roulette.model.pojo.SpinWithWagers;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.Iterator;
import java.util.List;

public class SpinRepository {

  private final Context context;
  private final SpinDao spinDao;
  private final WagerDao wagerDao;

  public SpinRepository(Context context) {
    this.context = context;
    RouletteDatabase database = RouletteDatabase.getInstance();
    spinDao = database.getSpinDao();
    wagerDao = database.getWagerDao();
  }

  public Single<SpinWithWagers> save(SpinWithWagers spin) {
    if (spin.getId() > 0) {
      // Update
      return spinDao
          .update(spin)
          .map((ignored) -> spin);
    } else {
      // Insert
      return spinDao
          .insert(spin)
          .flatMap((spinId) -> {
            for (Wager wager : spin.getWagers()) {
              wager.setSpinId(spinId);
            }
            return wagerDao.insert(spin.getWagers());
          })
          .map((wagerIds) -> {
            Iterator<Long> idIterator = wagerIds.iterator();
            Iterator<Wager> wagerIterator = spin.getWagers().iterator();
            while (idIterator.hasNext() && wagerIterator.hasNext()) {
              wagerIterator.next().setId(idIterator.next());
            }
            return spin;
          });
    }
  }

  public Completable delete(Spin spin) {
    return (spin.getId() == 0)
        ? Completable.complete() // No-op
        : spinDao
            .delete(spin)
            .ignoreElement(); // Delete
  }

  public LiveData<List<Spin>> getAll() {
    return spinDao.selectAll();
  }

  public LiveData<SpinWithWagers> get(long spinId) {
    return spinDao.selectById(spinId);
  }
}