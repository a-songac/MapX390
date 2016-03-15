package soen390.mapx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import soen390.mapx.R;
import soen390.mapx.helper.PreferenceHelper;

public class SplashScreen extends Activity{

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        PreferenceHelper.getInstance().init(this);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

               if (!PreferenceHelper.getInstance().isLanguagePreferenceInit()) {

                    PreferenceHelper.getInstance().completeLanguagePreferenceInit();

                    Intent i = new Intent(SplashScreen.this, InitLangActivity.class);
                    startActivity(i);
                }
                else
                {
                    Intent i2 = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i2);
                }


                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
