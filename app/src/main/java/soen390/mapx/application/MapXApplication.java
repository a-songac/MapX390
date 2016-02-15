package soen390.mapx.application;

import com.arnaud.android.core.application.BaseApplication;
import com.orm.SugarContext;

/**
 * Main Application
 */
public class MapXApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
