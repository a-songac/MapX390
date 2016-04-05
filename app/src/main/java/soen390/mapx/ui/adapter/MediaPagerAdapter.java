package soen390.mapx.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import soen390.mapx.fragment.POIImagesFragment;
import soen390.mapx.fragment.POIInfoFragment;
import soen390.mapx.fragment.POIMediaFragment;

/**
 * Class to implement media view pager adapter
 */
public class MediaPagerAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;
    private Long poiId;
    private boolean hasMedia;

    public MediaPagerAdapter(FragmentManager fm, int numOfTabs, Long poiId, boolean hasMedia) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.poiId = poiId;
        this.hasMedia = hasMedia;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return POIInfoFragment.newInstance(poiId);

            case 1:
                if (numOfTabs == 2 && hasMedia) {
                    return POIMediaFragment.newInstance(poiId);
                }
                return POIImagesFragment.newInstance(poiId);

            case 2:
                return POIMediaFragment.newInstance(poiId);

            default:
                return POIInfoFragment.newInstance(poiId);
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
