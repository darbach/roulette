package edu.cnm.deepdive.roulette.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import edu.cnm.deepdive.roulette.model.type.Color;

@Entity(
    foreignKeys = {
        @ForeignKey(
            entity = Spin.class,
            parentColumns = "spin_id", childColumns = "spin_id",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Wager {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "wager_id")
  private long id;

  @ColumnInfo(name = "spin_id", index = true)
  private long spinId;

  private Color color;

  private Byte number;

  private boolean row;

  private int amount;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getSpinId() {
    return spinId;
  }

  public void setSpinId(long spinId) {
    this.spinId = spinId;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public Byte getNumber() {
    return number;
  }

  public void setNumber(Byte number) {
    this.number = number;
  }

  public boolean isRow() {
    return row;
  }

  public void setRow(boolean row) {
    this.row = row;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

}
