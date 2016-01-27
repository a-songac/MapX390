package soen390.mapx.ui.view.holder;

import android.view.View;
import android.webkit.WebView;

import com.arnaud.android.core.ui.BaseViewHolder;

import soen390.mapx.R;

/**
 * View holder for Map Fragment
 */
public class MapFragmentViewHolder extends BaseViewHolder {


    private WebView webView;

    public MapFragmentViewHolder(View viewHolder) {
        super(viewHolder);

        webView = (WebView) viewHolder.findViewById(R.id.map_fragment_web_view);
    }

    public WebView getWebView() {
        return webView;
    }
}
