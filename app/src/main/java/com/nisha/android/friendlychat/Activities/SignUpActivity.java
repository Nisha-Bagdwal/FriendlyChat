package com.nisha.android.friendlychat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.nisha.android.friendlychat.R;

public class SignUpActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText phoneNumberEt;
    Button continuebt;
    String countryCode,phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.enter_ph);

        ccp = findViewById(R.id.ccp);
        phoneNumberEt=findViewById(R.id.editTextph);
        continuebt=findViewById(R.id.buttonph);

        continuebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countryCode=ccp.getSelectedCountryCode();
                phoneNumber=phoneNumberEt.getText().toString().trim();
                //Toast.makeText(SignUpActivity.this,countryCode + phoneNumber,Toast.LENGTH_LONG).show();
                if(phoneNumber.isEmpty() || phoneNumber.length() < 10){
                    phoneNumberEt.setError("Enter a valid mobile");
                    phoneNumberEt.requestFocus();

                }
                else {
                    String mobile = "+"+countryCode + phoneNumber;
                    Intent in = new Intent(SignUpActivity.this, VerifyPhoneActivity.class);
                    in.putExtra("mobile", mobile);
                    startActivity(in);
                }
            }
        });
    }
}
