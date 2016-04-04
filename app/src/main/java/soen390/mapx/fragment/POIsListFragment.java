package soen390.mapx.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import soen390.mapx.POISearchTextWatcher;
import soen390.mapx.R;
import soen390.mapx.application.MapXApplication;
import soen390.mapx.helper.ActionBarHelper;
import soen390.mapx.helper.NavigationHelper;
import soen390.mapx.manager.NodeManager;
import soen390.mapx.model.Node;
import soen390.mapx.ui.adapter.POISearchListAdapter;
import soen390.mapx.webapp.MapJSBridge;

/**
 * A simple {@link Fragment} subclass.
 */
public class POIsListFragment extends ListFragment {

    public POIsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.poi_search_fragment_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBarHelper.getInstance().setPOIsSearchFragmentActionBar();
        getActivity().invalidateOptionsMenu();

        POISearchListAdapter listAdapter = new POISearchListAdapter(getActivity(), NodeManager.getAllPOIsOrderedByName());
        setListAdapter(listAdapter);

        EditText searchEditText = (EditText)ActionBarHelper.getInstance()
                .getActionBar().getCustomView().findViewById(R.id.poi_search_edit_text);
        searchEditText.addTextChangedListener(new POISearchTextWatcher(listAdapter));

        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    hideKeyboardFrom(MapXApplication.getGlobalContext(), v);
            }
        });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        NavigationHelper.getInstance().popFragmentBackStackToMapFragment();
        Node node = POISearchListAdapter.class.cast(getListAdapter()).getItem(position);
        MapJSBridge.getInstance().changeToPOIFloor(node.getId().toString());

    }

    /**
     * Hide soft keyboard
     * @param context
     * @param view
     */
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
