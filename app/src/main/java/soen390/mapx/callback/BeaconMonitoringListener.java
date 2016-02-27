package soen390.mapx.callback;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;

import soen390.mapx.LogUtils;
import soen390.mapx.manager.NodeManager;

/**
 * Listener for IBeacon Detection
 */
public class BeaconMonitoringListener implements BeaconManager.MonitoringListener {


    @Override
    public void onEnteredRegion(Region region, List<Beacon> list) {
        Beacon beacon= list.get(0);
        LogUtils.info(this.getClass(), "onEnteredRegion", "Entered iBeacon region; Detected beacon " + beacon.getMacAddress());
        NodeManager.reachedPOI(beacon);

    }

    @Override
    public void onExitedRegion(Region region) {
        LogUtils.info(this.getClass(), "onExitedRegion", "Exited iBeacon region");
    }
}
