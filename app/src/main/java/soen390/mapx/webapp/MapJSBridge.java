package soen390.mapx.webapp;

import android.webkit.ValueCallback;
import android.webkit.WebView;

import soen390.mapx.LogUtils;

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
     */
    public void drawPath() {

        webView.evaluateJavascript("controller.startNavigation()", null);
    }


    /**
     * Notice the map the user has reached a Node
     * @param nodeId
     */
    public void reachedNode(Long nodeId) {


        webView.evaluateJavascript("controller.updateUserMarker()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                LogUtils.info(
                        this.getClass(),
                        "evaluateJavascript - controller.updateUserMarker() - onReceiveValue",
                        "Called js side with return value: " + value);
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

        webView.evaluateJavascript("controller.cancelNavigation()", null);
    }

    /**
     * Switch to the floor on which the user is if necessary
     */
    public void displayCurrentFloor(){
        webView.evaluateJavascript("controller.changeToUserLocationFloor()", null);//TODO
    }
}
