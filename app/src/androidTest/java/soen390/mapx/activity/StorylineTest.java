package soen390.mapx.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import soen390.mapx.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class StorylineTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);


    public void startStoryline(){
        onView(withId(R.id.toolbar)).perform(click());
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withText(R.string.navigation_drawer_story_lines)).perform(click());
        onView(withId(R.id.storyline_info_container)).perform(click());
        onView(withId(R.id.storyline_start_button)).perform(click());
        onView(withId(R.id.map_options_cancel_mode)).check(matches(isDisplayed()));
    }

    public void leaveStoryline(){
        startStoryline();
        onView(withId(R.id.map_options_cancel_mode)).perform(click());
        onView(withText(R.string.storyline_leave)).check(matches(isDisplayed()));
        onView(withText(R.string.yes)).perform(click());
        onView(withId(R.id.map_options_cancel_mode)).check(doesNotExist());
    }

    @Test
    public void testStartStoryline() {
        startStoryline();
    }

    @Test
    public void testLeaveStoryline() {
        leaveStoryline();
    }


}