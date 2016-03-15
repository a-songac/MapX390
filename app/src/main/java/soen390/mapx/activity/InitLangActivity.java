package soen390.mapx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.View;

import soen390.mapx.R;
import soen390.mapx.helper.PreferenceHelper;
public class InitLangActivity extends Activity{

    private NavigationView navigationView;

    public void change_to_fr(View view){
        PreferenceHelper.getInstance().setLanguagePreference("fr");
        Intent intent = new Intent(InitLangActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void change_to_en(View view){
        PreferenceHelper.getInstance().setLanguagePreference("en");
        Intent intent = new Intent(InitLangActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.init_language_activity);
    }


}
