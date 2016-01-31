package soen390.mapx.ui.view.binder;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.arnaud.android.core.ui.BaseViewBinder;
import com.arnaud.android.core.ui.BaseViewHolder;

import soen390.mapx.ui.view.holder.MapFragmentViewHolder;

/**
 * View binder for Map fragment
 */
public class MapFragmentViewBinder extends BaseViewBinder {

    private MapFragmentViewHolder viewHolder;
    private WebView webView;

    public MapFragmentViewBinder(BaseViewHolder viewHolder) {
        super(viewHolder);

        this.viewHolder = (MapFragmentViewHolder) viewHolder;
        webView = this.viewHolder.getWebView();

    }

    /**
     * Bind Leaflet maps to WebView
     */
    @SuppressWarnings("setJavascriptEnabled")
    public void bind() {

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/map_webview/index.html");


    }
}
