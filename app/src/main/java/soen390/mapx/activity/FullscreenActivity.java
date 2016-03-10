package soen390.mapx.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import soen390.mapx.R;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.manager.ContentManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        setActionBar();
        setImage();

    }

    /**
     * Set image
     */
    private void setImage() {
        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        String imagePath = getIntent().getExtras().getString(ConstantsHelper.POI_IMAGE_PATH_INTENT_EXTRA_KEY);

        int imageResourceId = ContentManager.getImageResourceId(this, imagePath);
        if (0 != imageResourceId) {
            imageView.setImageResource(imageResourceId);
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
            actionBar.setDisplayHomeAsUpEnabled(true);//TODO investigate duplication of the screen when setting the back navigation
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(title);
        }
    }

}
