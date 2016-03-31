package soen390.mapx.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import soen390.mapx.BitmapUtils;

import java.util.List;

import soen390.mapx.R;
import soen390.mapx.activity.MainActivity;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.model.ExpositionContent;
import soen390.mapx.ui.view.holder.*;

/**
 * List Adapter for poi media list fragment
 */
public class PoiMediaListAdapter extends ArrayAdapter<ExpositionContent> {

    private Context context;
    private MediaListItemViewHolder viewHolder;
    private List<ExpositionContent> items;

    /**
     * Constructor
     */
    public PoiMediaListAdapter(Context context, List<ExpositionContent> items) {
        super(context, R.layout.media_list_item);
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public ExpositionContent getItem(int position){
        if(items.size() < 0){
            return null;
        }
        return items.get(position);
    }
    @Override
    public long getItemId(int position)
    {
        return position;
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

        bindDataToView(items.get(position));

        return convertView;
    }


    /**
     * Populate list item
     * @param media
     */
    public void bindDataToView(final ExpositionContent media) {

        viewHolder.getTitle().setText(media.getContent());
        viewHolder.getDescription().setText(media.getTitle());

        if(media.isVideo()){
            viewHolder.getImageMediaType().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_theaters_black_48dp));
        } else if (media.isAudio()){
            viewHolder.getImageMediaType().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_music_note_black_48dp));
        }

        viewHolder.getImageThumbnail().setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(
                context.getResources(),
                R.drawable.moeb_logo,
                MainActivity.getWidth(),
                BitmapUtils.dpToPx((int)context.getResources().getDimension(R.dimen.stoyline_list_item_height))));

    }

}
