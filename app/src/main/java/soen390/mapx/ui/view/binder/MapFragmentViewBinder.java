package soen390.mapx.ui.view.binder;

import com.arnaud.android.core.ui.BaseViewBinder;
import com.arnaud.android.core.ui.BaseViewHolder;

import soen390.mapx.ui.view.holder.MapFragmentViewHolder;

/**
 * View binder for Map fragment
 */
public class MapFragmentViewBinder extends BaseViewBinder {

    private MapFragmentViewHolder viewHolder;

    public MapFragmentViewBinder(BaseViewHolder viewHolder) {
        super(viewHolder);

        this.viewHolder = (MapFragmentViewHolder) viewHolder;

    }

    /**
     * Bind data to view
     */
    public void bind() {

        // TODO Bind content to webView
        viewHolder.getWebView();

    }
}
