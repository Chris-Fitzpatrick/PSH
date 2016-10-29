package com.lunaandchris.projstudenthousing.psh;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ui.main.MainActivity;

@RunWith(AndroidJUnit4.class)
/**
 * Created by Will on 10/29/16.
 */

public class MainActivityTest {

    @Rule public final ActivityRule<MainActivity> main = new ActivityRule<>(MainActivity.class);

    @Test
    public void shouldBeAbleToLaunchMainScreen() {

    }
}
