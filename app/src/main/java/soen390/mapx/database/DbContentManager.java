package soen390.mapx.database;

import soen390.mapx.helper.PreferenceHelper;

/**
 * Class to manage persistent data
 */
public class DbContentManager {

    /**
     * Init database content in first use
     */
    public static void initDatabaseContent() {

        if (!PreferenceHelper.getInstance().isDbInitPreference()) {
            DummyData.populateDb(); //TODO temporary
            PreferenceHelper.getInstance().initDB();
        }

    }

    public static void updateContent() {

    }
}
