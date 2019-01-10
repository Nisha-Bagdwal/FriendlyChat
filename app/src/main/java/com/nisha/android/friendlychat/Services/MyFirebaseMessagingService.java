package com.nisha.android.friendlychat.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nisha.android.friendlychat.Objects.TokenContact;

import static com.firebase.ui.auth.ui.phone.SubmitConfirmationCodeFragment.TAG;
import static com.nisha.android.friendlychat.FirebaseClasses.FirebaseInitialization.sendRegistrationToServer;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("onMessageReceived", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("remoteMessage", "Message data payload: " + remoteMessage.getData());

            SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("mFriendToken",remoteMessage.getData().get("fromContact"));
            editor.putString("mFriendContact",remoteMessage.getData().get("fromToken"));
            editor.commit();
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("remoteMessage", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    @Override
    public void onNewToken(String s) {
        Log.d("tokenmessage", "Refreshed token: " + s);
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //preferences.edit().putString(SyncStateContract.Constants.FIREBASE_TOKEN, s).apply();
        //sendRegistrationToServer(s);
    }
}
