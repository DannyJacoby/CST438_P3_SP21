package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tatapi.models.User;
import com.example.tatapi.models.Enemy;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    protected static final String PREF_KEY = LandingActivity.PREF_KEY;
    protected static final String USER_KEY = LandingActivity.USER_KEY;

    public Button attackButton;
    public Button defendButton;
    public Button itemButton;

    public ImageButton mEnemyIcon;

    public TextView healthView;

    public TextView battleView;

    private String mUserId = "none";
    private ParseUser mUser;
    private User player;
    private Enemy testEnemy = new Enemy();

    private SharedPreferences mPrefs = null;
    private SharedPreferences.Editor mEdit;

    private Handler battleHandler = new Handler();
    private int lineCount;
    private List<String> mBattleLog = new ArrayList<String>();
    //these should be exported or maybe just added onto the user object
    private int turnCount;
    private int enemiesDefeated;
    private int currentLevel;
    private boolean dead = false;
    private boolean levelUp = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // this will be called each time an enemy dies?
        getEnemy();

        wireUp();
        login();

        lineCount = 0;
        turnCount = 0;
        enemiesDefeated = 0;
        refreshDisplay("A Battle has Started!");
    }

    private void wireUp(){
        attackButton = findViewById(R.id.attack_button);
        defendButton = findViewById(R.id.defend_button);
        itemButton = findViewById(R.id.item_button);
        healthView = findViewById(R.id.health_text);
        battleView = findViewById(R.id.battle_text);
        battleView.setMovementMethod(new ScrollingMovementMethod());

        mEnemyIcon = findViewById(R.id.imgBtn);
        //TODO here's where you can put whatever the enemies picture would be, or we could replace it with something else like a basic bad guy look alike
        mEnemyIcon.setImageResource(R.drawable.goblin);
        mEnemyIcon.setOnClickListener(v -> {
            alertMaker(testEnemy.getDescription());
        });

        attackButton.setOnClickListener(v -> {
            executeTurn(0);
            turnCount++;

            /*if(mUser.getDead() == false && dummyEnemy.getDead() == false) {
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
            }*/
        });

        defendButton.setOnClickListener(v -> {
            executeTurn(1);
            turnCount++;
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
            currentHealth += testEnemy.getHealth() + "/";
        }
        currentHealth += testEnemy.getOverallHealth();

        return (currentHealth);
    }

    private void executeTurn(int type){ // TODO replace with recyclerView
        //type 0 will be attack
        if(lineCount + 1 > 8){
//            battleView.setText(""); // TODO recycle? Starting Battle?
            refreshDisplay("Battle has started!");
            lineCount = 0;
        }
        if(type == 0){
            //damage calculation formula
            Random rand = new Random();
            int damage = rand.nextInt(player.getStrength());
            if(testEnemy.getHealth() - damage <= 0){
                //dead
                testEnemy.setHealth(0);
                levelUp = true;
            }
            else{
                int newHealth = testEnemy.getHealth();
                newHealth -= damage;
                testEnemy.setHealth(newHealth);
            }
            //update display
            healthView.setText(currentHealthDisplay());
            if(levelUp){
//                battleView.append(testEnemy.getName() + " took mortal damage!\n"); // TODO recycle?
                refreshDisplay(testEnemy.getName() + " took mortal damage!");
            }
            else{
//                battleView.append(testEnemy.getName() + " took " + damage + " damage!\n"); // TODO recycle?
                refreshDisplay(testEnemy.getName() + " took " + damage + " damage!");
            }
            lineCount++;
            //if enemy is defeated...
            if(levelUp){
                //load a new monster
                snackMaker("Need to load a new monster");
                //increase level
                currentLevel += 1;
                enemiesDefeated++;
                player.setLevel(currentLevel);
                levelUp = false;
            } else{
                //using a handler + runnable to delay an action so an enemy's attack is not instant
                //supposedly each view has its own handler, not sure what it actually is though
                //this works for now
                //https://stackoverflow.com/questions/14186846/delay-actions-in-android
                //https://developer.android.com/reference/android/os/Handler
                battleHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Random rand = new Random();
                        int damage = rand.nextInt(testEnemy.getStrength());
                        if(player.getHealth() - damage <= 0){
                            //dead
                            player.setHealth(0);
                            dead = true;
                        }
                        else{
                            int newHealth = player.getHealth();
                            newHealth -= damage;
                            player.setHealth(newHealth);
                        }
                        //update display
                        healthView.setText(currentHealthDisplay());
                        if(dead){
//                            battleView.append(player.getUsername() + " took mortal damage!\n"); // TODO recycle?
                            refreshDisplay(player.getUsername() + " took mortal damage!");
                        }
                        else{
//                            battleView.append(player.getUsername() + " took " + damage + " damage!\n"); // TODO recycle?
                            refreshDisplay(player.getUsername() + " took " + damage + " damage!");
                        }
                        lineCount++;
                        healthView.setText(currentHealthDisplay());
                        if(dead){
                            snackMaker("Pop-up message here informing player they died at level " + currentLevel + ".");
                            // TODO amDead function?
                        }
                    }
                }, 2000);
            }
        }
        //type 1 will be defend
        if(type == 1){
//            battleView.append(player.getUsername() + " defended.\n");
            lineCount++;
            battleHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //replace with defense calculation
                    Random rand = new Random();
                    //nope, you might get negative values!
                    int damage = rand.nextInt(testEnemy.getStrength());
                    if(damage - rand.nextInt(player.getDefense()) <= 0){
                        damage = 0;
                    }
                    if(player.getHealth() - damage <= 0){
                        //dead
                        player.setHealth(0);
                        dead = true;
                    }
                    else{
                        int newHealth = player.getHealth();
                        newHealth -= damage;
                        player.setHealth(newHealth);
                    }
                    //update display
                    healthView.setText(currentHealthDisplay());
                    if(dead){
//                        battleView.append(player.getUsername() + " took mortal damage!\n"); // TODO recycle?
                        refreshDisplay(player.getUsername() + " took mortal damage!");
                    }
                    else{
                        if(damage <= 0){
//                            battleView.append(testEnemy.getName() + " missed!\n"); // TODO recycle?
                            refreshDisplay(testEnemy.getName() + " missed!");
                        }else{
//                            battleView.append(player.getUsername() + " took " + damage + " damage!\n"); // TODO recycle?
                            refreshDisplay(player.getUsername() + " took " + damage + " damage!");
                        }
                    }
                    lineCount++;
                    healthView.setText(currentHealthDisplay());
                    if(dead){
                        snackMaker("Pop-up message here informing player they died at level " + currentLevel + ".");
                        // TODO replace with amDead function?
                    }
                }
            }, 2000);
        }
        //type 2 will be item? if we're still going to do that
    }

