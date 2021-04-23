package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private Enemy dummyEnemy;

    private UserDAO mUserDAO;
    private EnemyDAO mEnemyDAO;

    private SharedPreferences mPrefs = null;
    private SharedPreferences.Editor mEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getDatabase();
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
            snackMaker("Defending...");
        });

        itemButton.setOnClickListener(v -> {
            snackMaker("Using an item...");
        });
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

    private void updateHealthView(){
        snackMaker("Do some attacks here, update the hp values");
    }

    private void updateBattleView(){
        battleView.setText("doing some sick attacks right now...");
    }
}
