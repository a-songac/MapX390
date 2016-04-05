package soen390.mapx.ui.view.holder;


import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arnaud.android.core.ui.BaseViewHolder;

import soen390.mapx.R;

public class SplashScreenActivityViewHolder extends BaseViewHolder{

    private Button retryButton;
    private ProgressBar progressBar;
    private TextView error;

    /**
     * Constructor
     * @param viewHolder
     */
    public SplashScreenActivityViewHolder(View viewHolder) {
        super(viewHolder);

        retryButton = (Button) viewHolder.findViewById(R.id.retry_button);
        progressBar = (ProgressBar) viewHolder.findViewById(R.id.progress_bar);
        error = (TextView) viewHolder.findViewById(R.id.error);
    }

    public Button getRetryButton() {
        return retryButton;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getError() {
        return error;
    }
}
