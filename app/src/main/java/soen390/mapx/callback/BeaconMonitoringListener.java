package soen390.mapx.callback;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;

import soen390.mapx.LogUtils;

/**
 * Listener for IBeacon Detection
 */
public class BeaconMonitoringListener implements BeaconManager.MonitoringListener {


    @Override
    public void onEnteredRegion(Region region, List<Beacon> list) {
        LogUtils.info(this.getClass(), "onEnteredRegion", "Entered iBeacon region; Detected beacon " + list.get(0).getMacAddress());
    }

    @Override
    public void onExitedRegion(Region region) {
        LogUtils.info(this.getClass(), "onExitedRegion", "Exited iBeacon region");
    }
}
