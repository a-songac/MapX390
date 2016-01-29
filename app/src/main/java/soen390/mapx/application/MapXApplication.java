package soen390.mapx.application;

import com.arnaud.android.core.application.BaseApplication;
import com.orm.SugarContext;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import soen390.mapx.callback.BeaconMonitoringListener;

/**
 * Main Application
 */
public class MapXApplication extends BaseApplication {

    private BeaconManager beaconManager;

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        initIBeaconMonitoring();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    /**
     * Set application to monitor ibeacons
     */
    private void initIBeaconMonitoring() {
        beaconManager = new BeaconManager(getApplicationContext());


        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        null,
                        null,
                        null));
            }
        });

        beaconManager.setMonitoringListener(new BeaconMonitoringListener());
    }

}
