package soen390.mapx.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import soen390.mapx.BitmapUtils;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.fragment.POIImagesFragment;
import soen390.mapx.model.ExpositionContent;

/**
 * Adapter for poi image content grid view
 */
public class PoiImageAdapter extends BaseAdapter {

    private Context context;
    private String[] imagesPaths;

    private List<ExpositionContent> images;

    /**
     * Constructor
     * @param images
     */
    public PoiImageAdapter(List<ExpositionContent> images) {
        this.context = MapXApplication.getGlobalContext();
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position).getContent();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {

            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    BitmapUtils.dpToPx(POIImagesFragment.GRID_ITEM_HEIGHT_IN_DP)));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(4, 4, 4, 4);

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(BitmapUtils.loadBitmap(images.get(position).getContent()));

        return imageView;
    }

}
