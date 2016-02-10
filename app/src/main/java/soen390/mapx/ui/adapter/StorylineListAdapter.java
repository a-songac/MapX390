package soen390.mapx.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import soen390.mapx.R;
import soen390.mapx.model.Storyline;
import soen390.mapx.ui.view.binder.StorylineListItemViewBinder;
import soen390.mapx.ui.view.holder.StorylineListItemViewHolder;

/**
 * List Adapter for storyline list fragment
 */
public class StorylineListAdapter extends ArrayAdapter<Storyline> {

    private final Context context;
    private StorylineListItemViewHolder viewHolder;
    private StorylineListItemViewBinder binder;
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

        binder = new StorylineListItemViewBinder(viewHolder);
        binder.bind(storyline);


        return convertView;
    }




}
