package com.l4d.maye;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class KontrolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontrol);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        int myInt = preferences.getInt("session", 0);



        if(myInt == 0){
            Intent i = new Intent(KontrolActivity.this,startActivity.class);
            startActivity(i);
            finish();
        }
        else {
            Intent i = new Intent(KontrolActivity.this,IndexActivity.class);
            startActivity(i);
            finish();
        }
    }

    public static boolean kontrolet = false;
}
