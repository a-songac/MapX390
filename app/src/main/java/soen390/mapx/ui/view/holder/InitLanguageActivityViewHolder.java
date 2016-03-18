package soen390.mapx.ui.view.holder;


import android.view.View;
import android.widget.Button;

import com.arnaud.android.core.ui.BaseViewHolder;

import soen390.mapx.R;

public class InitLanguageActivityViewHolder extends BaseViewHolder{

    private Button frenchLanguageButton;
    private Button englishLanguageButton;

    /**
     * Constructor
     * @param viewHolder
     */
    public InitLanguageActivityViewHolder(View viewHolder) {
        super(viewHolder);

        frenchLanguageButton = (Button) viewHolder.findViewById(R.id.fr_button);
        englishLanguageButton = (Button) viewHolder.findViewById(R.id.en_button);
    }

    public Button getFrenchLanguageButton(){
        return frenchLanguageButton;
    }

    public Button getEnglishLanguageButton(){
        return englishLanguageButton;
    }

}
