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
import soen390.mapx.database.DummyData;
import soen390.mapx.helper.ActionBarHelper;
import soen390.mapx.helper.NavigationHelper;
import soen390.mapx.ui.adapter.StorylineListAdapter;
import soen390.mapx.ui.view.holder.StorylineListItemViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StorylineListFragment extends ListFragment implements IBaseFragment {

    public static final int COLLAPSED_DESCRIPTION_MAX_LINES = 4;
    public static final int EXPANDED_DESCRIPTION_MAX_LINES = 10;
    private View expandedView = null;
    private StorylineListItemViewHolder itemViewHolder;

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

//        StorylineListAdapter listAdapter = new StorylineListAdapter(getActivity(), Storyline.listAll(Storyline.class));
        StorylineListAdapter listAdapter = new StorylineListAdapter(getActivity(), DummyData.getStorylinesList());
        setListAdapter(listAdapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != expandedView && expandedView != v) {
            collapseItem((StorylineListItemViewHolder) expandedView.getTag());
        }


        Object tag = v.getTag();
        if (null != tag) {

            itemViewHolder = (StorylineListItemViewHolder) tag;
            if (!isExpanded(itemViewHolder)) {
                expandItem(itemViewHolder);
            } else {
                collapseItem(itemViewHolder);
            }
            expandedView = v;

        }

    }



    @Override
    public void onBackPressed() {
        NavigationHelper.getInstance().navigateToLastFragment();

    }

    public void expandItem(StorylineListItemViewHolder viewHolder) {

        viewHolder.getDescription().setMaxLines(EXPANDED_DESCRIPTION_MAX_LINES);
        viewHolder.getStartButton().setVisibility(View.VISIBLE);
        viewHolder.getViewHolder().setBackgroundColor(getResources().getColor(R.color.storyline_clicked_card));

    }

    public void collapseItem(StorylineListItemViewHolder viewHolder) {

        if (viewHolder != null) {
            viewHolder.getStartButton().setVisibility(View.GONE);
            viewHolder.getDescription().setMaxLines(COLLAPSED_DESCRIPTION_MAX_LINES);
            viewHolder.getViewHolder().setBackgroundColor(getResources().getColor(R.color.white));
        }
    }


    public boolean isExpanded(StorylineListItemViewHolder v) {

        return v.getStartButton().getVisibility() == View.VISIBLE;

    }
}
