package com.ericho.workintime;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.inputmethod.InputMethodManager;

import com.ericho.workintime.http.WebUrl;

import java.util.Calendar;

/**
 * <p>A class for usually access method.</p>
 * Created by EricH on 11/1/2016 in ${package_name}.
 */
public class Common {
    private static final String t ="Common";





    public static void hideSoftKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }catch (NullPointerException ne){
            //do nothings
        }
    }


    public static long getCurrentTime(){
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }

//    public static Collection<?> generate(Collection<Object> in, Class<?> cls){
//       Class.forName(cls.getClassLoader())
//    }
    public static void setGoogleToken(Context context,String input){
    context.getSharedPreferences(Key.pref_name,0)
            .edit().putString(Key.my_google_token,input)
            .apply();
}
    public static String getGoogleToken(Context context){
        return context.getSharedPreferences(Key.pref_name,0).getString(Key.my_google_token, "");
    }

    public static String getDeviceCode(Context context){
        return context.getSharedPreferences(Key.pref_name,0).getString(Key.device_code,"");
    }
    public static boolean haveNetwork(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static void setServerAddress(Context context,String input){
        context.getSharedPreferences(Key.pref_name,0)
                .edit().putString(Key.server_address,input)
                .apply();
    }
    public static String getServerAddress(Context context){
        return context.getSharedPreferences(Key.pref_name,0).getString(Key.server_address, WebUrl.default_server);
    }


}
