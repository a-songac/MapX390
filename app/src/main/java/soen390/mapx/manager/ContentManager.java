package soen390.mapx.manager;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.List;

import soen390.mapx.database.DbContentManager;
import soen390.mapx.model.ExpositionContent;

/**
 * Content Manager
 */
public class ContentManager {

    private ContentManager(){}

    /**
     * Get resource id for an image name
     * @param context
     * @param imagePath
     * @return resource id or 0 if not a resource
     */
    public static int getImageResourceId(Context context, String imagePath) {
        return context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
    }

    /**
     * Get all media contents
     * @return
     */
    public static List<ExpositionContent> getAllExpositionMediaContents() {

        return ExpositionContent.find(
                ExpositionContent.class,
                "type=? OR type=? OR type=?",
                ExpositionContent.AUDIO_TYPE,
                ExpositionContent.IMAGE_TYPE,
                ExpositionContent.VIDEO_TYPE);

    }

    /**
     * Get media directory path
     * @return
     */
    public static String getMediaDirectoryPath() {
        return Environment.getExternalStorageDirectory()
                + File.separator + DbContentManager.EXTERNAL_STORAGE_MAPX_DIR + File.separator;
    }
}
