package edu.cnm.deepdive.roulette.model.pojo;

import androidx.room.ColumnInfo;
import edu.cnm.deepdive.roulette.model.entity.Spin;

public class SpinWithPayout extends Spin {

  @ColumnInfo(name = "total_wager")
  private int totalWager;

  @ColumnInfo(name = "total_payout")
  private int totalPayout;

  public int getTotalWager() {
    return totalWager;
  }

  public void setTotalWager(int totalWager) {
    this.totalWager = totalWager;
  }

  public int getTotalPayout() {
    return totalPayout;
  }

  public void setTotalPayout(int totalPayout) {
    this.totalPayout = totalPayout;
  }

}
