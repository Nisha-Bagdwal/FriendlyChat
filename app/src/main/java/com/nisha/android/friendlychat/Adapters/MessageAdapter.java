package com.nisha.android.friendlychat.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nisha.android.friendlychat.Objects.FriendlyMessage;
import com.nisha.android.friendlychat.Activities.MainActivity;
import com.nisha.android.friendlychat.R;

import java.util.List;


public class MessageAdapter extends ArrayAdapter<FriendlyMessage> {

    private ImageView photoImageView;
    TextView messageTextView,authorTextView;

    public MessageAdapter(Context context, int resource, List<FriendlyMessage> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FriendlyMessage message = getItem(position);

        String currentUser=MainActivity.mUserContact;

        if(message.getContact().contentEquals(currentUser)) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.my_message, parent, false);
            //photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
            messageTextView = (TextView) convertView.findViewById(R.id.message_body);
            //authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        }else{
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.their_message, parent, false);
            //photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
            messageTextView = (TextView) convertView.findViewById(R.id.message_body);
            //authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        }

        messageTextView.setText(message.getText());


        /*boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getText());
        }
        authorTextView.setText(message.getName());*/

        return convertView;
    }
}
