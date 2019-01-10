package com.nisha.android.friendlychat.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nisha.android.friendlychat.Activities.ContactListActivity;
import com.nisha.android.friendlychat.Activities.MainActivity;
import com.nisha.android.friendlychat.Objects.FriendlyContact;
import com.nisha.android.friendlychat.R;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<FriendlyContact> {

    Context context;

    public ContactAdapter(@NonNull Context context, int resource, @NonNull List<FriendlyContact> objects) {
        super(context, resource, objects);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.contact, parent, false);

        TextView contactName=(TextView)convertView.findViewById(R.id.contacttextview);

        final FriendlyContact contact = getItem(position);

        contactName.setText(contact.getName()+" "+contact.getContact());
        contactName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contactNo=contact.getContact();

                Bundle bun=new Bundle();
                bun.putString("mFriendToken",contact.getFriendToken());
                bun.putString("mFriendContact",contactNo);
                Intent in= new Intent(context,MainActivity.class);
                in.putExtras(bun);
                context.startActivity(in);
            }
        });

        return  convertView;
    }
}
