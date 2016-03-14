package soen390.mapx.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.arnaud.android.core.fragment.IBaseFragment;

import soen390.mapx.R;
import soen390.mapx.activity.FullscreenActivity;
import soen390.mapx.database.DummyData;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.model.Node;
import soen390.mapx.ui.adapter.PoiImageAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class POIImagesFragment extends Fragment implements IBaseFragment {


    /**
     * Create new instance of Profile fragment
     * @return ProfileFragment : ProfileFragment new instance
     */
    public static POIImagesFragment newInstance(Long poiId) {

        Bundle arguments = new Bundle();
        arguments.putLong(ConstantsHelper.MEDIA_PAGER_POI_ID, poiId);
        POIImagesFragment poiImagesFragment = new POIImagesFragment();
        poiImagesFragment.setArguments(arguments);
        return poiImagesFragment;

    }


    public POIImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.poi_images_grid_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {

            Long poiId = args.getLong(ConstantsHelper.MEDIA_PAGER_POI_ID, 0L);
            Node poi = Node.findById(Node.class, poiId);

            View root = getView();

            if (null != root) {
                GridView gridview = (GridView) root.findViewById(R.id.gridview);
                setGridView(gridview, poi);

            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * Set gridView
     * @param gridView
     */
    private void setGridView(GridView gridView, final Node poi){

        final String[] imagesPath = DummyData.dummyImages();//TODO TEMP Get image of the POI

        gridView.setAdapter(new PoiImageAdapter(imagesPath));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent intent = new Intent(getContext(), FullscreenActivity.class);

                intent.putExtra(ConstantsHelper.POI_IMAGE_START_POSITION_INTENT_EXTRA_KEY, position);
                intent.putExtra(ConstantsHelper.POI_ID_INTENT_EXTRA_KEY, poi.getId()                                                    );

                startActivity(intent);

            }
        });

    }
}
