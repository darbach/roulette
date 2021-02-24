package edu.cnm.deepdive.roulette.service;

import android.app.Application;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import edu.cnm.deepdive.roulette.model.dao.SpinDao;
import edu.cnm.deepdive.roulette.model.dao.StatisticsDao;
import edu.cnm.deepdive.roulette.model.dao.WagerDao;
import edu.cnm.deepdive.roulette.model.entity.Spin;
import edu.cnm.deepdive.roulette.model.entity.Wager;
import edu.cnm.deepdive.roulette.model.type.Color;
import edu.cnm.deepdive.roulette.model.view.ValueCount;
import edu.cnm.deepdive.roulette.service.RouletteDatabase.Converters;
import java.util.Date;

@Database(
    entities = {Spin.class, Wager.class},
    views = {ValueCount.class},
    version = 1
)
@TypeConverters(value = {Converters.class, Color.class})
public abstract class RouletteDatabase extends RoomDatabase {

  private static final String DB_NAME = "roulette_db";

  private static Application context;

  public static void setContext(Application context) {
    RouletteDatabase.context = context;
  }

  public static RouletteDatabase getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public abstract SpinDao getSpinDao();

  public abstract WagerDao getWagerDao();

  public abstract StatisticsDao getStatisticsDao();

  private static class InstanceHolder {

    private static final RouletteDatabase INSTANCE =
        Room.databaseBuilder(context, RouletteDatabase.class, DB_NAME)
            .build();

  }

  public static class Converters {

    @TypeConverter
    public static Long dateToLong(Date value) {
      return (value != null) ? value.getTime() : null;
    }

    @TypeConverter
    public static Date longToDate(Long value) {
      return (value != null) ? new Date(value) : null;
    }

  }

}
