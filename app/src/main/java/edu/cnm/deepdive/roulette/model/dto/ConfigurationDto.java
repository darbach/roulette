package edu.cnm.deepdive.roulette.model.dto;

import com.google.gson.annotations.Expose;
import java.util.List;

public class ConfigurationDto {

  @Expose
  private List<PocketDto> pockets;

  @Expose
  private List<ColorDto> colors;

  public List<PocketDto> getPockets() {
    return pockets;
  }

  public void setPockets(List<PocketDto> pockets) {
    this.pockets = pockets;
  }

  public List<ColorDto> getColors() {
    return colors;
  }

  public void setColors(List<ColorDto> colors) {
    this.colors = colors;
  }

}
