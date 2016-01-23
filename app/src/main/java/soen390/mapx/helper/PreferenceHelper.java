package soen390.mapx.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**Class to implement Created by Arnaud on 2015-09-28.
 */
public class PreferenceHelper {
    /**
     * Single ton instance
     */
    private static PreferenceHelper INSTANCE = new PreferenceHelper();
    public static PreferenceHelper getInstance() {
        return INSTANCE;
    }
    private PreferenceHelper() {
    }

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }


    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();

    }

    public void clear(){
        editor.clear();
        editor.commit();
    }


}
