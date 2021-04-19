package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import com.parse.Parse;

public class LandingActivity extends AppCompatActivity {
    private static final String PREF_KEY = "com.example.tatapi.PREFERENCES_KEY";
    private static final String USER_KEY = "com.example.tatapi.USERS_KEY";

    public Button mLoginBtn;
    public Button mCreateBtn;

    private SharedPreferences mPrefs = null;
    private SharedPreferences.Editor mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId(getString(R.string.back4app_app_id))
            .clientKey(getString(R.string.back4app_client_key))
            .server(getString(R.string.back4app_server_url))
            .build());

        wireUp();
        checkForUser();

    }

    private void wireUp(){
        mLoginBtn = findViewById(R.id.loginMainBtn);
        mCreateBtn = findViewById(R.id.createMainBtn);

        mLoginBtn.setOnClickListener(v -> {
            Intent intent = LoginActivity.intent_factory(getApplicationContext(), true);
            startActivity(intent);
        });

        mCreateBtn.setOnClickListener(v -> {
            Intent intent = LoginActivity.intent_factory(getApplicationContext(), false);
            startActivity(intent);
        });

    }

    private void getPrefs(){
        mPrefs = this.getSharedPreferences(PREF_KEY, 0);
        mEdit = mPrefs.edit();
    }

    private void checkForUser(){
        if(mPrefs == null){
            getPrefs();
        }


    }

    private void snackMaker(String message){
        Snackbar snackBar = Snackbar.make(findViewById(R.id.LandingLayout),
                message,
                Snackbar.LENGTH_SHORT);
        snackBar.show();
    }

    /**
     * intent_factory
     * @param context
     */
    public static Intent intent_factory(Context context){
        Intent intent = new Intent(context, LandingActivity.class);
        return intent;
    }

}