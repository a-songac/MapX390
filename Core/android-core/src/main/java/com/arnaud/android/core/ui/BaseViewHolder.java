package com.arnaud.android.core.ui;

import android.view.View;

/**
 * Class to implement base view holder
 */
public class BaseViewHolder {

    /**
     * Base view holder
     */
    private View viewHolder;

    /**
     * Constructor
     * @param viewHolder : base view holder
     */
    public BaseViewHolder(View viewHolder) {
        this.viewHolder = viewHolder;
    }

    /**
     * Get base view holder
     * @return View : Base view holder
     */
    public View getViewHolder() {
        return viewHolder;
    }
}

