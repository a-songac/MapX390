package soen390.mapx.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.List;

import soen390.mapx.R;
import soen390.mapx.UiUtils;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.fragment.StorylineListFragment;
import soen390.mapx.manager.MapManager;
import soen390.mapx.model.Storyline;
import soen390.mapx.ui.view.holder.*;

/**
 * List Adapter for poi media list fragment
 */
public class PoiMediaListAdapter extends ArrayAdapter<String> {

    private Context context;
    private MediaListItemViewHolder viewHolder;
    private String[] mediaPath;

    /**
     * Constructor
     */
    public PoiMediaListAdapter(Context context, String [] items) {
        super(context, R.layout.media_list_item, items);
        this.context = context;
        this.mediaPath = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = MapXApplication.getGlobalContext();

        if(convertView == null){

            convertView = LayoutInflater.from(context).inflate(R.layout.media_list_item, parent, false);
            viewHolder = new MediaListItemViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (MediaListItemViewHolder) convertView.getTag();
        }


        bindDataToView(getItem(position));

        return convertView;
    }


    /**
     * Populate list item
     * @param media
     */
    public void bindDataToView(final String media) {
        viewHolder.getTitle().setText("Nipper");
        viewHolder.getDescription().setText("Lorem ipsum");
        //TODO check media type
        viewHolder.getImageThumbnail().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.moeb_logo));
        viewHolder.getImageMediaType().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_movie_black_48dp));
    }

}
