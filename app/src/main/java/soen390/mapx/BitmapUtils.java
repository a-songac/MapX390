package soen390.mapx;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import soen390.mapx.application.MapXApplication;
import soen390.mapx.fragment.POIImagesFragment;
import soen390.mapx.manager.ContentManager;

/**
 * Utils for handling images
 */
public class BitmapUtils {

    private BitmapUtils(){}

    /**
     * Load bitmap from resources or external storage
     * @param imageName
     * @return
     */
    public static Bitmap loadBitmap(String imageName) {

        Context context = MapXApplication.getGlobalContext();

        Bitmap bitmap;
        int imageResourceId = ContentManager.getImageResourceId(context, imageName);

        if (0 != imageResourceId) {

            bitmap = BitmapUtils.decodeSampledBitmapFromResource(
                    context.getResources(),
                    imageResourceId,
                    UiUtils.getRootViewWidth() / 2,//gridView has 2 columns
                    BitmapUtils.dpToPx(POIImagesFragment.GRID_ITEM_HEIGHT_IN_DP));

        } else {

            String path  = ContentManager.getMediaDirectoryPath() + imageName;

            bitmap = BitmapUtils.decodeSampleBitmapFromExternalStorage(
                    path,
                    UiUtils.getRootViewWidth() / 2,//gridView has 2 columns
                    BitmapUtils.dpToPx(POIImagesFragment.GRID_ITEM_HEIGHT_IN_DP));

        }
        return  bitmap;
    }

    /**
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Load bitmap from resources
     * @param res
     * @param resId
     * @param reqWidth Width in pixels
     * @param reqHeight Height in pixels
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    /**
     * Load bitmap from file
     * @param path
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampleBitmapFromExternalStorage(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * dp to pixels
     * @param dp
     * @return
     */
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Pixels to dp
     * @param px
     * @return
     */
    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
