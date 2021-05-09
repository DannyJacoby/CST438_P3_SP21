package com.example.tatapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tatapi.models.Enemy;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class ModEnemyStatsActivity extends AppCompatActivity {

    private EditText enemyNameField;
    private TextView enemyNameTag;
    private EditText enemyHealthField;
    private TextView enemyHealthTag;
    private EditText enemyStrField;
    private TextView enemyStrTag;
    private EditText enemyDefField;
    private TextView enemyDefTag;
    private EditText enemyDesField;
    private TextView enemyDesTag;
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
        enemyNameTag = findViewById(R.id.enemyNameTag);
        enemyHealthField = findViewById(R.id.enemyHealthField);
        enemyHealthTag = findViewById(R.id.enemyHealthTag);
        enemyStrField = findViewById(R.id.enemyStrengthField);
        enemyStrTag = findViewById(R.id.enemyStrengthTag);
        enemyDefField = findViewById(R.id.enemyDefenseField);
        enemyDefTag = findViewById(R.id.enemyDefenseTag);
        enemyDesField = findViewById(R.id.enemyDescriptionField);
        enemyDesTag = findViewById(R.id.enemyDescriptionTag);
        loadStatsButton = findViewById(R.id.loadStatsBtn);
        updateStatsButton = findViewById(R.id.updateStatsBtn);
        toggleFieldsAndButtons(false);
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
                        toggleFieldsAndButtons(true);
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
                toggleFieldsAndButtons(false);
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

    private void toggleEditText(EditText editText, boolean active) {
        if(active){
            editText.setEnabled(true);
        } else {
            editText.setEnabled(false);
        }
    }

    private void toggleFieldsAndButtons(boolean show) {
        if (show) {
            toggleEditText(enemyNameField, false);
            enemyNameTag.setVisibility(View.GONE);
            enemyHealthField.setVisibility(View.VISIBLE);
            enemyHealthTag.setVisibility(View.VISIBLE);
            enemyStrField.setVisibility(View.VISIBLE);
            enemyStrTag.setVisibility(View.VISIBLE);
            enemyDefField.setVisibility(View.VISIBLE);
            enemyDefTag.setVisibility(View.VISIBLE);
            enemyDesField.setVisibility(View.VISIBLE);
            enemyDesTag.setVisibility(View.VISIBLE);
            loadStatsButton.setVisibility(View.GONE);
            updateStatsButton.setVisibility(View.VISIBLE);
        } else {
            toggleEditText(enemyNameField, true);
            enemyNameTag.setVisibility(View.VISIBLE);
            enemyHealthField.setVisibility(View.GONE);
            enemyHealthTag.setVisibility(View.GONE);
            enemyStrField.setVisibility(View.GONE);
            enemyStrTag.setVisibility(View.GONE);
            enemyDefField.setVisibility(View.GONE);
            enemyDefTag.setVisibility(View.GONE);
            enemyDesField.setVisibility(View.GONE);
            enemyDesTag.setVisibility(View.GONE);
            loadStatsButton.setVisibility(View.VISIBLE);
            updateStatsButton.setVisibility(View.GONE);
        }
    }
}