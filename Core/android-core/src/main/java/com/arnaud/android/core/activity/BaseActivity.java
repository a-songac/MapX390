package com.arnaud.android.core.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.arnaud.android.core.fragment.IBaseFragment;

import java.util.List;

/**
 * Class to implement base activity with fragment transaction on back pressed
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {

        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible()) {
                try {

                    IBaseFragment.class.cast(fragment).onBackPressed();

                } catch (ClassCastException e) {

                    super.onBackPressed();
                }

                break;
            }
        }

    }
}
