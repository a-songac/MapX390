package soen390.mapx.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.arnaud.android.core.fragment.IBaseFragment;

import soen390.mapx.R;
import soen390.mapx.helper.ActionBarHelper;
import soen390.mapx.helper.NavigationHelper;
import soen390.mapx.model.Storyline;
import soen390.mapx.ui.adapter.StorylineListAdapter;
import soen390.mapx.ui.view.holder.StorylineListItemViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StorylineListFragment extends ListFragment implements IBaseFragment {

    private View expandedView = null;
    public static int expandedPosition = -1;

    /**
     * Create new instance of Profile fragment
     * @return ProfileFragment : ProfileFragment new instance
     */
    public static StorylineListFragment newInstance() {

        Bundle arguments = new Bundle();
        StorylineListFragment fragment = new StorylineListFragment();
        fragment.setArguments(arguments);
        return fragment;

    }


    public StorylineListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.story_line_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBarHelper.getInstance().setStorylineFragmentActionBar();
        getActivity().invalidateOptionsMenu();

        StorylineListAdapter listAdapter = new StorylineListAdapter(getActivity(), Storyline.listAll(Storyline.class));
        setListAdapter(listAdapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != expandedView && expandedView != v) {
            StorylineListItemViewHolder.class.cast(expandedView.getTag()).collapse(getContext());
        }

        Object tag = v.getTag();

        if (null != tag) {

            StorylineListItemViewHolder itemViewHolder = (StorylineListItemViewHolder) tag;

            if (itemViewHolder.isExpanded()) {
                itemViewHolder.collapse(getContext());
            } else {
                itemViewHolder.expand(getContext());
            }
            expandedView = v;
            expandedPosition = position;
        }

        fullyShowHalfHiddenItem(position);

    }

    /**
     * Scroll the list to fully show half hidden list items
     * @param position
     */
    private void fullyShowHalfHiddenItem(int position) {
        getListView().smoothScrollToPosition(position);
        if (position == getListAdapter().getCount() - 1) {
            getListView().setSelection(position);
        }
    }



    @Override
    public void onBackPressed() {
        NavigationHelper.getInstance().popFragmentBackStackToMapFragment();

    }

}
