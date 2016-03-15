package soen390.mapx.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import soen390.mapx.LogUtils;
import soen390.mapx.R;
import soen390.mapx.database.DummyData;
import soen390.mapx.helper.ConstantsHelper;

/**
 * Media Player Activity
 */
public class MediaPlayerActivity extends AppCompatActivity {

    private VideoView videoView = null;
    private int stopPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_media_player);
        initActionBar();

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            int position = extras.getInt(ConstantsHelper.POI_MEDIA_START_POSITION_INTENT_EXTRA_KEY);
            initVideoPlayer(position);
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

    @Override
    public void onPause() {
        super.onPause();
        videoView.stopPlayback();
        stopPosition = videoView.getCurrentPosition();
        videoView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        videoView.seekTo(stopPosition);
        videoView.start(); //Or use resume() if it doesn't work. I'm not sure
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * Initialize action bar
     */
    private void initActionBar() {

        Toolbar toolbar = Toolbar.class.cast(findViewById(R.id.toolbar));
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        } else {
            LogUtils.error(getClass(), "onCreate", "Null toolbar");
        }
    }

    /**
     * Init video player
     * @param position
     */
    private void initVideoPlayer(int position) {
        Uri uri = null;
        try {
            JSONObject media = DummyData.dummyMedia().getJSONObject(position);
            videoView = (VideoView) findViewById(R.id.videoView);

            uri = Uri.parse("android.resource://" + getPackageName() +
                    "/" + getResources().getIdentifier(DummyData.dummyMedia().getJSONObject(position).getString("path"), "raw", getPackageName()));

            setActionBar(media.getString("title"));
        }catch (JSONException e) {
            e.printStackTrace();
        }

        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.start();

    }
}
