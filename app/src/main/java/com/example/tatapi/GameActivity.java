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
import android.widget.Toast;

import com.example.tatapi.models.User;
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
    private User player;


//    private User mUser;
    private String dummyEnemyId = "none";
    private Enemy testEnemy;

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

        // this will be called each time an enemy dies?
        getEnemy("yQLZKAoZ7Y");

        wireUp();
        login();
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
            updateHealthView();
            updateBattleView();

            //using for test purposes (remove later)
            testFunctionToChangeEnemyData(testEnemy);
            updateEnemy(testEnemy);
//            if(mUser.getDead() == false && dummyEnemy.getDead() == false) {
//                executeTurn(0);
//                turnCount++;
//            }
//            else if(mUser.getDead() == true){
//                healthView.setText(currentHealthDisplay());
//                snackMaker("can't do anything, you're dead!");
//                Log.d("EVENT", "Survived " + turnCount + " turns.");
//            }
//            else{
//                snackMaker("Enemy is dead, level up and grab a new monster");
//                //level up here
//                enemiesDefeated++;
//            }
        });

        defendButton.setOnClickListener(v -> {
            //snackMaker("Defending...");

            //Using this to test accessing enemy data (remove later)
            //snackMaker(testEnemy.getDescription());
            snackMaker(Integer.toString(testEnemy.getHealth()));
//            if(mUser.getDead() == false && dummyEnemy.getDead() == false) {
//                executeTurn(1);
//                turnCount++;
//            }
//            else if(mUser.getDead() == true){
//                snackMaker("can't do anything, you're dead!");
//                Log.d("EVENT", "Survived " + turnCount + " turns.");
//            }
//            else{
//                snackMaker("Enemy is dead, level up and grab a new monster");
//                //level up here
//                enemiesDefeated++;
//            }
        });

        itemButton.setOnClickListener(v -> {
            snackMaker("Using an item...");

//            if(mUser.getDead() == false && dummyEnemy.getDead() == false) {
//                snackMaker("Using an item...");
//                turnCount++;
//            }
//            else if(mUser.getDead() == true){
//                snackMaker("can't do anything, you're dead!");
//                Log.d("EVENT", "Survived " + turnCount + " turns.");
//            }
//            else{
//                snackMaker("Enemy is dead, level up and grab a new monster");
//                //level up here
//                enemiesDefeated++;
//            }
        });
    }

    private String currentHealthDisplay() {
        String currentHealth = player.getUsername();
        currentHealth += "'s HP: ";

        //player's hp
        //if below 0, just display 0
        if (player.getHealth() <= 0) {
            currentHealth += "0.0/";
        } else {
            currentHealth += player.getHealth() + "/";
        }
        currentHealth += player.getOverAllHealth() + "\n";
        currentHealth += testEnemy.getName() + " HP: ";
        //enemy's hp
        //if below 0, just display 0
        if (testEnemy.getHealth() <= 0) {
            currentHealth += "0/";
        } else {
            currentHealth += testEnemy.getOverallHealth() + "/";
        }
        currentHealth += testEnemy.getHealth();

        return (currentHealth);
    }

    private void executeTurn(int type){
        //type 0 will be attack
        if(lineCount + 1 > 8){
            battleView.setText("");
            lineCount = 0;
        }
        if(type == 0){
            //replace with damage calulation
//            dummyEnemy.takeDamage(50.0f);
            healthView.setText(currentHealthDisplay());
//            battleView.append(dummyEnemy.getName() + " took " + 50.0f + " damage.\n");
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
//                    mUser.takeDamage(25.0f);
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
//                    mUser.takeDamage(damage);
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

    private void updateBattleView(){
        battleView.setText("doing some sick attacks right now...");
    }

    private void getEnemy(String enemyId){
        //subject to change depending on game play implementation
        ParseQuery<Enemy> query = ParseQuery.getQuery("Enemy");
        query.getInBackground(enemyId, new GetCallback<Enemy>() {
            public void done(Enemy tempEnemy, ParseException e) {
                if (e == null) {
                    snackMaker("pulled " + tempEnemy.getName() + " from db");
                    testEnemy = tempEnemy;
                    healthView.setText(currentHealthDisplay());
                } else {
                    snackMaker(e.getMessage());
                }
            }
        });
    }

    private User setPlayerInfo(ParseUser currentUser){
        User player = new User();
        player.setUsername(currentUser.get("username").toString());
        player.setHealth(currentUser.getInt("health"));
        player.setOverAllHealth(currentUser.getInt("overallHealth"));
        player.setStrength(currentUser.getInt("strength"));
        player.setDefense(currentUser.getInt("defense"));
        return player;
    }

    private void updateEnemy (Enemy enemy){
        //subject to change or removal depending on game play implementation
        enemy.saveInBackground();
    }

    private void testFunctionToChangeEnemyData(Enemy enemy){
        //remove later
        enemy.setHealth(enemy.getHealth() + 25);
        enemy.setStrength(enemy.getStrength() + 5);
        enemy.setDefense(enemy.getDefense() + 5);
    }

    private void login(){
        if(mPrefs == null){
            getPrefs();
        }
        mUserId = mPrefs.getString(USER_KEY, "none");
        mUser = ParseUser.getCurrentUser();
        player = setPlayerInfo(mUser);
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
}
