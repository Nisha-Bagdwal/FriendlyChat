package com.nisha.android.friendlychat.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nisha.android.friendlychat.Adapters.ContactAdapter;
import com.nisha.android.friendlychat.Database.OfflineDatabse;
import com.nisha.android.friendlychat.FirebaseClasses.FirebaseInitialization;
import com.nisha.android.friendlychat.Objects.FriendlyContact;
import com.nisha.android.friendlychat.Objects.FriendlyMessage;
import com.nisha.android.friendlychat.Objects.TokenContact;
import com.nisha.android.friendlychat.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private static final String TAG = "ContactListActivity";

    public static final String ANONYMOUS = "anonymous";
    private static boolean permission=true;
    ListView mContactListView;
    ProgressBar mProgressBar;
    ContactAdapter mContactAdapter;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public static final int RC_SIGN_IN=1;
    ArrayList<FriendlyContact> phoneNo;
    HashSet<FriendlyContact> phoneNoOnApp;
    private SharedPreferences sharedpreferences;

    public static String mUserContact;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contact_list);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        //FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        //mFirebaseDatabase.setPersistenceEnabled(true);

        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseStorage=FirebaseStorage.getInstance();

        mUsersDatabaseReference=mFirebaseDatabase.getReference().child("users");
        mUsersDatabaseReference.keepSynced(true);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mContactListView=(ListView)findViewById(R.id.contactListView);

        List<FriendlyContact> friendlyContacts = new ArrayList<>();
        mContactAdapter=new ContactAdapter(this,R.layout.contact,friendlyContacts);
        mContactListView.setAdapter(mContactAdapter);

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user=firebaseAuth.getCurrentUser();

                if (user != null) {
                    onSignedInInitialize(user.getPhoneNumber());
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        permission=false;
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                        //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                    }
                    if(permission==true) {
                        onSignedOutCleanUp();
                        startActivity(new Intent(ContactListActivity.this,SignUpActivity.class));
                        /*List<AuthUI.IdpConfig> providers = Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build());

    // Create and launch sign-in intent
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setAvailableProviders(providers)
                                        .build(),
                                RC_SIGN_IN);*/
                    }else{
                        Toast.makeText(ContactListActivity.this,"Grant permission to read contacts",Toast.LENGTH_LONG);

                    }

                }
            }
        };
        //showContacts();
    }


    private void showContacts() {
        OfflineDatabse odb=new OfflineDatabse(this);
        odb.open(null);
        ArrayList<FriendlyContact> totalContactsInDB=odb.getUserData();

        Collections.sort(totalContactsInDB);
        for(FriendlyContact fc: totalContactsInDB){
            Log.i("inDB",fc.getContact()+" "+fc.getName());
            mContactAdapter.add(fc);
        }
        odb.close();
    }


    private void adToOfflineDB(String contact, String name) {

        OfflineDatabse odb=new OfflineDatabse(this);
        odb.open(null);
        odb.createEntryInUserTable(contact,name);
        odb.close();
    }

    private void getContacts() {
        // Check the SDK version and whether the permission is already granted or not
             phoneNo= new ArrayList<FriendlyContact>();
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
            while (phones.moveToNext())
            {
                String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_SHORT).show();
                FriendlyContact contact = new FriendlyContact(name, phoneNumber);
                phoneNo.add(contact);
            }
            phones.close();
            Collections.sort(phoneNo);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permission=true;
                // Permission is granted
            } else {
                permission=false;
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                Toast.makeText(ContactListActivity.this,"Signed in!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ContactListActivity.this,"Signed in cancelled",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void onSignedOutCleanUp() {
        mUserContact=ANONYMOUS;
        mContactAdapter.clear();
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if(mChildEventListener!=null) {
            mUsersDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener=null;
        }
    }

    private void onSignedInInitialize(String displayName) {
        mUserContact=displayName;
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("mUserToken",token);
                        editor.commit();
                        FirebaseInitialization.sendRegistrationToServer(token);
                        Log.i("token send to server","true");
                    }
                });
        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("mUserContact",mUserContact);
        editor.commit();
        getContacts();
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if(mChildEventListener==null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    TokenContact tokenContact = dataSnapshot.getValue(TokenContact.class);
                    phoneNoOnApp=new HashSet<FriendlyContact>();
                    for(FriendlyContact contact : phoneNo) {
                        //Log.i("testing","checkiff"+ PhoneNumberUtils.compare(friendlyContact.getContact(),contact.getContact())+" "+contact.getName());
                        if (PhoneNumberUtils.compare(tokenContact.getContact(),contact.getContact())){
                            phoneNoOnApp.add(new FriendlyContact(contact.getName(),tokenContact.getContact(),tokenContact.getToken()));
                            //adToOfflineDB(friendlyContact.getContact(),contact.getName());
                        }
                    }
                    for(FriendlyContact fc: phoneNoOnApp){
                        if(!fc.getContact().equals(mUserContact))
                            mContactAdapter.add(fc);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    TokenContact tokenContact = dataSnapshot.getValue(TokenContact.class);
                    phoneNoOnApp=new HashSet<FriendlyContact>();
                    for(FriendlyContact contact : phoneNo) {
                        //Log.i("testing","checkiff"+ PhoneNumberUtils.compare(friendlyContact.getContact(),contact.getContact())+" "+contact.getName());
                        if (PhoneNumberUtils.compare(tokenContact.getContact(),contact.getContact())){
                            phoneNoOnApp.add(new FriendlyContact(contact.getName(),tokenContact.getContact(),tokenContact.getToken()));
                            //adToOfflineDB(friendlyContact.getContact(),contact.getName());
                        }
                    }
                    for(FriendlyContact fc: phoneNoOnApp){
                        if(!fc.getContact().equals(mUserContact))
                            mContactAdapter.add(fc);
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mUsersDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener!=null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        mContactAdapter.clear();
        detachDatabaseReadListener();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                //AuthUI.getInstance().signOut(this);
                startActivity(new Intent(ContactListActivity.this,SignUpActivity.class));
                finish();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
