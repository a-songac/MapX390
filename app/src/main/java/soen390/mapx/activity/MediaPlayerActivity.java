package soen390.mapx.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import org.json.JSONException;

import java.io.FileNotFoundException;

import soen390.mapx.R;
import soen390.mapx.database.DummyData;
import soen390.mapx.helper.ConstantsHelper;

/**
 * Created by ericl on 3/13/2016.
 */
public class MediaPlayerActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.media_player_fragment);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            int position = extras.getInt(ConstantsHelper.POI_MEDIA_START_POSITION_INTENT_EXTRA_KEY);

            VideoView videoView =(VideoView)findViewById(R.id.videoView);
            Uri uri = null;


            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.moeb_point5);


            MediaController mediaController= new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);
            videoView.requestFocus();


            videoView.start();

        }

    }
}
