package edu.cnm.deepdive.roulette.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Encapsulates a single spin of a roulette wheel, including its date & time, and the outcome of the
 * spin.
 */
@Entity
public class Spin {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "spin_id")
  private long id;

  @NonNull
  @ColumnInfo(index = true)
  private Date timestamp = new Date();

  @ColumnInfo(index = true)
  private String value;

  /**
   * Returns the unique identifier of this spin.
   *
   * @return
   */
  public long getId() {
    return id;
  }

  /**
   * Change the unique identifier for this spin. In general, this method is not invoked outside of
   * Room and {@link edu.cnm.deepdive.roulette.service.SpinRepository}.
   *
   * @param id
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Returns the date & time when this spin was made. In general, this method is not invoked outside of
   * Room and {@link edu.cnm.deepdive.roulette.service.SpinRepository}.
   *
   * @return
   */
  @NonNull
  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@NonNull Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
