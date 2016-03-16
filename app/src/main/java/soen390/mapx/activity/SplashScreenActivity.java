package soen390.mapx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import soen390.mapx.R;
import soen390.mapx.helper.PreferenceHelper;

public class SplashScreenActivity extends Activity{

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        PreferenceHelper.getInstance().init(this);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent intent = PreferenceHelper.getInstance().isLanguagePreferenceInit()?
                        new Intent(SplashScreenActivity.this, MainActivity.class):
                        new Intent(SplashScreenActivity.this, InitLanguageActivity.class);

                startActivity(intent);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
