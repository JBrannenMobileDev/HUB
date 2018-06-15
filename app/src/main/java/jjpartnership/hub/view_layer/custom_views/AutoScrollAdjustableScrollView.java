package jjpartnership.hub.view_layer.custom_views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by Jonathan on 6/15/2018.
 */

public class AutoScrollAdjustableScrollView extends NestedScrollView {

    private int scrollOffset = 0;

    public AutoScrollAdjustableScrollView (final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollOffset (final int scrollOffset) {
        this.scrollOffset = scrollOffset;
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen (final Rect rect) {
        // adjust by scroll offset
        int scrollDelta = super.computeScrollDeltaToGetChildRectOnScreen(rect);
        int newScrollDelta = (int) Math.signum(scrollDelta) * (scrollDelta + this.scrollOffset);
        return newScrollDelta;
    }
}
