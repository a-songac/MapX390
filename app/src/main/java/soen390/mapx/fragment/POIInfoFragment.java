package soen390.mapx.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arnaud.android.core.fragment.IBaseFragment;

import soen390.mapx.R;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.model.Node;
import soen390.mapx.ui.view.binder.POIInfoFragmentViewBinder;
import soen390.mapx.ui.view.holder.POIInfoFragmentViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class POIInfoFragment extends Fragment implements IBaseFragment {


    /**
     * Create new instance of Profile fragment
     * @return ProfileFragment : ProfileFragment new instance
     */
    public static POIInfoFragment newInstance(Long poiId) {

        Bundle arguments = new Bundle();
        arguments.putLong(ConstantsHelper.MEDIA_PAGER_POI_ID, poiId);
        POIInfoFragment poiInfoFragment = new POIInfoFragment();
        poiInfoFragment.setArguments(arguments);
        return poiInfoFragment;

    }


    public POIInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.poi_info_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {

            Long poiId = args.getLong(ConstantsHelper.MEDIA_PAGER_POI_ID, 0L);
            Node poi = Node.findById(Node.class, poiId);
            POIInfoFragmentViewHolder viewHolder = new POIInfoFragmentViewHolder(getView());
            POIInfoFragmentViewBinder viewBinder = new POIInfoFragmentViewBinder(viewHolder);
            viewBinder.bind(poi);
        }

    }

    @Override
    public void onBackPressed() {
    }
}
