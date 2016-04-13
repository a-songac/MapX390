package soen390.mapx;

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
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class POISearchTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);


    public String getResourceString(int resID){
        return getInstrumentation().getTargetContext().getString(resID);
    }

    @Test
    public void searchPOIName() {

        onView(withContentDescription(getResourceString(R.string.action_bar_search))).perform(click());
        onView(withContentDescription(getResourceString(R.string.search_text_view_placeholder))).perform(click());


       /*
       onView(withId(R.id.poi_search_edit_text)).perform(click());

       onView(withContentDescription(getResourceString(R.string.navigation_drawer_open))).perform(click());
        onView(withText(R.string.action_settings)).perform(click());
        onView(withText(R.string.settings_language)).perform(click());
        onView(withText(R.string.language_french)).perform(click());
        onView(withText(R.string.settings_language)).check(matches(withText("Langues")));*/
    }

}
