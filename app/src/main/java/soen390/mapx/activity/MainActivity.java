package soen390.mapx.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.arnaud.android.core.activity.BaseActivity;
import com.arnaud.android.core.application.BaseApplication;

import java.util.Locale;

import soen390.mapx.LogUtils;
import soen390.mapx.R;
import soen390.mapx.callback.IDialogResponseCallBack;
import soen390.mapx.helper.ActionBarHelper;
import soen390.mapx.helper.AlertDialogHelper;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.helper.NavigationHelper;
import soen390.mapx.helper.PreferenceHelper;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceHelper.getInstance().init(this);
        setApplicationLanguage();
        setContentView(R.layout.activity_main);
        BaseApplication.setGlobalContext(this);
        initActionBar();
        initNavigationDrawer();
        initLanguagePreference();


        if (savedInstanceState == null) {
            NavigationHelper.getInstance().navigateToMainFragment();
        } else {
            loadLastFragment(savedInstanceState.getString(ConstantsHelper.LAST_FRAGMENT_TAG_KEY, ""));
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
            NavigationHelper.getInstance().navigateToMapFragment();
        } else if (id == R.id.nav_storyline) {
            NavigationHelper.getInstance().navigateToStorylineFragment();

        } else if (id == R.id.nav_qr_scanner) {

        } else if (id == R.id.nav_settings) {
            NavigationHelper.getInstance().navigateToSettingsFragment(false);

        } else if (id == R.id.nav_help_feedback) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

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

            default:
                NavigationHelper.getInstance().navigateToMainFragment();
        }

    }

    /**
     * Prompt the user for language preference if it is the first time he uses the application
     */
    private void initLanguagePreference() {
        if (!PreferenceHelper.getInstance().isLanguagePreferenceInit()) {

            navigationView.getMenu().getItem(3).setChecked(true);
            PreferenceHelper.getInstance().completeLanguagePreferenceInit();

            AlertDialogHelper.showInitLanguagePreferenceAlertDialog(new IDialogResponseCallBack() {
                @Override
                public void onPositiveResponse() {
                    NavigationHelper.getInstance().navigateToSettingsFragment(true);
                }

                @Override
                public void onNegativeResponse() {

                }
            });
        }

    }
}