//    private void updateHealthView(){
//        snackMaker("Do some attacks here, update the hp values");
//    }

//    private void updateBattleView(){
//        battleView.setText("doing some sick attacks right now...");
//    }

    private void getEnemy(){
        //check for existing enemy user may have
        ParseQuery<ParseObject> query = ParseQuery.getQuery("savedEnemy");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> savedEnemy, ParseException e) {
                if (e == null) {
                    if(savedEnemy.size() > 0) {
                        testEnemy.setName(savedEnemy.get(0).getString("name"));
                        testEnemy.setHealth(savedEnemy.get(0).getInt("health"));
                        testEnemy.setOverallHealth(savedEnemy.get(0).getInt("overallHealth"));
                        testEnemy.setStrength(savedEnemy.get(0).getInt("strength"));
                        testEnemy.setDefense(savedEnemy.get(0).getInt("defense"));
                        testEnemy.setDescription(savedEnemy.get(0).getString("description"));

                    } else {
                        getNewEnemy();
                    }
                } else {
                    snackMaker(e.getMessage());
                }
            }
        });
    }

    private void getNewEnemy(){
        //subject to change depending on game play implementation
        ParseQuery<Enemy> query = ParseQuery.getQuery("Enemy");
        query.findInBackground(new FindCallback<Enemy>() {
            @Override
            public void done(List<Enemy> enemies, final ParseException e) {
                if (e == null) {
                    int random = (int)Math.floor(Math.random() * enemies.size());
                    Enemy tempEnemy = enemies.get(random);
                    //snackMaker(tempEnemy.getName());
                    testEnemy = tempEnemy;
                    snackMaker(testEnemy.getName());
                    // Quick dump of info if needed for testing...
                    /*
                    Log.d("DEBUG", "Start Enemy OverallHealth: " + Integer.toString(testEnemy.getOverallHealth()));
                    Log.d("DEBUG", "Start Enemy Health: " + Integer.toString(testEnemy.getHealth()));
                    Log.d("DEBUG", "Start Enemy Strength: " + Integer.toString(testEnemy.getStrength()));
                    Log.d("DEBUG", "Start Enemy Defense: " + Integer.toString(testEnemy.getDefense()));
                     */
                    //NOTE: ONLY call calcStats upon level up, do not call it upon restoring state
                    //it will overwrite stats
                    tempEnemy.calcStats(currentLevel);
                    /*
                    Log.d("DEBUG", "New Enemy OverallHealth: " + Integer.toString(testEnemy.getOverallHealth()));
                    Log.d("DEBUG", "New Enemy Health: " + Integer.toString(testEnemy.getHealth()));
                    Log.d("DEBUG", "New Enemy Strength: " + Integer.toString(testEnemy.getStrength()));
                    Log.d("DEBUG", "New Enemy Defense: " + Integer.toString(testEnemy.getDefense()));
                     */
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
        player.setLevel(currentUser.getInt("level"));
        return player;
    }

    private void updateUserInfo(User player){
        ParseUser updateUser = ParseUser.getCurrentUser();
        updateUser.put("health", player.getHealth());
        updateUser.put("overallHealth", player.getOverAllHealth());
        updateUser.put("strength", player.getStrength());
        updateUser.put("defense", player.getDefense());
        updateUser.put("level", player.getLevel());
        updateUser.saveInBackground();
    }

    private void updateEnemy (Enemy enemy){
        //subject to change or removal depending on game play implementation
        enemy.saveInBackground();
    }

    private void login(){
        if(mPrefs == null){
            getPrefs();
        }
        mUserId = mPrefs.getString(USER_KEY, "none");
        mUser = ParseUser.getCurrentUser();
        player = setPlayerInfo(mUser);
        // Quick dump of info if needed for testing...
        /*Log.d("DEBUG", "Start Player Health: " + Integer.toString(player.getOverAllHealth()));
        Log.d("DEBUG", "Start Player Strength: " + Integer.toString(player.getStrength()));
        Log.d("DEBUG", "Start Player Defense: " + Integer.toString(player.getDefense()));
        Log.d("DEBUG", "Start Player Level: " + Integer.toString(player.getLevel()));
         */
        player.calcStats(player.getLevel());
        currentLevel = player.getLevel();
        /*Log.d("DEBUG", "New Player Health: " + Integer.toString(player.getOverAllHealth()));
        Log.d("DEBUG", "New Player Strength: " + Integer.toString(player.getStrength()));
        Log.d("DEBUG", "New Player Defense: " + Integer.toString(player.getDefense()));
        Log.d("DEBUG", "New Player Level: " + Integer.toString(player.getLevel()));
         */
    }

    private void refreshDisplay(String str){
        if(mBattleLog.size() <= 0){
            String s = "A Battle has Started!";
            battleView.setText("A Battle has Started!");
            mBattleLog.add(s);
            return;
        }

        mBattleLog.add(str);

        StringBuilder sb = new StringBuilder();
        for(String log : mBattleLog){
            sb.append(log);
            sb.append("\n");
        }
        battleView.setText(sb);
    }

    private void getPrefs(){
        mPrefs = this.getSharedPreferences(PREF_KEY, 0);
        mEdit = mPrefs.edit();
    }

    private void alertMaker(String message){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(message);

        alertBuilder.setNegativeButton("Done", (dialog, which) -> {
            //Don't need to do anything here
            snackMaker("Back to the game");
        });

        alertBuilder.create().show();
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

    @Override
    public void onBackPressed() {
        //delete old enemy save if it exists
        ParseQuery<ParseObject> query = ParseQuery.getQuery("savedEnemy");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> save, ParseException e) {
                if (e == null) {
                    if(save.size() > 0) {
                        save.get(0).deleteInBackground();
                    }
                } else {
                    snackMaker(e.getMessage());
                }
            }
        });
        //save current enemy
        ParseObject saveEnemy = new ParseObject("savedEnemy");
        saveEnemy.put("name", testEnemy.getName().toString());
        saveEnemy.put("health", testEnemy.getHealth());
        saveEnemy.put("overallHealth", testEnemy.getOverallHealth());
        saveEnemy.put("strength", testEnemy.getDefense());
        saveEnemy.put("defense", testEnemy.getDefense());
        saveEnemy.put("description", testEnemy.getDescription());
        saveEnemy.put("user", ParseUser.getCurrentUser().getObjectId());
        saveEnemy.saveInBackground();
        //update user with player stats
        updateUserInfo(player);

        Intent intent = HomeActivity.intent_factory(this);
        startActivity(intent);
        super.onBackPressed();
    }
}
