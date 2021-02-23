package edu.cnm.deepdive.roulette.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.roulette.model.entity.Spin;
import edu.cnm.deepdive.roulette.model.entity.Wager;
import java.util.LinkedList;
import java.util.List;

public class SpinWithWagers extends Spin {

  @Relation(
      entity = Wager.class,
      parentColumn = "spin_id",
      entityColumn = "spin_id"
  )
  private List<Wager> wagers = new LinkedList<>();

  public List<Wager> getWagers() {
    return wagers;
  }

  public void setWagers(List<Wager> wagers) {
    this.wagers = wagers;
  }
}
