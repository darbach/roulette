package edu.cnm.deepdive.roulette.model.dto;

import com.google.gson.annotations.Expose;

public class Pocket {

  @Expose
  private String label;

  @Expose
  private int wheelPosition;

  @Expose
  private int wagerPosition;

  @Expose
  private int wagerSpan;

  @Expose
  private int color;

  @Expose
  private int payout;

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public int getWheelPosition() {
    return wheelPosition;
  }

  public void setWheelPosition(int wheelPosition) {
    this.wheelPosition = wheelPosition;
  }

  public int getWagerPosition() {
    return wagerPosition;
  }

  public void setWagerPosition(int wagerPosition) {
    this.wagerPosition = wagerPosition;
  }

  public int getWagerSpan() {
    return wagerSpan;
  }

  public void setWagerSpan(int wagerSpan) {
    this.wagerSpan = wagerSpan;
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
  }

  public int getPayout() {
    return payout;
  }

  public void setPayout(int payout) {
    this.payout = payout;
  }
}
