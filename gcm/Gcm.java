package com.rgghgh.gcmdemo.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.rgghgh.gcmdemo.exceptions.GcmApiKeyNotDefinedException;

import org.json.JSONObject;

/**
 * Helper Class for implementing Google Cloud Messaging Communication by Tomer Gandler.
 * <p/>
 * Todo:
 * - Add permissions and definitions to Manifest.
 * - set up the files needed
 */
public class Gcm
{
    private Context context;
    private GcmHandler handler;
    private RegistrationReceiver registerReceiver;
    private MessageReceiver messageReceiver;

    private String token;
    private String apiKey;

    /**
     * Created a new Google Cloud Messaging helper object.
     * @param context the context to use
     * @param handler the callback class
     */
    public Gcm(Context context, GcmHandler handler)
    {
        this.context = context;
        this.handler = handler;
        this.registerReceiver = new RegistrationReceiver();
        this.messageReceiver = new MessageReceiver();
        this.context.registerReceiver(this.messageReceiver, new IntentFilter(this.context.getPackageName() + "/" + GcmMessageListener.TAG));
    }

    @Override
    protected void finalize() throws Throwable
    {
        this.context.unregisterReceiver(this.messageReceiver);
        super.finalize();
    }

    /**
     * Requests this devices unique token.<br>
     * The token can be obtained, when ready, by using getToken() or on callback from GcmHandler's onGcmTokenReceived()
     */
    public void requestDeviceToken()
    {
        Intent intent = new Intent(this.context, GcmRegistrationService.class);
        this.context.startService(intent);
        this.context.registerReceiver(this.registerReceiver, new IntentFilter(this.context.getPackageName() + "/GcmRegistrationService"));
    }

    private void setToken(String token)
    {
        this.token = token;
        this.handler.onGcmTokenReceived(token);
    }

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    public String getToken()
    {
        return this.token;
    }

    public void sendMessage(String toToken, JSONObject bundle)
    {
        if (this.apiKey == null)
            throw new GcmApiKeyNotDefinedException("use setApiKey(String key) to define");
        GcmSender.send(this.apiKey, toToken, bundle, this.handler);
    }

    private class MessageReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle bundle = intent.getBundleExtra(GcmMessageListener.EXTRA_BUNDLE);
            String from = intent.getStringExtra(GcmMessageListener.EXTRA_FROM);
            Gcm.this.handler.onGcmMessageReceived(from, bundle);
        }
    }

    private class RegistrationReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Gcm.this.setToken(intent.getStringExtra(GcmRegistrationService.EXTRA_TOKEN));
            Gcm.this.context.unregisterReceiver(this);
        }
    }
}
