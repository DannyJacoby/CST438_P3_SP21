package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    private void snackMaker(String message){
        Snackbar snackBar = Snackbar.make(findViewById(R.id.HomeLayout),
                message,
                Snackbar.LENGTH_SHORT);
        snackBar.show();
    }

    public static Intent intent_factory(Context context){
        Intent intent = new Intent(context, HomeActivity.class);

        return intent;
    }
}