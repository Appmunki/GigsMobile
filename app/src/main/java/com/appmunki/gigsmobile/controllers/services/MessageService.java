package com.appmunki.gigsmobile.controllers.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.appmunki.gigsmobile.helpers.Utils;
import com.appmunki.gigsmobile.models.User;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.WritableMessage;

import hugo.weaving.DebugLog;

public class MessageService extends Service {
    private static final String APP_KEY = "533a8603-f672-4f38-9faf-3a52acd55d2b";

    private static final String APP_SECRET = "TWj6uji3ykmwE2aM8RrO5w==";

    private static final String ENVIRONMENT = "sandbox.sinch.com";


    private static final String TAG = MessageService.class.getSimpleName();

    private final MessageServiceInterface mServiceInterface = new MessageServiceInterface();

    private SinchClient mSinchClient = null;

    private MessageClient mMessageClient = null;

    public class MessageServiceInterface extends Binder {

        public void sendMessage(String recipientUserId, String textBody) {
            Log.i(TAG,"sending message: "+textBody+" to "+recipientUserId);
            MessageService.this.sendMessage(recipientUserId, textBody);
        }

        public void addMessageClientListener(MessageClientListener listener) {
            MessageService.this.addMessageClientListener(listener);
        }

        public void removeMessageClientListener(MessageClientListener listener) {
            MessageService.this.removeMessageClientListener(listener);
        }
    }

    SinchClientListener mSinchClientListener = new SinchClientListener() {

        @DebugLog
        @Override
        public void onClientStarted(SinchClient sinchClient) {
            sinchClient.startListeningOnActiveConnection();
            mMessageClient = sinchClient.getMessageClient();

            /*Intent intent = new Intent(getApplicationContext(), MessagingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
        }

        @Override
        public void onClientStopped(SinchClient sinchClient) {
            Log.d(TAG, "SinchClient stopped");
            mSinchClient = null;
        }

        @Override
        public void onClientFailed(SinchClient sinchClient, SinchError error) {
            Log.e(TAG, "SinchClient error: " + error);
            Toast.makeText(getApplicationContext(), String.format("SinchClient error: %s", error), Toast.LENGTH_LONG).show();
            mSinchClient = null;
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {

        }

        @Override
        public void onLogMessage(int level, String area, String message) {
            switch (level) {
                case Log.DEBUG:
                    Log.d(area, message);
                    break;
                case Log.ERROR:
                    Log.e(area, message);
                    break;
                case Log.INFO:
                    Log.i(area, message);
                    break;
                case Log.VERBOSE:
                    Log.v(area, message);
                    break;
                case Log.WARN:
                    Log.w(area, message);
                    break;
            }
        }
    };
    public MessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mServiceInterface;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopSinchClient();
        stopSelf();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        stopSinchClient();
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        User user = intent.getParcelableExtra(Utils.ARG_USER);
        String username = user.getEmail();

        if (username != null && !isSinchClientStarted()) {
            startSinchClient(username);
        }

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * Checks to see is the message client is started
     * @return
     */
    private boolean isSinchClientStarted() {
        return mSinchClient != null && mSinchClient.isStarted();
    }


    @DebugLog
    public void sendMessage(String recipientUserId, String textBody) {

        if (mMessageClient != null) {
            WritableMessage message = new WritableMessage(recipientUserId, textBody);
            mMessageClient.send(message);
        }
    }

    @DebugLog
    public void addMessageClientListener(MessageClientListener listener) {
        if (mMessageClient != null) {
            mMessageClient.addMessageClientListener(listener);
        }
    }

    public void removeMessageClientListener(MessageClientListener listener) {
        if (mMessageClient != null) {
            mMessageClient.removeMessageClientListener(listener);
        }
    }

    public void startSinchClient(String userName) {
        mSinchClient = Sinch.getSinchClientBuilder().context(this).userId(userName).applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET).environmentHost(ENVIRONMENT).build();

        mSinchClient.addSinchClientListener(mSinchClientListener);

        mSinchClient.setSupportMessaging(true);
        mSinchClient.setSupportCalling(false);
        mSinchClient.setSupportActiveConnectionInBackground(true);

        mSinchClient.checkManifest();
        mSinchClient.start();
    }

    public void stopSinchClient() {
        if (isSinchClientStarted()) {
            mSinchClient.stop();
            mSinchClient.removeSinchClientListener(mSinchClientListener);
        }
        mSinchClient = null;
    }

}
