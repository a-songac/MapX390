package soen390.mapx.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import soen390.mapx.application.MapXApplication;
import soen390.mapx.manager.ContentManager;

/**
 * Adapter for poi image content grid view
 */
public class PoiImageAdapter extends BaseAdapter {

    private Context context;
    private String[] imagesPaths;

    public PoiImageAdapter(String[] imagesPaths) {

        if (null != imagesPaths) {
            context = MapXApplication.getGlobalContext();
            this.imagesPaths = imagesPaths;
        }
    }

    @Override
    public int getCount() {
        return imagesPaths.length;
    }

    @Override
    public Object getItem(int position) {
        return imagesPaths[position];
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
                    450));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(4, 4, 4, 4);

        } else {
            imageView = (ImageView) convertView;
        }

        int imageResourceId = ContentManager.getImageResourceId(context, imagesPaths[position]);
        if (0 != imageResourceId) {
            imageView.setImageResource(imageResourceId);
        }
        return imageView;
    }

}
