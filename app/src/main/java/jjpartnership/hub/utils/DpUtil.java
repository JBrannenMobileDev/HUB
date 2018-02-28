package jjpartnership.hub.utils;

import android.content.Context;

/**
 * Created by jbrannen on 9/14/17.
 */

public class DpUtil {
    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
