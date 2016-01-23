package com.arnaud.android.core.ui;

/**
 * Class to implement base view binder
 */
public class BaseViewBinder {

    /**
     * holder of the view
     */
    private BaseViewHolder viewHolder;

    /**
     * Constructor
     * @param viewHolder : holder of the view
     */
    public BaseViewBinder(BaseViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    public BaseViewHolder getViewHolder() {
        return viewHolder;
    }
}

