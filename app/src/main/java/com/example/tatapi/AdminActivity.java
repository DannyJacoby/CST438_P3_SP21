package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        wireUp();


    }


    private void wireUp(){

    }

    public static Intent intent_factory(Context context){
        Intent intent = new Intent(context, AdminActivity.class);
        return intent;
    }
}