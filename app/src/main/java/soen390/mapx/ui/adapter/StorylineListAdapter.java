package soen390.mapx.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import soen390.mapx.R;
import soen390.mapx.UiUtils;
import soen390.mapx.fragment.StorylineListFragment;
import soen390.mapx.model.Storyline;
import soen390.mapx.ui.view.holder.StorylineListItemViewHolder;

/**
 * List Adapter for storyline list fragment
 */
public class StorylineListAdapter extends ArrayAdapter<Storyline> {

    private final Context context;
    private StorylineListItemViewHolder viewHolder;
    private Storyline storyline;

    /**
     * Constructor
     */
    public StorylineListAdapter(Context context, List<Storyline> items) {
        super(context, R.layout.story_line_list_item, items);

        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){

            convertView = LayoutInflater.from(context).inflate(R.layout.story_line_list_item, parent, false);
            viewHolder = new StorylineListItemViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (StorylineListItemViewHolder) convertView.getTag();
        }

        storyline = getItem(position);

        if (position == StorylineListFragment.expandedPosition) {
            viewHolder.expand(context);
        } else {
            viewHolder.collapse(context);
        }

        bindDataToView();

        return convertView;
    }


    /**
     * Populate list item
     */
    public void bindDataToView() {

        viewHolder.getTitle().setText(storyline.getTitle());
        viewHolder.getDescription().setText(storyline.getDescription());
        viewHolder.getStartButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.displayToast("Start button clicked");
            }
        });
    }

}
