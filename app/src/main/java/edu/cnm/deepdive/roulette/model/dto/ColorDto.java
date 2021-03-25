package edu.cnm.deepdive.roulette.model.dto;

import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import edu.cnm.deepdive.roulette.model.type.Color;
import java.util.Objects;

public class ColorDto implements WagerSpot {

  @Expose
  private String name;

  @Expose
  private String resource;

  private int colorResource;

  @Expose
  private int spot;

  @Expose
  private int span;

  @Expose
  private int payout;

  private Color color;

  private boolean hashComputed = false;

  private int hash;

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

  @Override
  public int hashCode() {
    if (!hashComputed) {
      hash = 37 * name.hashCode() + Integer.hashCode(spot);
      hashComputed = true;
    }
    return hash;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    boolean equal = false;
    if (this == obj) {
      equal = true;
    } else if (obj instanceof ColorDto) {
      ColorDto other = (ColorDto) obj;
      equal = name.equals(other.name) && spot == other.spot;
    }
    return equal;
  }
}
