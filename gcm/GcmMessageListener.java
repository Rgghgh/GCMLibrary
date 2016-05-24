package com.rgghgh.gcmdemo.gcm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * May be extended and changed in manifest.
 */
public class GcmMessageListener extends GcmListenerService
{
    protected static final String TAG = "GcmMessageListener";

    protected static final String EXTRA_BUNDLE = "bundle";
    protected static final String EXTRA_FROM = "from";

    @Override
    public void onMessageReceived(String from, Bundle data)
    {
        Intent i = new Intent(getPackageName() + "/" + TAG);
        i.putExtra(EXTRA_BUNDLE, data);
        i.putExtra(EXTRA_FROM, from);
        sendBroadcast(i);
    }

}
