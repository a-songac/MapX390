package soen390.mapx.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

import soen390.mapx.R;
import soen390.mapx.database.DummyData;
import soen390.mapx.helper.ConstantsHelper;

/**
 * Created by ericl on 3/13/2016.
 */
public class MediaPlayerActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.media_player_fragment);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            int position = extras.getInt(ConstantsHelper.POI_MEDIA_START_POSITION_INTENT_EXTRA_KEY);
            Uri uri = null;
            VideoView videoView = null;

            try {
                JSONObject media = DummyData.dummyMedia().getJSONObject(position);
                videoView = (VideoView) findViewById(R.id.videoView);

                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.moeb_point5);

                /* USE THIS WHEN ALL VIDEO ARE CONVERTED TO MP4
                uri = Uri.parse("android.resource://" + getPackageName() +
                        "/R.raw." + DummyData.dummyMedia().getJSONObject(position).getString("path"));
                */

                setActionBar(media.getString("title"));
            }catch (JSONException e) {
                e.printStackTrace();
            }

            MediaController mediaController= new MediaController(this);
            mediaController.setAnchorView(videoView);

            videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);
            videoView.requestFocus();

            videoView.start();
        }

    }

    /**
     * Set action bar title
     */
    private void setActionBar(String title) {

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar){
            actionBar.setDisplayHomeAsUpEnabled(true);//TODO investigate duplication of the screen when setting the back navigation
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(title);
        }
    }
}
