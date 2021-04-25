package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.tatapi.db.AppDatabase;
import com.example.tatapi.db.Enemy;
import com.example.tatapi.db.EnemyDAO;
import com.example.tatapi.db.User;
import com.example.tatapi.db.UserDAO;
import com.google.android.material.snackbar.Snackbar;

public class GameActivity extends AppCompatActivity {
    protected static final String PREF_KEY = LandingActivity.PREF_KEY;
    protected static final String USER_KEY = LandingActivity.USER_KEY;

    public Button attackButton;
    public Button defendButton;
    public Button itemButton;
    public TextView healthView;
    public TextView battleView;

    private int mUserId = -1;
    private User mUser;
    private int dummyEnemyId = -1;
    private Enemy dummyEnemy = new Enemy("Test", 100.0f, 2, 2);

    private UserDAO mUserDAO;
    private EnemyDAO mEnemyDAO;

    private SharedPreferences mPrefs = null;
    private SharedPreferences.Editor mEdit;

    private Handler battleHandler = new Handler();
    private int lineCount;
    //these should be exported or maybe just added onto the user object
    private int turnCount;
    private int enemiesDefeated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getDatabase();
        wireUp();
        login();
        healthView.setText(currentHealthDisplay());
        lineCount = 0;
        turnCount = 0;
        enemiesDefeated = 0;
    }

    private void wireUp(){
        attackButton = findViewById(R.id.attack_button);
        defendButton = findViewById(R.id.defend_button);
        itemButton = findViewById(R.id.item_button);
        healthView = findViewById(R.id.health_text);
        battleView = findViewById(R.id.battle_text);
        //healthView.setText(currentHealthDisplay());

        //wrapped these in a conditional, we should probably handle this better, but this works for now
        attackButton.setOnClickListener(v -> {
            if(mUser.getDead() == false && dummyEnemy.getDead() == false) {
                executeTurn(0);
                turnCount++;
            }
            else if(mUser.getDead() == true){
                healthView.setText(currentHealthDisplay());
                snackMaker("can't do anything, you're dead!");
                Log.d("EVENT", "Survived " + turnCount + " turns.");
            }
            else{
                snackMaker("Enemy is dead, level up and grab a new monster");
                //level up here
                enemiesDefeated++;
            }
        });

        defendButton.setOnClickListener(v -> {
            if(mUser.getDead() == false && dummyEnemy.getDead() == false) {
                executeTurn(1);
                turnCount++;
            }
            else if(mUser.getDead() == true){
                snackMaker("can't do anything, you're dead!");
                Log.d("EVENT", "Survived " + turnCount + " turns.");
            }
            else{
                snackMaker("Enemy is dead, level up and grab a new monster");
                //level up here
                enemiesDefeated++;
            }
        });

        itemButton.setOnClickListener(v -> {
            if(mUser.getDead() == false && dummyEnemy.getDead() == false) {
                snackMaker("Using an item...");
                turnCount++;
            }
            else if(mUser.getDead() == true){
                snackMaker("can't do anything, you're dead!");
                Log.d("EVENT", "Survived " + turnCount + " turns.");
            }
            else{
                snackMaker("Enemy is dead, level up and grab a new monster");
                //level up here
                enemiesDefeated++;
            }
        });
    }

    private String currentHealthDisplay() {
        String currentHealth = "Player HP: ";

        //player's hp
        //if below 0, just display 0
        if(mUser.getHealth() <= 0){
            currentHealth += "0.0/";
        }
        else{
            currentHealth += mUser.getHealth() + "/";
        }
        currentHealth += mUser.getOverAllHealth() + "\n";

        currentHealth += dummyEnemy.getName() + " HP: ";

        //enemy's hp
        //if below 0, just display 0
        if(dummyEnemy.getHealth() <= 0){
            currentHealth += "0.0/";
        }else{
            currentHealth += dummyEnemy.getHealth() + "/";
        }
        currentHealth += dummyEnemy.getOverAllHealth();

        return (currentHealth);
    }

    private void login(){
        if(mPrefs == null){
            getPrefs();
        }
        mUserId = mPrefs.getInt(USER_KEY, -1);
        mUser = mUserDAO.getUserByUserId(mUserId);
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).allowMainThreadQueries().build().getUserDAO();
        // not sure if we need a local DB for enemies honestly but it's there if we need to use it
        mEnemyDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).allowMainThreadQueries().build().getEnemyDAO();
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

    private void executeTurn(int type){
        //type 0 will be attack
        if(lineCount + 1 > 8){
            battleView.setText("");
            lineCount = 0;
        }
        if(type == 0){
            //replace with damage calulation
            dummyEnemy.takeDamage(50.0f);
            healthView.setText(currentHealthDisplay());
            battleView.append(dummyEnemy.getName() + " took " + 50.0f + " damage.\n");
            lineCount++;
            //using a handler + runnable to delay an action so an enemy's attack is not instant
            //supposedly each view has its own handler, not sure what it actually is though
            //this works for now
            //https://stackoverflow.com/questions/14186846/delay-actions-in-android
            //https://developer.android.com/reference/android/os/Handler
            battleHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //replace with damage calculation
                    mUser.takeDamage(25.0f);
                    healthView.setText(currentHealthDisplay());
                    battleView.append("Player took " + 25.0f + " damage.\n");
                    lineCount++;
                }
            }, 2000);
        }
        //type 1 will be defend
        if(type == 1){
            battleView.append("Player defended.\n");
            lineCount++;
            battleHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //replace with defense calculation
                    float damage = 25.0f/2.0f;
                    mUser.takeDamage(damage);
                    healthView.setText(currentHealthDisplay());
                    battleView.append("Player took " + damage + " damage.\n");
                    lineCount++;
                }
            }, 2000);
        }
        //type 2 will be item? if we're still going to do that
    }

    private void updateHealthView(){
        snackMaker("Do some attacks here, update the hp values");
    }
}
