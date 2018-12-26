package com.example.a0a00uj.inappsmsmanager;

import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForReceiveSmsPermission();
    }

    public void smsSendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.receiver_number);
        String destinationAddress = editText.getText().toString();


        EditText smsEditText = (EditText) findViewById(R.id.sms_message);
        String smsMessage = smsEditText.getText().toString();

        String scAddress = null;
        PendingIntent sentIntent = null;
        PendingIntent deliveryIntent = null;

        checkForSendSmsPermission();

        SmsManager smsManager = SmsManager.getDefault();

        smsManager.sendTextMessage(destinationAddress, scAddress, smsMessage, sentIntent, deliveryIntent);
    }

    private void checkForSendSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);

        } else {
            enableSmsButton();
        }
    }

    private void checkForReceiveSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_RECEIVE_SMS);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_SEND_SMS: {

                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    Toast.makeText(MainActivity.this, "permission denied!", Toast.LENGTH_SHORT).show();
                    disableSmsButton();
                }
            }
        }
    }

    private void disableSmsButton() {
    }

    private void enableSmsButton() {
    }
}
