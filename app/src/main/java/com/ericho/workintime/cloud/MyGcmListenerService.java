package com.ericho.workintime.cloud;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by steve_000 on 2016/4/12.
 * for project Work In Time
 * package name com.ericho.workintime.cloud
 */
public class MyGcmListenerService extends GcmListenerService {
    final String t = "MyGcmListenerService";
    final String change_device_code = "change_device_code";
    @Override
    public void onMessageReceived(String from, Bundle data) {

        String deviceCode = data.getString("deviceCode");
        String title = data.getString("title");
        String content = data.getString("content");
        String collapse_key = data.getString("collapse_key");
        Log.d(t, "From: " + from);
        Log.d(t, "deviceCode= " + deviceCode);
        Log.d(t, "title= " + title);
        Log.d(t, "content= " +content);
        Log.d(t, "collapse_key= " + collapse_key);


        if (from.startsWith("/topics")) {
            // message received from some topic.
//            CloudMessage c = new CloudMessage(title,content,CloudMessage.type_group_message_all);
//            MyDao.getInstance().saveCloudMessage(c);
//            notifyUser(title, content, R.drawable.camera_icon, 19);
        } else {
            int mInt = process(collapse_key);
            // normal downstream message.
            switch (mInt){

            }




        }

    }
    /**
     *  @return 0 for default, other for known issue
     */

    private int process(String collapse_key) {
        return collapse_key.equals(change_device_code)? 1:0;
    }

    public void notifyUser(String title,String content,int iconInt,int notify_id){
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(iconInt);
        builder.setContentTitle(title);
        builder.setContentText(content);
        NotificationManager mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNM.notify(notify_id, builder.build());

    }

}


