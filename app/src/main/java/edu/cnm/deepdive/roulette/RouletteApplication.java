package edu.cnm.deepdive.roulette;

import android.app.Application;
import com.facebook.stetho.Stetho;
import edu.cnm.deepdive.roulette.service.GoogleSignInService;
import edu.cnm.deepdive.roulette.service.RouletteDatabase;
import io.reactivex.schedulers.Schedulers;

public class RouletteApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    GoogleSignInService.setContext(this);
    Stetho.initializeWithDefaults(this);
    RouletteDatabase.setContext(this);
    RouletteDatabase.getInstance()
        .getSpinDao()
        .delete()
        .subscribeOn(Schedulers.io())
        .subscribe();
  }
}
