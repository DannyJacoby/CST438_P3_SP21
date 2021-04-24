package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tatapi.models.Enemy;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class GameActivity extends AppCompatActivity {
    protected static final String PREF_KEY = LandingActivity.PREF_KEY;
    protected static final String USER_KEY = LandingActivity.USER_KEY;

    public Button attackButton;
    public Button defendButton;
    public Button itemButton;
    public TextView healthView;
    public TextView battleView;

    private String mUserId = "none";
    private ParseUser mUser;
    private String dummyEnemyId = "none";
    private Enemy testEnemy;

    private SharedPreferences mPrefs = null;
    private SharedPreferences.Editor mEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getEnemy("yQLZKAoZ7Y");
        wireUp();
        login();

    }

    private void wireUp(){
        attackButton = findViewById(R.id.attack_button);
        defendButton = findViewById(R.id.defend_button);
        itemButton = findViewById(R.id.item_button);
        healthView = findViewById(R.id.health_text);
        battleView = findViewById(R.id.battle_text);

        attackButton.setOnClickListener(v -> {
            updateHealthView();
            updateBattleView();
        });

        defendButton.setOnClickListener(v -> {
            //snackMaker("Defending...");

            //Using this to test accessing enemy data (remove later)
            snackMaker(testEnemy.getDescription());
        });

        itemButton.setOnClickListener(v -> {
            snackMaker("Using an item...");

        });
    }

    private void login(){
        if(mPrefs == null){
            getPrefs();
        }
        mUserId = mPrefs.getString(USER_KEY, "none");
        mUser = ParseUser.getCurrentUser();
    }

    private void getPrefs(){
        mPrefs = this.getSharedPreferences(PREF_KEY, 0);
        mEdit = mPrefs.edit();
    }

    private void snackMaker(String message){
        Snackbar snackBar = Snackbar.make(findViewById(R.id.GameLayout),
                message,
                Snackbar.LENGTH_SHORT);
        snackBar.show();
    }

    public static Intent intent_factory(Context context){
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    private void updateHealthView(){
        snackMaker("Do some attacks here, update the hp values");
    }

    private void updateBattleView(){
        battleView.setText("doing some sick attacks right now...");
    }

    private void getEnemy(String enemyId){
        ParseQuery<Enemy> query = ParseQuery.getQuery("Enemy");
        query.getInBackground(enemyId, new GetCallback<Enemy>() {
            public void done(Enemy tempEnemy, ParseException e) {
                if (e == null) {
                    snackMaker("pulled " + tempEnemy.getName() + " from db");
                    testEnemy = tempEnemy;
                } else {
                    snackMaker(e.getMessage());
                }
            }
        });
    }
}
