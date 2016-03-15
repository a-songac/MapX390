package soen390.mapx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import soen390.mapx.R;
import soen390.mapx.helper.ConstantsHelper;
import soen390.mapx.helper.PreferenceHelper;
public class InitLangActivity extends Activity{

    private Button englishButton;
    private Button frenchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.init_language_activity);

        englishButton = Button.class.cast(findViewById(R.id.en_button));
        frenchButton = Button.class.cast(findViewById(R.id.fr_button));

        frenchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage(ConstantsHelper.PREF_LANGUAGE_FRENCH);
            }
        });

        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage(ConstantsHelper.PREF_LANGUAGE_ENGLISH);
            }
        });

    }

    private void setLanguage(String language) {
        PreferenceHelper.getInstance().completeLanguagePreferenceInit();
        PreferenceHelper.getInstance().setLanguagePreference(language);
        Intent intent = new Intent(InitLangActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
