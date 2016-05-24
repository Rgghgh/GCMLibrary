package com.rgghgh.gcmdemo.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.rgghgh.gcmdemo.R;
import com.rgghgh.gcmdemo.ui.MainActivity;

public final class GcmRegistrationService extends IntentService
{
    private static final String TAG = "GcmRegistrationService";
    public static final String EXTRA_TOKEN = "ExtraToken";

    protected GcmRegistrationService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        try
        {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.d(TAG, token);

            Intent i = new Intent(getPackageName() + "/GcmRegistrationService");
            i.putExtra(EXTRA_TOKEN, token);
            sendBroadcast(i);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
