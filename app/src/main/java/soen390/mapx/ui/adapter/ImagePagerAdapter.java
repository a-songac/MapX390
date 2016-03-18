package soen390.mapx.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import soen390.mapx.fragment.ImageFullPagerFragment;
import soen390.mapx.model.ExpositionContent;

/**
 * Class to implement media view pager adapter
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    private List<ExpositionContent> images;

    public ImagePagerAdapter(FragmentManager fm, List<ExpositionContent> images) {
        super(fm);
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {

        return ImageFullPagerFragment.newInstance(images.get(position).getContent(), images.get(position).getTitle());
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
