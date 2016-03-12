package soen390.mapx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import soen390.mapx.R;

public class SplashScreen extends Activity{
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                //Executing main activity once timer ends
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // closing the splash screen activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
