package edu.cnm.deepdive.roulette.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import edu.cnm.deepdive.roulette.model.entity.Spin;
import edu.cnm.deepdive.roulette.model.pojo.SpinWithPayout;
import edu.cnm.deepdive.roulette.model.pojo.SpinWithWagers;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface SpinDao {

  @Insert
  Single<Long> insert(Spin spin);

  @Update
  Single<Integer> update(Spin spin);

  @Delete
  Single<Integer> delete(Spin spin);

  @Delete
  Single<Integer> delete(Spin... spins);

  @Delete
  Single<Integer> delete(Collection<Spin> spins);

  @Query("SELECT * FROM Spin ORDER BY timestamp DESC")
  LiveData<List<Spin>> selectAll();

  @Transaction
  @Query("SELECT * FROM Spin WHERE spin_id = :spinId")
  LiveData<SpinWithWagers> selectById(long spinId);

  @Query("SELECT s.*, t.total_wager, t.total_payout FROM Spin AS s LEFT JOIN (SELECT w.spin_id, SUM(w.amount) as total_wager, SUM(w.payout) as total_payout FROM Wager AS w GROUP BY w.spin_id) AS t ON s.spin_id = t.spin_id ORDER BY s.timestamp DESC")
  LiveData<List<SpinWithPayout>> selectHistory();

}
