package soen390.mapx;

import android.support.test.espresso.AmbiguousViewMatcherException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import soen390.mapx.activity.MainActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class LanguagesUITest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);


    public String getResourceString(int resID){
        return getInstrumentation().getTargetContext().getString(resID);
    }

    @Test
    public void setApplicationLanguageUITestFr() {
        onView(withId(R.id.toolbar)).perform(click());
        onView(withContentDescription(getResourceString(R.string.navigation_drawer_open))).perform(click());
        onView(withText(R.string.action_settings)).perform(click());
        onView(withText(R.string.settings_language)).perform(click());
        onView(withText(R.string.language_french)).perform(click());
        onView(withText(R.string.settings_language)).check(matches(withText("Langues")));
    }

    @Test
    public void setApplicationLanguageUITestEng() throws AmbiguousViewMatcherException{
        onView(withId(R.id.toolbar)).perform(click());
        onView(withContentDescription(getResourceString(R.string.navigation_drawer_open))).perform(click());
        onView(withText(R.string.action_settings)).perform(click());
        onView(withText(R.string.settings_language)).perform(click());
        onView(withText(R.string.language_english)).perform(click());
        onView(withText(R.string.navigation_drawer_museum_map)).check(matches(withText("Museum Map")));
    }


}
