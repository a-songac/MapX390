package soen390.mapx.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.arnaud.android.core.fragment.IBaseFragment;

import java.util.List;

import soen390.mapx.R;
import soen390.mapx.activity.FullscreenActivity;
import soen390.mapx.activity.MediaPlayerActivity;
import soen390.mapx.database.DummyData;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.manager.MapManager;
import soen390.mapx.model.ExpositionContent;
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

        Bundle args = getArguments();
        Node poi = null;
        if (args != null) {

            Long poiId = args.getLong(ConstantsHelper.MEDIA_PAGER_POI_ID, 0L);
            poi = Node.findById(Node.class, poiId);
        }
        Long storylineId = MapManager.isStorylineMode()?
                MapManager.getCurrentStoryline().getId():
                -1L;

        //Add all media content audio + video
        List<ExpositionContent> expositionContentList = poi.getContent(storylineId, ExpositionContent.VIDEO_TYPE);
        expositionContentList.addAll(poi.getContent(storylineId, ExpositionContent.AUDIO_TYPE));

        PoiMediaListAdapter listAdapter = new PoiMediaListAdapter(getActivity(), expositionContentList);
        setListAdapter(listAdapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getContext(), MediaPlayerActivity.class);
        if(l.getAdapter().getCount() > 0) {
            intent.putExtra(ConstantsHelper.POI_MEDIA_START_POSITION_INTENT_EXTRA_KEY,
                      ((ExpositionContent) l.getAdapter().getItem(position)).getId());
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }


}
