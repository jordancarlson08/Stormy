package me.jordancarlson.stormy.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import me.jordancarlson.stormy.R;

/**
 * Created by jcarlson on 5/21/15.
 */
public class ToolbarUtil {

    public static void setupToolbar(final ActionBarActivity activity) {
        // Set a toolbar to replace the action bar.
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);


        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.blue_700));
        }
        // else {
        // Implement this feature without material design
    }
}
