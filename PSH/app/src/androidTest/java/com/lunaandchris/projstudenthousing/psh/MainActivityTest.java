package com.lunaandchris.projstudenthousing.psh;

import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ui.main.MainActivity;
import ui.main.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
/**
 * Created by Will on 10/29/16.
 */

public class MainActivityTest {

    @Rule public final ActivityRule<MainActivity> main = new ActivityRule<>(MainActivity.class);

    @Test
    public void shouldBeAbleToLaunchMainScreen() {

        onView(withId(R.id.button3))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.searchAddress))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(typeText("Boston Ave"));

        onView(withId(R.id.button4))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.searchAddress))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(clearText());

        onView(withId(R.id.searchAddress))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(typeText("Electric Avenue"));

        onView(withId(R.id.button4))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
    }
}
