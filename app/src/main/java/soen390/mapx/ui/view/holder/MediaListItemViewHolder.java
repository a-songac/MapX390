package soen390.mapx.ui.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arnaud.android.core.ui.BaseViewHolder;

import soen390.mapx.R;

/**
 * View holder for storyline list item
 */
public class MediaListItemViewHolder extends BaseViewHolder {

    private ImageView image_thumbnail;
    private ImageView image_media_type;
    private TextView title;
    private TextView description;


    public MediaListItemViewHolder(View viewHolder) {
        super(viewHolder);

        image_thumbnail = (ImageView) viewHolder.findViewById(R.id.media_thumbnail);
        image_media_type = (ImageView) viewHolder.findViewById(R.id.media_type_image);
        title = (TextView) viewHolder.findViewById(R.id.media_title);
        description = (TextView) viewHolder.findViewById(R.id.media_description);

    }

    public ImageView getImageThumbnail() {
        return image_thumbnail;
    }

    public ImageView getImageMediaType() {
        return image_media_type;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getDescription() {
        return description;
    }

}
