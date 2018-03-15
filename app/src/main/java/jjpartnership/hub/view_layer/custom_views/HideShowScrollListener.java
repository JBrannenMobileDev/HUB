package jjpartnership.hub.view_layer.custom_views;

import android.support.v7.widget.RecyclerView;

import jjpartnership.hub.utils.DpUtil;

/**
 * Created by Jonathan on 3/14/2018.
 */
public abstract class HideShowScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = 500;
    private int scrolledDistance = 0;
    private boolean controlsVisible = false;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }

        if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
            scrolledDistance += dy;
        }
    }

    public abstract void onHide();

    public abstract void onShow();

}
