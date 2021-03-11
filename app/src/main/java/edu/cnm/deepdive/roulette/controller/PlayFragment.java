package edu.cnm.deepdive.roulette.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.databinding.FragmentPlayBinding;
import edu.cnm.deepdive.roulette.viewmodel.PlayViewModel;
import java.security.SecureRandom;
import java.util.Random;

public class PlayFragment extends Fragment {

  private static final int MIN_ROTATION_TIME = 2000;
  public static final int MAX_ROTATION_TIME = 5000;
  public static final int DEGREES_PER_REVOLUTION = 360;
  public static final int MAX_FULL_ROTATIONS = 6;
  public static final int MIN_FULL_ROTATIONS = 3;

  private PlayViewModel playViewModel;
  private FragmentPlayBinding binding;
  private boolean spinning;
  private Random rng;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.rng = new SecureRandom();
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentPlayBinding.inflate(inflater, container, false);
    binding.spinWheel.setOnClickListener((v) -> spinWheel());
    binding.placeWager.setOnClickListener((v) -> {
      Navigation.findNavController(binding.getRoot()).navigate(PlayFragmentDirections.actionNavigationPlayToNavigationWager());
    });
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
    playViewModel = new ViewModelProvider(getActivity()).get(PlayViewModel.class);
    getLifecycle().addObserver(playViewModel);
    playViewModel.getRouletteValue().observe(getViewLifecycleOwner(),
        (s) -> binding.rouletteValue.setText(s));
    playViewModel.getPocketIndex().observe(getViewLifecycleOwner(), this::rotateToPocket);
    playViewModel.getThrowable().observe(getViewLifecycleOwner(), (throwable) -> {
      if (throwable != null) {
        //noinspection ConstantConditions
        Snackbar.make(getContext(), binding.getRoot(), throwable.getMessage(),
            BaseTransientBottomBar.LENGTH_INDEFINITE).show();
      }
    });
    playViewModel.getCurrentPot().observe(getViewLifecycleOwner(),
        (pot) -> binding.currentPotValue.setText(getString(R.string.current_pot_format, pot)));
  }

  @Override
  public void onStop() {
    binding.rouletteWheel.clearAnimation(); // Stop the animation due to Back Arrow clicked.
    super.onStop();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.play_options, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    //noinspection SwitchStatementWithTooFewBranches
    switch (item.getItemId()) {
      case R.id.new_game:
        playViewModel.newGame();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
        break;
    }
    return handled;
  }

  private void spinWheel() {
    if (!spinning) {
      spinning = true;
      binding.spinWheel.setEnabled(false);
      binding.rouletteValue.setVisibility(View.INVISIBLE);
      playViewModel.spinWheel();
    }
  }

  private void rotateToPocket(Integer pocketIndex) {
    float finalRotation =
        -DEGREES_PER_REVOLUTION * pocketIndex / (float) PlayViewModel.POCKETS_ON_WHEEL;
    if (spinning) {
      float centerX = binding.rouletteWheel.getWidth() / 2f;
      float centerY = binding.rouletteWheel.getHeight() / 2f;
      float currentRotation = binding.rouletteWheel.getRotation();
      binding.rouletteWheel.setPivotX(centerX);
      binding.rouletteWheel.setPivotY(centerY);
      RotateAnimation rotation = new RotateAnimation(
          0,
          (finalRotation - currentRotation) - DEGREES_PER_REVOLUTION *
              (MIN_FULL_ROTATIONS + rng.nextInt(MAX_FULL_ROTATIONS - MIN_FULL_ROTATIONS + 1)),
          centerX,
          centerY
      );
      rotation.setDuration(MIN_ROTATION_TIME + rng.nextInt(MAX_ROTATION_TIME - MIN_ROTATION_TIME));
      rotation.setAnimationListener(new AnimationFinalizer(finalRotation));
      binding.rouletteWheel.startAnimation(rotation);
    } else {
      binding.rouletteWheel.setRotation(finalRotation);
    }
  }

  private class AnimationFinalizer implements AnimationListener {

    private final float finalRotation;

    private AnimationFinalizer(float finalRotation) {
      this.finalRotation = finalRotation;
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
      binding.rouletteWheel.setRotation(this.finalRotation);
      spinning = false;
      binding.spinWheel.setEnabled(true); //turn the button back on
      binding.rouletteValue.setVisibility(View.VISIBLE); //show the roulette value
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
  }
}