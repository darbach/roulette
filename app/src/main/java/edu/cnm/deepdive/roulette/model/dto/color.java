package edu.cnm.deepdive.roulette.model.dto;

import com.google.gson.annotations.Expose;

public class color {

  @Expose
  private String label;

  @Expose
  private String value;

  @Expose
  private int wagerPosition;

  @Expose
  private int wagerSpan;

  @Expose
  private int payout;

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
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

  public int getPayout() {
    return payout;
  }

  public void setPayout(int payout) {
    this.payout = payout;
  }
}
