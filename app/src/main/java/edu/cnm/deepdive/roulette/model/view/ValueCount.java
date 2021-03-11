package edu.cnm.deepdive.roulette.model.view;

import androidx.room.DatabaseView;

@DatabaseView("SELECT value, COUNT(*) AS `count`, (100.0 * COUNT(*) / (SELECT COUNT(*) FROM spin WHERE value IS NOT NULL)) AS `percent` "
    + "FROM Spin "
    + "WHERE value IS NOT NULL "
    + "GROUP BY value "
    + "ORDER BY CASE value WHEN '00' THEN -1 ELSE CAST(value AS INTEGER) END")
public class ValueCount {

  private String value;

  private int count;

  private float percent;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public float getPercent() {
    return percent;
  }

  public void setPercent(float percent) {
    this.percent = percent;
  }
}
