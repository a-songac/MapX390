package soen390.mapx.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import soen390.mapx.LogUtils;
import soen390.mapx.R;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.manager.ContentManager;
import soen390.mapx.model.ExpositionContent;


public class MediaPlayerActivity extends AppCompatActivity {

    private VideoView videoView = null;
    private int stopPosition = 0;
    private ActionBar actionBar;
    private Toolbar toolbar;

    private Handler handler = new Handler();

    private Runnable hideActionBarRunnable = new Runnable() {
        @Override
        public void run() {
            hideActionBar();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_media_player);
        initActionBar();

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            Long mediaId = extras.getLong(ConstantsHelper.POI_MEDIA_START_POSITION_INTENT_EXTRA_KEY);
            initVideoPlayer(mediaId);
        }
        hideActionBar();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction()== MotionEvent.ACTION_DOWN) {
            showActionBAr();

            handler.removeCallbacks(hideActionBarRunnable);
            handler.postDelayed(hideActionBarRunnable, 4000);
            return true;
        }
        return false;
    }

    /**
     * Set action bar title
     */
    private void setActionBar(String title) {

        actionBar = getSupportActionBar();
        if (null != actionBar){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(title);
        }
    }


    /**
     * Initialize action bar
     */
    private void initActionBar() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        toolbar = Toolbar.class.cast(findViewById(R.id.toolbar));
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        } else {
            LogUtils.error(getClass(), "onCreate", "Null toolbar");
        }
    }

    /**
     * Init video player
     * @param mediaId
     */
    private void initVideoPlayer(Long mediaId) {
        videoView = (VideoView) findViewById(R.id.videoView);
        ExpositionContent expositionContent = ExpositionContent.findById(ExpositionContent.class, mediaId);
        if (videoView != null) {

            Uri uri = Uri.parse(ContentManager.getMediaDirectoryPath() + expositionContent.getContent());

            setActionBar(expositionContent.getTitle());

            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);

            videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);
            videoView.start();
        }

    }

    private void hideActionBar() {
//        actionBar.hide();
        toolbar.setVisibility(View.GONE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showActionBAr() {
//        actionBar.show();
        toolbar.setVisibility(View.VISIBLE);
    }
}
