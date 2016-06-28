package com.oozeetech.iproperty_cms.utils;

import android.util.Log;


/**
 * Created by root on 17/6/16.
 */

public class Debug {

    public static final boolean DEBUG = true;

    public static void e(String tag, String msg) {
        if (DEBUG) {
            if (tag.isEmpty())
                tag = "unknown";

            Log.e(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            if (tag.isEmpty())
                tag = "unknown";
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            if (tag.isEmpty())
                tag = "unknown";
            Log.w(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            if (tag.isEmpty())
                tag = "unknown";
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            if (tag.isEmpty())
                tag = "unknown";
            Log.v(tag, msg);
        }
    }

}
