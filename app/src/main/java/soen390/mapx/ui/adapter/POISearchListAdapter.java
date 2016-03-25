package soen390.mapx.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import soen390.mapx.R;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.model.Node;
import soen390.mapx.ui.view.holder.POISearchListItemViewHolder;

/**
 * List Adapter for storyline list fragment
 */
public class POISearchListAdapter extends ArrayAdapter<Node> implements Filterable{

    private POISearchListItemViewHolder viewHolder;
    private List<Node> filteredList, fullList;
    private POIFilter contactFilter;

    /**
     * Constructor
     */
    public POISearchListAdapter(Context context, List<Node> items) {
        super(context, R.layout.story_line_list_item, items);
        filteredList = fullList = items;
        contactFilter = new POIFilter();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = MapXApplication.getGlobalContext();

        if(convertView == null){

            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new POISearchListItemViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (POISearchListItemViewHolder) convertView.getTag();
        }

        Node poi = getItem(position);

       viewHolder.getPoiTitle().setText(poi.getTitle());

        return convertView;

    }

    @Override
    public Filter getFilter() {
        return contactFilter;
    }

    @Override
    public Node getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    /**
     * Class to implement contact filter
     */
    private class POIFilter extends Filter {

        List<Node> tempList;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            tempList = new ArrayList<>();

            for (Node node : fullList) {
                if (node.getTitle().toLowerCase().contains(filterString)) {
                    tempList.add(node);
                }
            }

            FilterResults results = new FilterResults();
            results.values = tempList;
            results.count = tempList.size();
            return results;

        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (List<Node>)results.values;
            notifyDataSetChanged();
        }
    }



}
