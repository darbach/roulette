package edu.cnm.deepdive.roulette.model.dto;

import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;

public class PocketDto implements WagerSpot {

  @Expose
  private String name;

  @Expose
  private int position;

  @Expose
  private int spot;

  @Expose
  private int span;

  @Expose
  private String color;

  private ColorDto colorDto;

  @Expose
  private int payout;

  private boolean hashComputed = false;

  private int hash;

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  @Override
  public int getSpot() {
    return spot;
  }

  public void setSpot(int spot) {
    this.spot = spot;
  }

  @Override
  public int getSpan() {
    return span;
  }

  public void setSpan(int span) {
    this.span = span;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public ColorDto getColorDto() {
    return colorDto;
  }

  public void setColorDto(ColorDto colorDto) {
    this.colorDto = colorDto;
  }

  @Override
  public int getColorResource() {
    return colorDto.getColorResource();
  }

  @Override
  public int getPayout() {
    return payout;
  }

  public void setPayout(int payout) {
    this.payout = payout;
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
    } else if (obj instanceof PocketDto) {
      PocketDto other = (PocketDto) obj;
      equal = name.equals(other.name) && spot == other.spot;
    }
    return equal;
  }

}
