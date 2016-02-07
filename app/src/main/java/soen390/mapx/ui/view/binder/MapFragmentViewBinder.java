package soen390.mapx.ui.view.binder;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.arnaud.android.core.ui.BaseViewBinder;
import com.arnaud.android.core.ui.BaseViewHolder;

import soen390.mapx.application.MapXApplication;
import soen390.mapx.ui.view.holder.MapFragmentViewHolder;
import soen390.mapx.webapp.MyWebViewClient;
import soen390.mapx.webapp.POIJSInterface;

/**
 * View binder for Map fragment
 */
public class MapFragmentViewBinder extends BaseViewBinder {

    private MapFragmentViewHolder viewHolder;
    private WebView webView;
    private WebViewClient webViewClient;

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

        webViewClient = new MyWebViewClient();
        webView.addJavascriptInterface(new POIJSInterface(MapXApplication.getGlobalContext()), POIJSInterface.ANDROID_JAVASCRIPT_OBJECT);
        webView.setWebViewClient(webViewClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/map_webview/index.html");


    }
}
