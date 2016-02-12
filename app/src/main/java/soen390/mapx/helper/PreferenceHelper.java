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

    /**
     * Get language preference
     * @return String: Language preference
     */
    public String getLanguagePreference(){
        return sharedPreferences.getString(ConstantsHelper.PREF_LANGUAGE_KEY, ConstantsHelper.PREF_LANGUAGE_DEFAULT);
    }


    /**
     * Whether the user was prompt to change the language of preference of the application on first use
     * @return Boolean
     */
    public boolean isLanguagePreferenceInit(){
        return sharedPreferences.getBoolean(ConstantsHelper.PREF_LANGUAGE_INIT_KEY, false);
    }

    /**
     * Set that the user was prompt to change the language of preference on first use
     */
    public void completeLanguagePreferenceInit() {
        editor.putBoolean(ConstantsHelper.PREF_LANGUAGE_INIT_KEY, true);
        editor.commit();
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }

    public void initDB() {
        editor.putBoolean(ConstantsHelper.DB_INIT_KEY, true);
        editor.commit();
    }

    public boolean isDbInitPreference() {
        return sharedPreferences.getBoolean(ConstantsHelper.DB_INIT_KEY, false);
    }

}
