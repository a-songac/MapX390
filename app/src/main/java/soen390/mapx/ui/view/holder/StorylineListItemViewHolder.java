package soen390.mapx.ui.view.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arnaud.android.core.ui.BaseViewHolder;

import soen390.mapx.R;

/**
 * View holder for storyline list item
 */
public class StorylineListItemViewHolder extends BaseViewHolder {

    private ImageView image;
    private TextView title;
    private TextView description;
    private Button startButton;

    public StorylineListItemViewHolder(View viewHolder) {
        super(viewHolder);

        image = (ImageView) viewHolder.findViewById(R.id.storyline_image);
        title = (TextView) viewHolder.findViewById(R.id.storyline_title);
        description = (TextView) viewHolder.findViewById(R.id.stoyline_description);
        startButton = (Button) viewHolder.findViewById(R.id.storyline_start_button);

    }

    public ImageView getImage() {
        return image;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getDescription() {
        return description;
    }

    public Button getStartButton() {
        return startButton;
    }
}
