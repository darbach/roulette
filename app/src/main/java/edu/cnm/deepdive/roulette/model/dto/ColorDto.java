package edu.cnm.deepdive.roulette.model.dto;

import com.google.gson.annotations.Expose;
import edu.cnm.deepdive.roulette.model.type.Color;

public class ColorDto implements WagerSpot {

  @Expose
  private String name;

  @Expose
  private String resource;

  // TODO
  private int colorResource;

  @Expose
  private int spot;

  @Expose
  private int span;

  @Expose
  private int payout;

  private Color color;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public int getColorResource() {
    return colorResource;
  }

  public void setColorResource(int colorResource) {
    this.colorResource = colorResource;
  }

  public int getSpot() {
    return spot;
  }

  public void setSpot(int spot) {
    this.spot = spot;
  }

  public int getSpan() {
    return span;
  }

  public void setSpan(int span) {
    this.span = span;
  }

  public int getPayout() {
    return payout;
  }

  public void setPayout(int payout) {
    this.payout = payout;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

}
