package soen390.mapx;

import android.content.Intent;
import android.support.test.espresso.web.webdriver.Locator;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import soen390.mapx.activity.MainActivity;
import soen390.mapx.activity.MediaPlayerActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webClick;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class POIMediaContentTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Rule
    public ActivityTestRule<MediaPlayerActivity> mActivityRule = new ActivityTestRule<MediaPlayerActivity>(MediaPlayerActivity.class, false, false) {

        @Override
        protected void afterActivityLaunched() {
            onWebView().forceJavascriptEnabled();
        }
    };

    public String getResourceString(int resID){
        return getInstrumentation().getTargetContext().getString(resID);
    }


    @Test
    public void clickPOIViewInfo() {
        mActivityRule.launchActivity(withWebFormIntent());

        onWebView()
                // what is the id of the "view info" button?
                .withElement(findElement(Locator.ID, "web_view_info"))
                .perform(webClick());
        // What is the id of the tabs of POI media content(INFO, IMAGES, MEDIA) ?
        //onView(withId(R.id.)).perform(click());
    }



    /**
     * @return start {@link Intent} for the leafletJS map.
     */
    private static Intent withWebFormIntent() {
        Intent currentIntent = new Intent();
        return currentIntent;
    }
}
