package soen390.mapx.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.List;

import soen390.mapx.R;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.manager.MapManager;
import soen390.mapx.model.ExpositionContent;
import soen390.mapx.model.Node;
import soen390.mapx.ui.adapter.ImagePagerAdapter;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {


    private List<ExpositionContent> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            Long poiId = extras.getLong(ConstantsHelper.POI_ID_INTENT_EXTRA_KEY);
            Node poi = Node.findById(Node.class, poiId);
            int startPosition = extras.getInt(ConstantsHelper.POI_IMAGE_START_POSITION_INTENT_EXTRA_KEY);

            Long storylineId = MapManager.isStorylineMode()?
                    MapManager.getCurrentStoryline().getId():
                    -1L;
            this.images = poi.getContent(storylineId, ExpositionContent.IMAGE_TYPE);

            setActionBar(images.get(startPosition).getTitle());

            initPager(this.images, startPosition);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Set action bar title
     */
    private void setActionBar(String title) {

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(title);
        }
    }

    /**
     * Set view pager
     * @param images
     */
    private void initPager(final List<ExpositionContent> images, int startPosition) {

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final ImagePagerAdapter adapter = new ImagePagerAdapter(
                getSupportFragmentManager(),
                images);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(startPosition);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle(FullscreenActivity.this.images.get(position).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Do nothing
            }
        });

    }
}
