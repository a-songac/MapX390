package soen390.mapx.ui.view.binder;

import android.view.View;

import com.arnaud.android.core.ui.BaseViewBinder;
import com.arnaud.android.core.ui.BaseViewHolder;

import soen390.mapx.UiUtils;
import soen390.mapx.fragment.StorylineListFragment;
import soen390.mapx.model.Storyline;
import soen390.mapx.ui.view.holder.StorylineListItemViewHolder;

/**
 * View binder for Map fragment
 */
public class StorylineListItemViewBinder extends BaseViewBinder {

    private StorylineListItemViewHolder viewHolder;

    public StorylineListItemViewBinder(BaseViewHolder viewHolder) {
        super(viewHolder);

        this.viewHolder = (StorylineListItemViewHolder) viewHolder;

    }

    /**
     * Bind storyline data to view
     * @param storyline
     */
    public void bind(Storyline storyline) {

        // TODO english and french to be supported
        viewHolder.getTitle().setText(storyline.getTitle());
        viewHolder.getDescription().setText(storyline.getDescription());

        viewHolder.getStartButton().setVisibility(View.GONE);
        viewHolder.getDescription().setMaxLines(StorylineListFragment.COLLAPSED_DESCRIPTION_MAX_LINES);

        viewHolder.getStartButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.displayToast("Start button clicked");
            }
        });

    }
}
