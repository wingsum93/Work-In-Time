package com.ericho.workintime.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import com.ericho.workintime.Key;
import com.ericho.workintime.R;
import com.ericho.workintime.http.OkHttpUtil;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

/**
 * Created by steve_000 on 2016/4/12.
 * for project Work In Time
 * package name com.ericho.workintime.service
 */
public class RegistrationIntentService  extends IntentService {

    final String t = "RegistrationIntentSer";
    final String CLOUND_MESSAGE_GROUP_ALL = "all";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RegistrationIntentService() {
        super("Registration");
    }
    // ...

    @Override
    public void onHandleIntent(Intent intent) {
        // ...
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            subscribeTopics(token,CLOUND_MESSAGE_GROUP_ALL );
            getSharedPreferences(Key.pref_name,0).edit().putString(Key.my_google_token,token).apply();
            Log.i(t, "token= " + token);

            String device_code = getSharedPreferences(Key.pref_name, 0).getString(Key.device_code, "");
            if(device_code.length()==0){
                device_code = generateNewDeviceCode();
                getSharedPreferences(Key.pref_name, 0).edit().putString(Key.device_code,device_code).apply();
            }
            String str_res = OkHttpUtil.getInstance().registerToken(token,device_code);
            Log.w(t,"str_res="+str_res);
            JSONObject jsonObject = new JSONObject(str_res);
//            if(jsonObject.getBoolean(MessageReturnConstant.RESPONSE_STATUS)){
//                Log.w(t,"registration success");
//            }else {
//                Log.w(t,"registration error");
//            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();//connection isssue
        }

        // ...
    }

    // ...
    private void subscribeTopics(String token,String [] TOPICS) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    private void subscribeTopics(String token,String topic) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        pubSub.subscribe(token, "/topics/" + topic, null);

    }
    private String generateNewDeviceCode(){
        String ts = Long.toString(new Date().getTime());
        byte[] bb = ts.getBytes();
        String res = Base64.encodeToString(bb,Base64.DEFAULT);
        return res;
    }


}
