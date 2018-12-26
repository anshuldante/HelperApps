package com.example.a0a00uj.externalsmsmanager;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void smsSendMessage(View view) {
        // Find the TextView number_to_call and assign it to textView.
        TextView textView = (TextView) findViewById(R.id.number_to_call);

        // Concatenate "smsto:" with phone number to create smsNumber.
        String smsNumber = "smsto:" + textView.getText().toString();

        // Find the sms_message view.
        EditText smsEditText = (EditText) findViewById(R.id.sms_message);

        // Get the text of the sms message.
        String sms = smsEditText.getText().toString();

        // Create the intent.
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);

        // Set the data for the intent as the phone number.
        smsIntent.setData(Uri.parse(smsNumber));

        // Add the message (sms) with the key ("sms_body").
        smsIntent.putExtra("sms_body", sms);

        // If package resolves (target app installed), send intent.
        if (smsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(smsIntent);

        } else {
            Log.e("tag: ", "Can't resolve app for ACTION_SENDTO Intent.");
        }
    }
}
