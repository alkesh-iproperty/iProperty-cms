package com.oozeetech.iproperty_cms.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by root on 1/3/16.
 */
public class NetworkChangeReciever extends BroadcastReceiver {

    private static boolean firstConnect = true;
    Context context;

    @Override
    public void onReceive(final Context context, final Intent intent) {


        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
            if (firstConnect) {
                this.context = context;


            } else {

                Debug.e("network changed", "Network Gone");
            }

            firstConnect = false;

        } else {
            firstConnect = true;
        }

    }

    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        if (serviceManager.isNetworkAvailable()) {
            return true;
        } else {
            return false;
        }


    }

    public class ServiceManager extends ContextWrapper {

        public ServiceManager(Context base) {
            super(base);
        }

        public boolean isNetworkAvailable() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
            return false;
        }

    }

}
