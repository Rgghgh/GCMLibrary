package com.rgghgh.gcmdemo.gcm;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class GcmSender extends Thread
{
    private String apiKey;
    private JSONArray targetTokens;
    private JSONObject bundle;

    private GcmHandler callback;

    private Handler handler;

    protected static void send(String apiKey, String targetToken, JSONObject bundle, GcmHandler handler)
    {
        GcmSender thread = new GcmSender(apiKey, targetToken, bundle, handler);
        thread.start();
    }

    private GcmSender(String apiKey, String targetToken, JSONObject bundle, GcmHandler handler)
    {
        this.apiKey = apiKey;
        this.targetTokens = new JSONArray();
        this.targetTokens.put(targetToken);
        this.bundle = bundle;
        this.callback = handler;
    }

    @Override
    public void run()
    {
        try
        {
            JSONObject data = new JSONObject();
            data.put("registration_ids", this.targetTokens);
            data.put("data", this.bundle);

            String post = data.toString();
            Log.d("GcmSender", post);

            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Authorization", "key=" + this.apiKey);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeBytes(post);
            dos.close();
            os.close();

            StringBuilder ans = new StringBuilder();
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null)
                ans.append(line);

            reader.close();
            isr.close();
            is.close();
            conn.disconnect();

            String rawResponse = ans.toString();
            JSONObject response = new JSONObject(rawResponse);
            boolean successful = response.getInt("success") != 0;
            this.callback.onGcmMessageSent(successful, rawResponse);
        }
        catch (Exception e)
        {
            this.callback.onGcmError(e);
        }
    }
}
