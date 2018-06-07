package jjpartnership.hub.utils;


import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Jonathan on 6/7/2018.
 */

public class ActionBarUtil {
    public static void initActionBar(AppCompatActivity context, int actionBarColor, float elevation,
                                     int statusbarColor, boolean enableBackButton, String title){
        ActionBar actionbar = context.getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(enableBackButton);
        actionbar.setElevation(elevation);
        context.setTitle(title);
        actionbar.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(actionBarColor)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(context.getResources().getColor(statusbarColor));
        }
    }
}
