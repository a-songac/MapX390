package soen390.mapx.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.arnaud.android.core.activity.BaseActivity;
import com.arnaud.android.core.application.BaseApplication;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.Locale;

import soen390.mapx.LogUtils;
import soen390.mapx.R;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.database.DbContentManager;
import soen390.mapx.helper.ActionBarHelper;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.helper.NavigationHelper;
import soen390.mapx.helper.NotificationHelper;
import soen390.mapx.helper.PreferenceHelper;
import soen390.mapx.manager.MapManager;
import soen390.mapx.model.Node;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private static boolean drawerEnabled = true;
    private boolean poiReachedFromNotification = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceHelper.getInstance().init(this);
        setApplicationLanguage();
        setContentView(R.layout.activity_main);
        BaseApplication.setGlobalContext(this);
        initActionBar();
        initNavigationDrawer();
        DbContentManager.initDatabaseContent();

        PreferenceHelper.getInstance().init(this);

        if (!MapXApplication.isVirtualDevice()) {
            initIbeacon();
        }


        if (savedInstanceState == null) {
            NavigationHelper.getInstance().navigateToMainFragment();
        } else {
            loadLastFragment(savedInstanceState.getString(ConstantsHelper.LAST_FRAGMENT_TAG_KEY, ""));
        }

        checkIfLaunchedFromNotification();

    }

    /**
     * If activity launched from notification, set flag so that
     * position of user is displayed when map is initialized
     */
    private void checkIfLaunchedFromNotification() {
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            if (extras.containsKey(ConstantsHelper.INTENT_POI_REACHED_EXTRA_KEY)) {
                NavigationHelper.getInstance().popFragmentBackStackToMapFragment();
                poiReachedFromNotification = true;
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(
                ConstantsHelper.LAST_FRAGMENT_TAG_KEY,
                NavigationHelper.getInstance().getContainerFragment().getTag());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_map) {
            NavigationHelper.getInstance().popFragmentBackStackToMapFragment();
        }
        else if (id == R.id.nav_storyline) {
            NavigationHelper.getInstance().navigateToStorylineFragment();

        } else if (id == R.id.nav_qr_scanner) {
            //TODO Temporary, for testing purposes
            NavigationHelper.getInstance().navigateToMediaPagerFragment(0L);

        } else if (id == R.id.nav_settings) {
            NavigationHelper.getInstance().navigateToSettingsFragment(false);

        } else if (id == R.id.nav_help_feedback) {
            //TODO Temporary, for testing purposes
            NotificationHelper.getInstance().showPOIReachedNotification(Node.listAll(Node.class).get(2));
            MapManager.reachPOI(Node.listAll(Node.class).get(2));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_in_mode_options, menu);

        if (NavigationHelper.getInstance().isMapFragmentDisplayed() && (MapManager.isNavigationMode() || MapManager.isStorylineMode()))
            menu.getItem(0).setVisible(true);
        else
            menu.getItem(0).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.map_options_cancel_mode:
                MapManager.leaveCurrentMode();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        LogUtils.info(this.getClass(), "onNewIntent", "Entered onNewIntent");

        Bundle extras = intent.getExtras();
        if (null != extras) {
            if (extras.containsKey(ConstantsHelper.INTENT_POI_REACHED_EXTRA_KEY)) {
                NavigationHelper.getInstance().popFragmentBackStackToMapFragment();
                LogUtils.info(this.getClass(), "onNewIntent", "onNewIntent from POI Reached notification");
                MapManager.displayOnMapPOIReached();
                poiReachedFromNotification = true;
            }
        }
    }

    public boolean isPOIReachedFromNotification() {
        return poiReachedFromNotification;
    }

    public void userPositionDisplayedAfterNotification() {
        poiReachedFromNotification = false;
    }

    /**
     * Initialize action bar
     */
    private void initActionBar() {

        toolbar = Toolbar.class.cast(findViewById(R.id.toolbar));
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        } else {
            LogUtils.error(getClass(), "onCreate", "Null toolbar");
        }
        ActionBarHelper.getInstance().setActionBar(getSupportActionBar());
    }

    /**
     * Initialize navigation drawer
     */
    private void initNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                int position = 0;

                String tag = NavigationHelper.getInstance().getContainerFragment().getTag();
                switch (tag) {
                    case ConstantsHelper.MAP_FRAGMENT_TAG:
                        position = 0;
                        break;
                    case ConstantsHelper.SETTINGS_FRAGMENT_TAG:
                        position = 3;
                        break;
                    case ConstantsHelper.STORYLINE_FRAGMENT_TAG:
                        position = 1;
                        break;

                }

                navigationView.getMenu().getItem(position).setChecked(true);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //For when toggle is disabled, use is as a back button
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Set application language
     */
    private void setApplicationLanguage() {

        String languageToLoad =  PreferenceHelper.getInstance().getLanguagePreference();
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(
                config,
                this.getResources().getDisplayMetrics());
        LogUtils.info(this.getClass(), "setApplicationLanguage", "language set to " + languageToLoad);
    }

    /**
     * Load the last fragment referenced in the activity's saved instance state
     * @param tag : Tag of the last fragment
     */
    private void loadLastFragment(String tag){

        switch (tag) {
            case ConstantsHelper.MAP_FRAGMENT_TAG:
                NavigationHelper.getInstance().navigateToMapFragment();
                break;

            case ConstantsHelper.SETTINGS_FRAGMENT_TAG:
                NavigationHelper.getInstance().navigateToSettingsFragment(false);
                break;
            case ConstantsHelper.STORYLINE_FRAGMENT_TAG:
                NavigationHelper.getInstance().navigateToStorylineFragment();
                break;
            case ConstantsHelper.MEDIA_PAGER_FRAGMENT_TAG:
                Node lastPOI = MapManager.getLastNode();
                Long poiID = null != lastPOI?
                        lastPOI.getId():
                        0L;
                NavigationHelper.getInstance().navigateToMediaPagerFragment(poiID);
                break;

            default:
                NavigationHelper.getInstance().navigateToMainFragment();
        }

    }

    /**
     * Perform configuration checks to enable iBeacons scanning
     */
    private void initIbeacon() {
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    /**
     * Enable/disable drawer
     * @param enable : whether enable drawer
     */
    public void enableDrawer(boolean enable) {
        drawerEnabled = enable;
        int lockMode = enable ?
                DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawerLayout.setDrawerLockMode(lockMode);
        drawerToggle.setDrawerIndicatorEnabled(enable);
    }

    /**
     * Whether the drawer toggle is enabled
     * @return
     */
    public static boolean isDrawerEnabled(){
        return drawerEnabled;
    }

}
