package soen390.mapx.application;

import android.os.Build;

import com.arnaud.android.core.application.BaseApplication;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.orm.SugarContext;

import soen390.mapx.LogUtils;
import soen390.mapx.callback.BeaconMonitoringListener;

/**
 * Main Application
 */
public class MapXApplication extends BaseApplication {

    private BeaconManager beaconManager;
    private static boolean virtualDevice;

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);


        virtualDevice = Build.FINGERPRINT.startsWith("generic") || "generic".equals(Build.BRAND.toLowerCase());
        LogUtils.info(this.getClass(), "onCreate" , "Build FINGERPRINT: " + Build.FINGERPRINT);
        LogUtils.info(this.getClass(), "onCreate" , "Build BRAND: " + Build.BRAND);

        if (! virtualDevice) {
            initIBeaconMonitoring();
        }

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

    public static boolean isVirtualDevice() {
        return virtualDevice;
    }
}
