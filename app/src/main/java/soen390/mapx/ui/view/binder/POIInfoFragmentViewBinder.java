package soen390.mapx.ui.view.binder;

import com.arnaud.android.core.ui.BaseViewBinder;
import com.arnaud.android.core.ui.BaseViewHolder;

import soen390.mapx.manager.MapManager;
import soen390.mapx.model.ExpositionContent;
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


        String title;
        String description;
        if (MapManager.isStorylineMode()) {
            Long storylineId = MapManager.getCurrentStoryline().getId();
            ExpositionContent content =
                    poi.getContent(storylineId, ExpositionContent.TEXT_TYPE).get(0);
            title = content.getTitle();
            description = content.getContent();
        } else {
            title = poi.getTitle();
            description = poi.getDescription();
        }

        viewHolder.getPoiDescription().setText(description);
        viewHolder.getPoiTitle().setText(title);


    }
}
