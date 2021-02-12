package edu.cnm.deepdive.roulette;

import android.app.Application;
import com.facebook.stetho.Stetho;

public class RouletteApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
  }
}
