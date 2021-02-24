package edu.cnm.deepdive.roulette.model.view;

import androidx.room.DatabaseView;

@DatabaseView("SELECT value, COUNT(*) AS `count` "
    + "FROM Spin "
    + "WHERE value IS NOT NULL "
    + "GROUP BY value "
    + "ORDER BY CASE value WHEN '00' THEN -1 ELSE CAST(value AS INTEGER) END")
public class ValueCount {

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

  private String value;
  private int count;
}
