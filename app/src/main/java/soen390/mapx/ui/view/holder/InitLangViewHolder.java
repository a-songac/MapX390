package soen390.mapx.ui.view.holder;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.arnaud.android.core.ui.BaseViewHolder;
import soen390.mapx.R;

public class InitLangViewHolder extends BaseViewHolder{

    private ImageView logo_image;
    private Button fr_lang_button;
    private Button en_lang_button;


    public InitLangViewHolder(View viewHolder) {
        super(viewHolder);

        logo_image = (ImageView) viewHolder.findViewById(R.id.logo_image);
        fr_lang_button = (Button) viewHolder.findViewById(R.id.fr_button);
        en_lang_button = (Button) viewHolder.findViewById(R.id.en_button);
    }

    public Button getFr_lang_button(){
        return fr_lang_button;
    }

    public Button getEn_lang_button(){
        return en_lang_button;
    }

    public ImageView getLogo_image(){
        return logo_image;
    }
}
