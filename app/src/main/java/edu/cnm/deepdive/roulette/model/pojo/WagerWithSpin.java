package edu.cnm.deepdive.roulette.model.pojo;

import androidx.annotation.NonNull;
import androidx.room.Relation;
import edu.cnm.deepdive.roulette.model.entity.Spin;
import edu.cnm.deepdive.roulette.model.entity.Wager;

public class WagerWithSpin extends Wager {

  @Relation(
      entity = Spin.class,
      parentColumn = "spin_id",
      entityColumn = "spin_id"
  )
  private Spin spin;

  @NonNull
  public Spin getSpin() {
    return spin;
  }

  public void setSpin(@NonNull Spin spin) {
    this.spin = spin;
  }
}
