package com.oozeetech.iproperty_cms.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by root on 17/6/16.
 */
public class Constants {

    public static final String PACKAGE_NAME = "com.oozeetech.iproperty_cms";

    public static final String API_Key = "E76042EE-E9E8-441E-8F4C-3308A4E96D68";

    public static final String FOLDER_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + ".iProperty";

    public static final int TIMEOUT = 5 * 60 * 1000;
    public static final String CONDO = "condo";
    public static final String GCM_KEY = "fcm_key";
    public static final String LOGIN_INFO = "login_info";
    public static final String USERNAME = "username";
    public static int MAX_DISTANCE = 2000;

    public static String DEVICE_ID = "APA91bFzqHbowAiqI6C17BRbdQ8Ndy8EL7Luk9OZ0kObM9Bav_uMWfuHI3diw7-mryYZpec6R9rxsYqxhAWC28_m2sOMIdpHTj-X3ohf08BJoSkcFCQlHFogAoDCNubbr9mHfsLqmzEt";

    public static final String SENDER_ID = "1097226950144";




}