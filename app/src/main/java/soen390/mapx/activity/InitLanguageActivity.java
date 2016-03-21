package soen390.mapx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import soen390.mapx.R;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.helper.PreferenceHelper;
import soen390.mapx.ui.view.holder.InitLanguageActivityViewHolder;

public class InitLanguageActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_language_activity);

        InitLanguageActivityViewHolder viewHolder =
                new InitLanguageActivityViewHolder(findViewById(R.id.root));

        viewHolder.getFrenchLanguageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage(ConstantsHelper.PREF_LANGUAGE_FRENCH);
            }
        });

        viewHolder.getEnglishLanguageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage(ConstantsHelper.PREF_LANGUAGE_ENGLISH);
            }
        });

    }

    /**
     * Set language of system
     * @param language
     */
    private void setLanguage(String language) {
        PreferenceHelper.getInstance().completeLanguagePreferenceInit();
        PreferenceHelper.getInstance().setLanguagePreference(language);
        Intent intent = new Intent(InitLanguageActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
