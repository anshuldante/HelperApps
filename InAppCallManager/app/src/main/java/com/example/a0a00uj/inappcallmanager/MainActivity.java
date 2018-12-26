package com.example.a0a00uj.inappcallmanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    private TelephonyManager telephonyManager;
    private MyPhoneCallListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);


        if (isTelephonyEnabled()) {
            checkForPhonePermission();

            mListener = new MyPhoneCallListener();
            telephonyManager.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);

        } else {
            Toast.makeText(this,
                    "Telephony is not enabled, disabling call button",
                    Toast.LENGTH_LONG).show();
            disableCallButton();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isTelephonyEnabled()) {

            telephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.CALL_PHONE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.

                } else {
                    // Permission denied. Stop the app.
                    Log.d("Call permission: ", "Call permission was denied, disabling the call button!");

                    Toast.makeText(this, "Call permission was denied, disabling the call button!", Toast.LENGTH_LONG).show();

                    disableCallButton();
                }
            }
        }
    }

    public void callNumber(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);

        // Use format with "tel:" and phone number to create phoneNumber.

        String phoneNumber = String.format("tel: %s", editText.getText().toString());

        // Create the intent.
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(phoneNumber));

        if (callIntent.resolveActivity(getPackageManager()) != null) {

            checkForPhonePermission();

            startActivity(callIntent);
        } else {
            Log.e("Call enabled check: ", "Can't resolve app for ACTION_CALL Intent.");
        }
    }

    private void disableCallButton() {
    }

    private void checkForPhonePermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            Log.d("Permissions: ", "Permission not yet granted!");

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);

        } else {
        }
    }


    private boolean isTelephonyEnabled() {
        if (telephonyManager != null) {

            Log.i("Telephony state", "Telephony Manager is ready to be used!");

            if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {

                Log.i("Sim state", "Sim is ready to be used!");

                return true;
            }
        }
        return false;
    }

    private class MyPhoneCallListener extends PhoneStateListener {

        private boolean returningFromOffHook = false;

        String message = "Hi, your phone is ";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // Incoming call is ringing.
                    message = message + "ringing" + incomingNumber;

                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                    Log.i("Call state changed:", message);

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // Phone call is active -- off the hook.
                    message = message + "in a call with: " + incomingNumber;

                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                    Log.i("Call state changed:", message);

                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    // Phone is idle before and after phone call.

                    // If running on version older than 19 (KitKat), restart activity when phone call ends.

                    message = message + "idle";

                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                    Log.i("Call state changed:", message);

                    if (returningFromOffHook) {
                        // No need to do anything if >= version K

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {

                            Log.i("Call state changed:", message);

                            Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent);
                        }
                    }
                    break;
                default:
                    message = message + "Phone off";

                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                    Log.i("Call state changed:", message);

                    break;
            }
        }
    }
}
