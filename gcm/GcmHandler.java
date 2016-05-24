package com.rgghgh.gcmdemo.gcm;

import android.os.Bundle;

public interface GcmHandler
{
    void onGcmError(Exception e);

    void onGcmTokenReceived(String deviceToken);

    void onGcmMessageSent(boolean successful, String response);

    void onGcmMessageReceived(String from, Bundle data);
}
