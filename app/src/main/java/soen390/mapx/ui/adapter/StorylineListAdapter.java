package soen390.mapx.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import soen390.mapx.R;
import soen390.mapx.UiUtils;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.fragment.StorylineListFragment;
import soen390.mapx.manager.MapManager;
import soen390.mapx.model.Storyline;
import soen390.mapx.ui.view.holder.StorylineListItemViewHolder;

/**
 * List Adapter for storyline list fragment
 */
public class StorylineListAdapter extends ArrayAdapter<Storyline> {

    private Context context;
    private StorylineListItemViewHolder viewHolder;

    /**
     * Constructor
     */
    public StorylineListAdapter(Context context, List<Storyline> items) {
        super(context, R.layout.story_line_list_item, items);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = MapXApplication.getGlobalContext();

        if(convertView == null){

            convertView = LayoutInflater.from(context).inflate(R.layout.story_line_list_item, parent, false);
            viewHolder = new StorylineListItemViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (StorylineListItemViewHolder) convertView.getTag();
        }

        Storyline storyline = getItem(position);

        if (position == StorylineListFragment.expandedPosition) {
            viewHolder.expand(context);
        } else {
            viewHolder.collapse(context);
        }

        bindDataToView(storyline);

        return convertView;
    }


    /**
     * Populate list item
     * @param storyline
     */
    public void bindDataToView(final Storyline storyline) {

        viewHolder.getTitle().setText(storyline.getTitle());
        viewHolder.getDescription().setText(storyline.getDescription());
        viewHolder.getStartButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toast = context.getResources().getString(R.string.storyline_start_toast, storyline.getTitle());
                UiUtils.displayToastLong(toast);
                MapManager.launchStoryline(storyline.getId());
                StorylineListFragment.expandedPosition = -1;

            }
        });
    }

}
