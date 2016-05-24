# Google Cloud Messaging Library
A library that makes Google Cloud Messaging simple.

see [Google Cloud Messaging](https://developers.google.com/cloud-messaging/).

### How to install:
1) Copy gcm package into your project and add [this config](https://developers.google.com/cloud-messaging/android/start) to the 'app' directory.

2) Permissions: (Notice where you should insert you package name)

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.GET_ACCOUNTS"/>
<uses-permission android:name="android.permission.WAKE_LOCK"/>
<permission android:name="<YOUR PACKAGE NAME>.permission.C2D_MESSAGE" android:protectionLevel="signature"/>
<uses-permission android:name="<YOUR PACKAGE NAME>.permission.C2D_MESSAGE"/>
<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE"/>
```


3) Registrations (in manifest file):

```xml
<service android:name=".gcm.GcmRegistrationService"/>
```
```xml
<service android:name=".gcm.GcmMessageListener" android:exported="false">
	<intent-filter>
		<action android:name="com.google.android.c2dm.intent.RECEIVE"/>
	</intent-filter>
</service>
```
```xml
<receiver android:name="com.google.android.gms.gcm.GcmReceiver" android:exported="true" android:permission="com.google.android.c2dm.permission.SEND">
  <intent-filter>
  	<action android:name="com.google.android.c2dm.intent.RECEIVE"/>
  
  	<category android:name="<YOUR PACKAGE NAME HERE>"/>
  </intent-filter>
</receiver>
```

And you should be good to go.

### How to use:

1) Implement the "GcmHandler" callback class and its methods. Can be implemented by an Activity.
```java
public class MainActivity implements GcmHandler
```

2) Create a "Gcm" instance:
```java
Gcm gcm = new Gcm(context, gcmHandler);
```

2.1) Make sure to set your API key: (the one you got from "How to install: #1)
```java
gcm.setApiKey(myApiKey);
```
This is important to do right after creating the instance.

3) To obtain the uniqe key of this device, this key is used to send messages to it use:
```java
gcm.requestDeviceToken();
```
The token will we requested and returned to the GcnHandler's `onGcmTokenReceived()` method.
The token should behave like a "Phone number" and generaly does not change.

4) Now you can send Messages!
```java
gcm.sendMessage(toDeviceToken, bundledMessage)
```

When the message is sent, the GcmHandler's `onGcmMessageSent()` is called.
Messages you receive will go to the GcmHandler's `onGcmMessageReceived()`.
