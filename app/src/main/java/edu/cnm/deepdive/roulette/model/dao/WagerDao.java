package edu.cnm.deepdive.roulette.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.roulette.model.entity.Wager;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface WagerDao {

  @Insert
  Single<Long> insert(Wager wager);

  @Insert
  Single<List<Long>> insert(Wager... wagers);

  @Insert
  Single<List<Long>> insert(Collection<Wager> wagers);

  @Update
  Single<Integer> update(Wager wager);

  @Update
  Single<Integer> update(Wager... wagers);

  @Update
  Single<Integer> update(Collection<Wager> wagers);

  @Delete
  Single<Integer> delete(Wager wager);

  @Delete
  Single<Integer> delete(Wager... wagers);

  @Delete
  Single<Integer> delete(Collection<Wager> wagers);

  @Query("SELECT * FROM Wager WHERE spin_id = :spinId")
  LiveData<List<Wager>> selectBySpin(long spinId);


}