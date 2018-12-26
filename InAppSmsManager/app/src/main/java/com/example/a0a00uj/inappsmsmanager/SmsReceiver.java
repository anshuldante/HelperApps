package com.example.a0a00uj.inappsmsmanager;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("something: ", "whew!");
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";

        String format = bundle.getString("format");

        Object[] pdus = (Object[]) bundle.get("pdus");

        if (pdus != null) {

            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);

            msgs = new SmsMessage[pdus.length];

            for (int i = 0; i < msgs.length; i++) {

                if (isVersionM) {

                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {

                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                strMessage += " :" + msgs[i].getMessageBody() + "\n";

                Log.d("TAG", "onReceive: " + strMessage);
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
            }
        }
    }
}