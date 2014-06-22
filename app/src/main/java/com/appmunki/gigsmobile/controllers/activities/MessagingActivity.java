package com.appmunki.gigsmobile.controllers.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.appmunki.gigsmobile.R;
import com.appmunki.gigsmobile.controllers.services.MessageService;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MessagingActivity extends Activity implements ServiceConnection {
    private MessageService.MessageServiceInterface mMessageService;
    private MessageClientListener mMessageListener = new MessageClientListener() {
        @Override
        public void onIncomingMessage(MessageClient messageClient, Message message) {
            //TODO: add message to the messageAdapter as incoming
            mMessageAdapter.add(message.getTextBody());
            mMessageAdapter.notifyDataSetChanged();
        }

        @Override
        public void onMessageSent(MessageClient messageClient, Message message, String s) {
            //TODO: add message to the messageadapter as outgoing
            mMessageAdapter.add(message.getTextBody());
            mMessageAdapter.notifyDataSetChanged();
        }

        @Override
        public void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo) {
            //TODO: add message to show messaging failure.
            Toast.makeText(getApplicationContext(),messageFailureInfo.getSinchError().getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo) {

        }

        @Override
        public void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> pushPairs) {

        }
    };

    //Adding the views the project
    @InjectView(R.id.messagerecipientEditText)
    EditText messagerecipientEditText;
    @InjectView(R.id.messagehistoryListView)
    ListView messagehistoryListView;
    @InjectView(R.id.messagebodyEditText)
    EditText messagebodyEditText;
    @InjectView(R.id.messagesendbutton)
    Button messagesendButton;
    private ArrayAdapter<String> mMessageAdapter;

    @OnClick(R.id.messagesendbutton)
    void sendMessage() {
        //Reset the edittest
        messagerecipientEditText.setError(null);
        messagebodyEditText.setError(null);

        //Store values
        String recipient = messagerecipientEditText.getText().toString();
        String messageBody = messagebodyEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(recipient)) {
            messagerecipientEditText.setError("No recipient added");
            focusView = messagerecipientEditText;
            cancel=true;
        }
        if (TextUtils.isEmpty(messageBody)) {
            messagebodyEditText.setError("No text message");
            focusView = messagebodyEditText;
            cancel=true;
        }

        if(cancel){
            focusView.requestFocus();
            return;
        }

        mMessageService.sendMessage(recipient, messageBody);
        messagebodyEditText.setText("");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.inject(this);
        bindMessagingService();

        mMessageAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        messagehistoryListView.setAdapter(mMessageAdapter);
    }

    /**
     * Binds the messaging service to this activity
     */
    private void bindMessagingService() {
        Intent intent = new Intent(this, MessageService.class);
        bindService(intent,this,BIND_AUTO_CREATE);


    }

    /**
     * unbinds the messaging service from the activity
     */
    private void unbindMessagingServces() {
        if(mMessageService!=null)
            mMessageService.removeMessageClientListener(mMessageListener);
        unbindService(this);
    }
    @Override
    protected void onDestroy() {
        unbindMessagingServces();
        super.onDestroy();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mMessageService = (MessageService.MessageServiceInterface)iBinder;
        mMessageService.addMessageClientListener(mMessageListener);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mMessageService = null;
    }
}
