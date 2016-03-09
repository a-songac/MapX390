package soen390.mapx.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import soen390.mapx.fragment.POIInfoFragment;

/**
 * Class to implement media view pager adapter
 */
public class MediaPagerAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;

    public MediaPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
            default:
                return POIInfoFragment.newInstance(0L);
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
