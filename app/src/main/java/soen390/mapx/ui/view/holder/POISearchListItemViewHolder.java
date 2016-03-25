package soen390.mapx.ui.view.holder;

import android.view.View;
import android.widget.TextView;

import com.arnaud.android.core.ui.BaseViewHolder;

/**
 * View holder for POI Search List Fragment
 */
public class POISearchListItemViewHolder extends BaseViewHolder {


    private TextView poiTitle;

    public POISearchListItemViewHolder(View viewHolder) {
        super(viewHolder);

        poiTitle = (TextView) viewHolder.findViewById(android.R.id.text1);
    }

    public TextView getPoiTitle() {
        return poiTitle;
    }

}
