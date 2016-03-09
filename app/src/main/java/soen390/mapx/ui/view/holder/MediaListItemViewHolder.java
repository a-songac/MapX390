package soen390.mapx.ui.view.holder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arnaud.android.core.ui.BaseViewHolder;

import soen390.mapx.R;

/**
 * View holder for storyline list item
 */
public class MediaListItemViewHolder extends BaseViewHolder {

    private ImageView image;
    private TextView title;
    private TextView description;
    private Button startButton;

    public static final int COLLAPSED_DESCRIPTION_MAX_LINES = 4;
    public static final int EXPANDED_DESCRIPTION_MAX_LINES = 10;

    public MediaListItemViewHolder(View viewHolder) {
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

    public boolean isExpanded() {

        return startButton.getVisibility() == View.VISIBLE;

    }

    /**
     * Whether the view is collapsed
     * @param context
     */
    public void collapse(Context context) {
        startButton.setVisibility(View.GONE);
        description.setMaxLines(COLLAPSED_DESCRIPTION_MAX_LINES);
        getViewHolder().setBackgroundColor(context.getResources().getColor(R.color.white));
    }

    /**
     * Whether the view is expanded
     * @param context
     */
    public void expand(Context context) {
        startButton.setVisibility(View.VISIBLE);
        description.setMaxLines(EXPANDED_DESCRIPTION_MAX_LINES);
        getViewHolder().setBackgroundColor(context.getResources().getColor(R.color.storyline_clicked_card));
    }
}
