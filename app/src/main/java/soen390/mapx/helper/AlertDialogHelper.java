package soen390.mapx.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import soen390.mapx.R;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.callback.IDialogListItemClickCallback;
import soen390.mapx.callback.IDialogResponseCallBack;

/**
 * Class to implement alert dialog helper
 */
public class AlertDialogHelper {

    /**
     * Show regular alert dialog message with title, message, positive button and negative button
     * @param title
     * @param message
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
     * Initial language prompt
     * @param title
     * @param message
     * @param languages
     * @param defaultLanguageIndex
     * @param responseCallBack
     */
    public static void showLanguagePromptAlertDialog(String title, String message, CharSequence[] languages,
                                                     int defaultLanguageIndex,
                                                     final IDialogListItemClickCallback callBack,
                                                     final IDialogResponseCallBack responseCallBack) {

        Context context = MapXApplication.getGlobalContext();

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setSingleChoiceItems(languages, defaultLanguageIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callBack.onItemClick(which);
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        responseCallBack.onPositiveResponse();
                    }
                })
                .create();
        dialog.show();

    }

}
