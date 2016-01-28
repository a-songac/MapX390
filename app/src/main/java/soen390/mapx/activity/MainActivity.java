package soen390.mapx.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import soen390.mapx.helper.ActionBarHelper;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.helper.NavigationHelper;
import soen390.mapx.helper.PreferenceHelper;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setApplicationLanguage();
        setContentView(R.layout.activity_main);
        BaseApplication.setGlobalContext(this);
        initActionBar();
        initNavigationDrawer();
        PreferenceHelper.getInstance().init(this);

        NavigationHelper.getInstance().navigateToMainFragment();

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            NavigationHelper.getInstance().navigateToMapFragment();
        } else if (id == R.id.nav_storyline) {

        } else if (id == R.id.nav_qr_scanner) {

        } else if (id == R.id.nav_settings) {
            NavigationHelper.getInstance().navigateToSettingsFragment();

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Set application language
     */
    private void setApplicationLanguage() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String languageToLoad =  preferences.getString(
                ConstantsHelper.PREF_LANGUAGE,
                ConstantsHelper.PREF_LANGUAGE_DEFAULT);
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(
                config,
                this.getResources().getDisplayMetrics());
    }
}
