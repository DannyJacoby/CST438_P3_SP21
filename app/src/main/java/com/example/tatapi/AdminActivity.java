package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    private Button modEnemyStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        wireUp();

    }


    private void wireUp(){
        modEnemyStats = findViewById(R.id.adminButton1);

        modEnemyStats.setOnClickListener(v -> {
            Intent intent = ModEnemyStatsActivity.intent_factory(this);
            startActivity(intent);
        });

    }

    public static Intent intent_factory(Context context){
        Intent intent = new Intent(context, AdminActivity.class);
        return intent;
    }
}