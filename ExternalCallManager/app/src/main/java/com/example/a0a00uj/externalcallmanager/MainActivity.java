package com.example.a0a00uj.externalcallmanager;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void makeCall(View view) {

        TextView textView = (TextView) findViewById(R.id.editText);


        String phoneNumber = String.format("tel: %s",
                textView.getText().toString());


        Intent dialIntent = new Intent(Intent.ACTION_DIAL);


        dialIntent.setData(Uri.parse(phoneNumber));


        if (dialIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(dialIntent);
        } else {
            Log.e("OOPS", "Can't resolve app for ACTION_DIAL Intent.");
        }
    }
}
