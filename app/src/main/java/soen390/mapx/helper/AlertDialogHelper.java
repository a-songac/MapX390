package soen390.mapx.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import soen390.mapx.R;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.callback.IDialogResponseCallBack;

/**
 * Class to implement alert dialog helper
 */
public class AlertDialogHelper {

    public static void showInitLanguagePreferenceAlertDialog(final IDialogResponseCallBack callBack){

        Context context = MapXApplication.getGlobalContext();

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.language_init_dialog_title)
                .setMessage(R.string.language_init_dialog_body)
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

}
