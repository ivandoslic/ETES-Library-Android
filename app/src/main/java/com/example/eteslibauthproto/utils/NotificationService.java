package com.example.eteslibauthproto.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        FirestoreClass.assignNotificationTokenToUser(s);
        Log.d("TOKEN_REG", "onNewToken: new token generated");
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
