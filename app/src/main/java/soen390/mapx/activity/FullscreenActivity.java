package soen390.mapx.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import soen390.mapx.R;
import soen390.mapx.database.DummyData;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.model.Node;
import soen390.mapx.ui.adapter.ImagePagerAdapter;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            Long poiId = extras.getLong(ConstantsHelper.POI_ID_INTENT_EXTRA_KEY);
            Node poi = Node.findById(Node.class, poiId);
            int startPosition = extras.getInt(ConstantsHelper.POI_IMAGE_START_POSITION_INTENT_EXTRA_KEY);
            setActionBar();
            initPager(DummyData.dummyImages(), startPosition);//TODO get poi images

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set action bar title
     */
    private void setActionBar() {

        String title = getIntent().getExtras().getString(ConstantsHelper.POI_IMAGE_CAPTION_INTENT_EXTRA_KEY);

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(title);
        }
    }

    /**
     * Set view pager
     * @param imagePaths
     */
    private void initPager(String[] imagePaths, int startPosition) {

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final ImagePagerAdapter adapter = new ImagePagerAdapter(
                getSupportFragmentManager(),
                imagePaths);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(startPosition);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //TODO set action bar title with image caption
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
