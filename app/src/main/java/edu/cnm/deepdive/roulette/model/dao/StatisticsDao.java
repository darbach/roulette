package edu.cnm.deepdive.roulette.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import edu.cnm.deepdive.roulette.model.view.ValueCount;
import java.util.List;

@Dao
public interface StatisticsDao {

  @Query("SELECT * FROM ValueCount")
  LiveData<List<ValueCount>> selectCounts();

  @Query("SELECT COUNT(*) FROM Spin")
  LiveData<Integer> selectTotal();
}
