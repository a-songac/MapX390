package soen390.mapx.ui.view.holder;

import android.view.View;
import android.widget.TextView;

import com.arnaud.android.core.ui.BaseViewHolder;

import soen390.mapx.R;

/**
 * View holder for Map Fragment
 */
public class POIInfoFragmentViewHolder extends BaseViewHolder {


    private TextView poiTitle;
    private TextView poiDescription;

    public POIInfoFragmentViewHolder(View viewHolder) {
        super(viewHolder);

        poiTitle = (TextView) viewHolder.findViewById(R.id.poi_title);
        poiDescription = (TextView) viewHolder.findViewById(R.id.poi_description);
    }

    public TextView getPoiTitle() {
        return poiTitle;
    }

    public TextView getPoiDescription() {
        return poiDescription;
    }
}
