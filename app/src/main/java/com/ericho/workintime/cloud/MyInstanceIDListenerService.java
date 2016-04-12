package com.ericho.workintime.cloud;

import android.content.Intent;

import com.ericho.workintime.service.RegistrationIntentService;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by steve_000 on 2016/4/12.
 * for project Work In Time
 * package name com.ericho.workintime.cloud
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

    }

}
