package soen390.mapx;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import soen390.mapx.activity.InitLanguageActivity;
import soen390.mapx.activity.MainActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class InitLanguageActivityTest {

    @Rule
    public ActivityTestRule<InitLanguageActivity> initLanguageActivityTestRule = new ActivityTestRule<InitLanguageActivity>(InitLanguageActivity.class);

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);


    public String getResourceString(int resID){
        return getInstrumentation().getTargetContext().getString(resID);
    }

    @Test
    public void setInitLanguageUITestFr() {
        onView(withId(R.id.fr_button)).perform(click());
        onView(withId(R.id.toolbar)).perform(click());
        onView(withContentDescription(getResourceString(R.string.navigation_drawer_open))).perform(click());
        onView(withText("Histoire")).check(matches(isDisplayed()));

    }


    @Test
    public void setInitLanguageUITestEn() {
        onView(withId(R.id.en_button)).perform(click());
        onView(withId(R.id.toolbar)).perform(click());
        onView(withContentDescription(getResourceString(R.string.navigation_drawer_open))).perform(click());
        onView(withText("Story-lines")).check(matches(isDisplayed()));
    }
}
