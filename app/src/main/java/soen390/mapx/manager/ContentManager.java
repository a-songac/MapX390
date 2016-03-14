package soen390.mapx.manager;

import android.content.Context;

/**
 * Content Manager
 */
public class ContentManager {

    /**
     * Get resource id for an image name
     * @param context
     * @param imagePath
     * @return resource id or 0 if not a resource
     */
    public static int getImageResourceId(Context context, String imagePath) {
        return context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
    }
}
