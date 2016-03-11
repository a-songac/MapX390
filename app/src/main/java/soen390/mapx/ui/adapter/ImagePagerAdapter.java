package soen390.mapx.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import soen390.mapx.fragment.ImageFullPagerFragment;

/**
 * Class to implement media view pager adapter
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    private String[] imagePaths;

    public ImagePagerAdapter(FragmentManager fm, String[] imagePaths) {
        super(fm);
        this.imagePaths = imagePaths;
    }

    @Override
    public Fragment getItem(int position) {

        return ImageFullPagerFragment.newInstance(imagePaths[position], "Image Name");
    }

    @Override
    public int getCount() {
        return imagePaths.length;
    }
}
