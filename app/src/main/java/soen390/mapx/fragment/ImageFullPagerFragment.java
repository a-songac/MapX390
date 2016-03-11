package soen390.mapx.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arnaud.android.core.fragment.IBaseFragment;

import soen390.mapx.R;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.manager.ContentManager;

/**
 * Image full fragment
 */
public class ImageFullPagerFragment extends Fragment implements IBaseFragment {


    /**
     * Create new instance of Profile fragment
     * @return ProfileFragment : ProfileFragment new instance
     */
    public static ImageFullPagerFragment newInstance(String path, String name) {

        Bundle arguments = new Bundle();
        arguments.putString(ConstantsHelper.IMAGE_FULL_FRAGMENT_PATH, path);
        arguments.putString(ConstantsHelper.IMAGE_FULL_FRAGMENT_CAPTION, name);
        ImageFullPagerFragment imageFullPagerFragment = new ImageFullPagerFragment();
        imageFullPagerFragment.setArguments(arguments);
        return imageFullPagerFragment;

    }


    public ImageFullPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.image_full_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {

            String imagePath = args.getString(ConstantsHelper.IMAGE_FULL_FRAGMENT_PATH);

            View root = getView();

            if (null != root) {

                ImageView imageView = (ImageView) root.findViewById(R.id.full_image_view);
                int imageResourceId = ContentManager.getImageResourceId(getContext(), imagePath);
                if (0 != imageResourceId) {
                    imageView.setImageResource(imageResourceId);
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
    }

}
