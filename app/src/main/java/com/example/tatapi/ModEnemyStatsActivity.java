package com.example.tatapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tatapi.models.Enemy;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class ModEnemyStatsActivity extends AppCompatActivity {

    private EditText enemyNameField;
    private EditText enemyHealthField;
    private EditText enemyStrField;
    private EditText enemyDefField;
    private EditText enemyDesField;
    private Button loadStatsButton;
    private Button updateStatsButton;
    boolean statsLoaded;

    Enemy tempEnemy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_enemy_stats);
        wireUp();


    }

    private void wireUp(){
        enemyNameField = findViewById(R.id.enemyNameField);
        enemyHealthField = findViewById(R.id.enemyHealthField);
        enemyStrField = findViewById(R.id.enemyStrengthField);
        enemyDefField = findViewById(R.id.enemyDefenseField);
        enemyDesField = findViewById(R.id.enemyDescriptionField);
        loadStatsButton = findViewById(R.id.loadStatsBtn);
        updateStatsButton = findViewById(R.id.updateStatsBtn);
        updateStatsButton.setVisibility(View.GONE);
        statsLoaded = false;

        loadStatsButton.setOnClickListener(v -> {
            String name = enemyNameField.getText().toString();
            ParseQuery<Enemy> query = ParseQuery.getQuery("Enemy");
            query.whereEqualTo("name", name);
            query.findInBackground(new FindCallback<Enemy>() {
                @Override
                public void done(List<Enemy> enemies, final ParseException e) {
                    if (e == null && enemies.size() != 0) {
                        tempEnemy = enemies.get(0);
                        enemyHealthField.setText(String.valueOf(tempEnemy.getOverallHealth()));
                        enemyStrField.setText(String.valueOf(tempEnemy.getStrength()));
                        enemyDefField.setText(String.valueOf(tempEnemy.getDefense()));
                        enemyDesField.setText(tempEnemy.getDescription());
                        statsLoaded = true;
                        loadStatsButton.setVisibility(View.GONE);
                        updateStatsButton.setVisibility(View.VISIBLE);
                    } else {
                        if (enemies.size() == 0) {
                            snackMaker("No such enemy found.");
                        } else {
                            snackMaker(e.getMessage());
                        }
                    }
                }
            });
        });

        updateStatsButton.setOnClickListener(v -> {
            if(!statsLoaded){
                snackMaker("You need to load an enemy first!");
            } else{
                tempEnemy.setHealth(Integer.parseInt(enemyHealthField.getText().toString()));
                tempEnemy.setOverallHealth(Integer.parseInt(enemyHealthField.getText().toString()));
                tempEnemy.setStrength(Integer.parseInt(enemyStrField.getText().toString()));
                tempEnemy.setDefense(Integer.parseInt(enemyDefField.getText().toString()));
                tempEnemy.setDescription(enemyDesField.getText().toString());
                tempEnemy.saveInBackground();
                statsLoaded = false;
                enemyNameField.setText("");
                enemyHealthField.setText("");
                enemyStrField.setText("");
                enemyDefField.setText("");
                enemyDesField.setText("");
                snackMaker("Enemy stats were updated!");
                updateStatsButton.setVisibility(View.GONE);
                loadStatsButton.setVisibility(View.VISIBLE);
            }
        });

    }

    public static Intent intent_factory(Context context){
        Intent intent = new Intent(context, ModEnemyStatsActivity.class);
        return intent;
    }

    private void snackMaker(String message){
        Snackbar snackBar = Snackbar.make(findViewById(R.id.ModStatsLayout),
                message,
                Snackbar.LENGTH_SHORT);
        snackBar.show();
    }
}