package soen390.mapx;

import android.text.Editable;
import android.text.TextWatcher;

import soen390.mapx.ui.adapter.POISearchListAdapter;

/**
 * Class to implement text watcher on pois search list adapter
 */
public class POISearchTextWatcher implements TextWatcher{
    private POISearchListAdapter listAdapter;

    public POISearchTextWatcher(POISearchListAdapter listAdapter) {
        this.listAdapter = listAdapter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        listAdapter.getFilter().filter(s.toString());
    }

}
