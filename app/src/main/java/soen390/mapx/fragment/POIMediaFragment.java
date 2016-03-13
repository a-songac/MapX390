package soen390.mapx.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.arnaud.android.core.fragment.IBaseFragment;

import soen390.mapx.R;
import soen390.mapx.database.DummyData;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.model.Node;
import soen390.mapx.model.Storyline;
import soen390.mapx.ui.adapter.PoiMediaListAdapter;
import soen390.mapx.ui.view.binder.POIInfoFragmentViewBinder;
import soen390.mapx.ui.view.holder.MediaListItemViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class POIMediaFragment extends ListFragment implements IBaseFragment {


    /**
     * Create new instance of Profile fragment
     * @return ProfileFragment : ProfileFragment new instance
     */
    public static POIMediaFragment newInstance(Long poiId) {

        Bundle arguments = new Bundle();
        arguments.putLong(ConstantsHelper.MEDIA_PAGER_POI_ID, poiId);
        POIMediaFragment poiMediaFragment = new POIMediaFragment();
        poiMediaFragment.setArguments(arguments);
        return poiMediaFragment;

    }


    public POIMediaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.poi_media_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PoiMediaListAdapter listAdapter = new PoiMediaListAdapter(getActivity(),  DummyData.dummyImages());
        setListAdapter(listAdapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //start media player

        fullyShowHalfHiddenItem(position);

    }

    /**
     * Scroll the list to fully show half hidden list items
     * @param position
     */
    private void fullyShowHalfHiddenItem(int position) {
        getListView().smoothScrollToPosition(position);
        if (position == getListAdapter().getCount() - 1) {
            getListView().setSelection(position);
        }
    }

    @Override
    public void onBackPressed() {
    }


}
