package soen390.mapx.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arnaud.android.core.fragment.IBaseFragment;

import soen390.mapx.R;
import soen390.mapx.activity.MainActivity;
import soen390.mapx.helper.NavigationHelper;
import soen390.mapx.manager.MapManager;
import soen390.mapx.ui.view.binder.MapFragmentViewBinder;
import soen390.mapx.ui.view.holder.MapFragmentViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements IBaseFragment {


    /**
     * Create new instance of Profile fragment
     * @return ProfileFragment : ProfileFragment new instance
     */
    public static MapFragment newInstance() {

        Bundle arguments = new Bundle();
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(arguments);
        return mapFragment;

    }


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.map_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapManager.syncActionBarStateWithCurrentMode();

        MapFragmentViewHolder viewHolder = new MapFragmentViewHolder(getView());
        MapFragmentViewBinder viewBinder = new MapFragmentViewBinder(viewHolder);
        viewBinder.bind();
    }

    @Override
    public void onBackPressed() {
        NavigationHelper.getInstance().popFragmentBackStackToMapFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.class.cast(getActivity()).isPOIReachedFromNotification()) {
            MapManager.displayOnMapPOIReached();
        }


    }
}
