package com.app.ui.utile;

import android.util.Log;

/**
 * Created by guom on 2016/8/14.
 */
public class DLog {
    public static void e(Object tag, Object value) {
        Log.e(tag.toString(), value.toString());
    }
}
