package soen390.mapx.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arnaud.android.core.fragment.IBaseFragment;

import soen390.mapx.R;
import soen390.mapx.activity.MainActivity;
import soen390.mapx.helper.ActionBarHelper;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.helper.NavigationHelper;
import soen390.mapx.model.Node;
import soen390.mapx.ui.adapter.MediaPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaViewPagerFragment extends Fragment implements IBaseFragment {


    /**
     * Create new instance of Profile fragment
     * @return ProfileFragment : ProfileFragment new instance
     */
    public static MediaViewPagerFragment newInstance(Long poiId) {

        Bundle arguments = new Bundle();
        arguments.putLong(ConstantsHelper.MEDIA_PAGER_POI_ID, poiId);
        MediaViewPagerFragment mediaViewPagerFragment = new MediaViewPagerFragment();
        mediaViewPagerFragment.setArguments(arguments);
        return mediaViewPagerFragment;

    }


    public MediaViewPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.media_tabs_viewpager, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.class.cast(getActivity()).enableDrawer(false);

        Bundle args = getArguments();

        if (args != null) {

            Long  poiId = args.getLong(ConstantsHelper.MEDIA_PAGER_POI_ID, 0L);
            Node poi = Node.findById(Node.class, poiId);
            ActionBarHelper.getInstance().setMediaContentActionBar(poi.getTitle());

            View root = getView();

            if (null != root) {
                initTabPager(root, poiId);
            }
        }

    }

    @Override
    public void onBackPressed() {
        NavigationHelper.getInstance().popFragmentBackStackToMapFragment();
    }

    /**
     * Initialize tab pager
     */
    private void initTabPager(View root, Long poiId) {

        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_info)));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_images));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_media));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) root.findViewById(R.id.pager);
        final MediaPagerAdapter adapter =
                new MediaPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), poiId);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
