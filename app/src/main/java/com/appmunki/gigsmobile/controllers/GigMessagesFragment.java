package com.appmunki.gigsmobile.controllers;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appmunki.gigsmobile.R;
import com.appmunki.gigsmobile.models.User;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GigMessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class GigMessagesFragment extends Fragment {
    // the fragment initialization parameters
    private static final String ARG_FROM_USER = "from";
    private static final String ARG_TO_USER = "to";

    // users in message
    private User mFromUser;
    private User mToUser;
    private SinchClientListener clientListener = new SinchClientListener() {
        @Override
        public void onClientStarted(SinchClient sinchClient) {
            sinchClient.startListeningOnActiveConnection();;
        }

        @Override
        public void onClientStopped(SinchClient sinchClient) {

        }

        @Override
        public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {

        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {

        }

        @Override
        public void onLogMessage(int i, String s, String s2) {

        }
    };
    private MessageClientListener messageClientListener = new MessageClientListener() {
        @Override
        public void onIncomingMessage(MessageClient messageClient, Message message) {

        }

        @Override
        public void onMessageSent(MessageClient messageClient, Message message, String s) {

        }

        @Override
        public void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo) {

        }

        @Override
        public void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo) {

        }

        @Override
        public void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> pushPairs) {

        }
    };


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param fromUser uer sending messages.
     * @param toUser user receiving messages.
     * @return A new instance of fragment MessageFragment.
     */
    public static GigMessagesFragment newInstance(User fromUser, User toUser) {
        GigMessagesFragment fragment = new GigMessagesFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_FROM_USER, fromUser);
        args.putParcelable(ARG_TO_USER, toUser);
        fragment.setArguments(args);
        return fragment;
    }
    public GigMessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFromUser = getArguments().getParcelable(ARG_FROM_USER);
            mToUser = getArguments().getParcelable(ARG_TO_USER);
        }


    }
    private void sendMessage(User fromUser, User toUser, String message){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Instantiate a SinchClient using the SinchClientBuilder.
        android.content.Context context = getActivity();
        SinchClient sinchClient = Sinch.getSinchClientBuilder().context(context)
                .applicationKey("533a8603-f672-4f38-9faf-3a52acd55d2b")
                .applicationSecret("TWj6uji3ykmwE2aM8RrO5w==")
                .environmentHost("sandbox.sinch.com")
                .userId(String.valueOf(mFromUser.getId()))
                .build();

        //starting the setting for the application
        //also checking the manifest file
        sinchClient.setSupportMessaging(true);
        sinchClient.setSupportCalling(true);
        sinchClient.setSupportActiveConnectionInBackground(true);
        sinchClient.setSupportPushNotifications(true);
        sinchClient.checkManifest();

        //add state listener and starting the application
        sinchClient.addSinchClientListener(clientListener);
        sinchClient.start();

        //add state listener and listen for messages
        MessageClient messageClient = sinchClient.getMessageClient();


        messageClient.addMessageClientListener(messageClientListener);
    }


}
