package soen390.mapx.ui.view.binder;

import com.arnaud.android.core.ui.BaseViewBinder;
import com.arnaud.android.core.ui.BaseViewHolder;

import soen390.mapx.model.Node;
import soen390.mapx.ui.view.holder.POIInfoFragmentViewHolder;

/**
 * View binder for Map fragment
 */
public class POIInfoFragmentViewBinder extends BaseViewBinder {

    private POIInfoFragmentViewHolder viewHolder;

    public POIInfoFragmentViewBinder(BaseViewHolder viewHolder) {
        super(viewHolder);

        this.viewHolder = (POIInfoFragmentViewHolder) viewHolder;

    }

    /**
     * Bind Leaflet maps to WebView
     */
    public void bind(Node poi) {

       viewHolder.getPoiTitle().setText(poi.getTitle());
        viewHolder.getPoiDescription().setText("TODO");//TODO Fetch poi text content


    }
}
