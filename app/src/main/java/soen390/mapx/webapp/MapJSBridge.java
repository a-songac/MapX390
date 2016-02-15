package soen390.mapx.webapp;

import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.util.List;

/**
 * Class to call the javascript functions of the MapController on the web client side
 */
public class MapJSBridge {

    private static MapJSBridge ourInstance = new MapJSBridge();

    public static MapJSBridge getInstance() {
        return ourInstance;
    }

    private MapJSBridge() {
    }

    private WebView webView;

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    /**
     * Path to draw on the map
     * @param path : list of POI ids that form the path
     */
    public void drawPath(List<Long> path) {

        //TODO

        webView.evaluateJavascript("TODO Javascript function", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }

    /**
     * Path to draw on the map
     * @param path : array POI ids of POI that form the path
     */
    public void drawPath(int[] path) {

        //TODO

        webView.evaluateJavascript("TODO Javascript function", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }


    /**
     * Notice the map the user has reached a POI
     * @param poiId
     */
    public void reachedPOI(Long poiId) {

        //TODO

        webView.evaluateJavascript("TODO Javascript function", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });

    }

    /**
     * Erase storyline path
     */
    public void leaveStoryline(){

        //TODO

        webView.evaluateJavascript("TODO Javascript function", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }

    /**
     * Erase the path currently displayed
     */
    public void leaveNavigation(){

        //TODO

        webView.evaluateJavascript("TODO Javascript function", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }
}
