package com.nisha.android.friendlychat.FirebaseClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nisha.android.friendlychat.Activities.ContactListActivity;
import com.nisha.android.friendlychat.Objects.TokenContact;
import com.nisha.android.friendlychat.Services.MyFirebaseMessagingService;

public class FirebaseInitialization {

    public static void sendRegistrationToServer(String token){
        FirebaseDatabase mFirebaseDatabase=FirebaseDatabase.getInstance();
        String mUserContact=ContactListActivity.mUserContact;
        DatabaseReference mTokenDatabaseReference=mFirebaseDatabase.getReference().child("users").child(mUserContact);
        TokenContact tc=new TokenContact(mUserContact,token);
        mTokenDatabaseReference.setValue(tc);
    }
}
