package soen390.mapx.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import soen390.mapx.application.MapXApplication;
import soen390.mapx.model.Node;

/**
 * List Adapter for storyline list fragment
 */
public class PoiBeaconStubListAdapter extends ArrayAdapter<Node> {

    private Context context;
    private ViewHolder viewHolder;

    /**
     * Constructor
     */
    public PoiBeaconStubListAdapter(Context context, List<Node> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = MapXApplication.getGlobalContext();

        if(convertView == null){

            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Node poi = getItem(position);
        viewHolder.getTextView().setText(poi.getTitle() + " (" + String.valueOf(poi.getId()) + ")");

        return convertView;
    }


    private static class ViewHolder{

        private TextView textView;

        public ViewHolder(View root) {
            textView = (TextView) root.findViewById(android.R.id.text1);
        }

        public TextView getTextView() {
            return textView;
        }
    }

}
