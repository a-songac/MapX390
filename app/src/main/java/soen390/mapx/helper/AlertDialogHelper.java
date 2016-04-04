package soen390.mapx.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.List;

import soen390.mapx.R;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.callback.IDialogResponseCallBack;
import soen390.mapx.manager.MapManager;
import soen390.mapx.manager.NodeManager;
import soen390.mapx.model.Node;
import soen390.mapx.ui.adapter.PoiBeaconStubListAdapter;

/**
 * Class to implement alert dialog helper
 */
public class AlertDialogHelper {

    /**
     * Show regular alert dialog message with title, message, positive button and negative button
     * @param callBack
     */
    public static void showAlertDialog(String title, String message, final IDialogResponseCallBack callBack){

        Context context = MapXApplication.getGlobalContext();

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callBack.onPositiveResponse();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callBack.onNegativeResponse();
                    }
                })
                .create();
        dialog.show();

    }

    /**
     * Provide a dialog with a list of all the POIs so that we can simulate beacons
     */
    public static void showPOIBeaconStubDialog() {
        Context context = MapXApplication.getGlobalContext();

        final List<Node> POIs = NodeManager.getAllPOIs();
        PoiBeaconStubListAdapter adapter = new PoiBeaconStubListAdapter(context, POIs);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("POI Beacon Stub")
                .setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NotificationHelper.getInstance().showPOIReachedNotification(POIs.get(which));
                        MapManager.reachPOI(POIs.get(which));
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * Display the content of the QR code
     * @param title
     * @param message
     */
    public static void showQRResultDialog(String title, String message) {
        Context context = MapXApplication.getGlobalContext();

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // do nothing
                    }
                })
                .create();
        dialog.show();
    }

}
