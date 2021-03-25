package edu.cnm.deepdive.roulette.service;

import android.app.Application;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.model.dto.ColorDto;
import edu.cnm.deepdive.roulette.model.dto.ConfigurationDto;
import edu.cnm.deepdive.roulette.model.dto.PocketDto;
import edu.cnm.deepdive.roulette.model.dto.WagerSpot;
import edu.cnm.deepdive.roulette.model.type.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigurationRepository {

  private static Application context;

  private List<WagerSpot> wagerSpots; //sorted by "spot" on the wager table
  private List<PocketDto> pockets; //sorted by "position" on the roulette wheel

  private ConfigurationRepository() {
    try (
        InputStream input = context.getResources().openRawResource(R.raw.configuration);
        Reader reader = new InputStreamReader(input);
    ) {

      ConfigurationDto configurationDto = parse(reader);
      Map<String, ColorDto> colorDtoMap = buildColorMap(configurationDto);
      buildPocketList(configurationDto, colorDtoMap);
      buildWagerSpotList(colorDtoMap);

      Log.d(getClass().getName(), "Completed processing");

    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static void setContext(Application context) {
    ConfigurationRepository.context = context;
  }

  public static ConfigurationRepository getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public List<WagerSpot> getWagerSpots() {
    return wagerSpots;
  }

  public List<PocketDto> getPockets() {
    return pockets;
  }

  private ConfigurationDto parse(Reader reader) {
    Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create();
    return gson.fromJson(reader, ConfigurationDto.class);
  }

  private Map<String, ColorDto> buildColorMap(ConfigurationDto configurationDto) {
    return configurationDto //populate ColorDto's Color enum
        .getColors()
        .stream()
        .peek((c) -> {
          c.setColor(Color.valueOf(c.getName().toUpperCase()));
          c.setColorResource(
              context.getResources().getIdentifier(
                  c.getResource(), "color", context.getPackageName()));
        })
        .collect(Collectors.toMap(ColorDto::getName, Function.identity())); //pair = name, self
  }

  private void buildPocketList(ConfigurationDto configurationDto, Map<String, ColorDto> colorDtoMap) {
    pockets = configurationDto
        .getPockets()
        .stream()
        .peek((p) -> p
            .setColorDto(colorDtoMap.get(p.getColor())))//populate PocketDto's ColorDto object
        .sorted(Comparator.comparing(PocketDto::getPosition))
        .collect(Collectors.toList());
  }

  private void buildWagerSpotList(Map<String, ColorDto> colorDtoMap) {
    wagerSpots = Stream.concat(
        pockets
            .stream(),
        colorDtoMap
            .values()
            .stream()
    )
        .sorted(Comparator.comparingInt(WagerSpot::getSpot))
        .collect(Collectors.toList());
  }

  private static class InstanceHolder {

    private static final ConfigurationRepository INSTANCE = new ConfigurationRepository();

  }

}
