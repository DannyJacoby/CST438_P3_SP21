package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tatapi.db.AppDatabase;
import com.example.tatapi.db.User;
import com.example.tatapi.db.UserDAO;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity {
    protected static final String PREF_KEY = LandingActivity.PREF_KEY;
    protected static final String USER_KEY = LandingActivity.USER_KEY;


    private Button playBtn;
    private Button leaderboardBtn;
    private Button adminBtn;
    private Button logoutBtn;
    private TextView welcomeMsg;


    private String mUserId = "none";
    ParseUser currentUser;

    private SharedPreferences mPrefs = null;
    private SharedPreferences.Editor mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        currentUser = ParseUser.getCurrentUser();
        getDatabase();
        login();
        wireUp();
        welcomeMsg.setText("Welcome to TATAPI " + currentUser.getObjectId());


    }

    private void wireUp(){
        playBtn = findViewById(R.id.playBtn);
        leaderboardBtn = findViewById(R.id.leaderboardBtn);
        adminBtn = findViewById(R.id.adminBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        welcomeMsg = findViewById(R.id.welcomeHomeTextView);

        adminBtn.setVisibility( (currentUser.getBoolean("isAdmin")) ? View.VISIBLE : View.INVISIBLE);

        playBtn.setOnClickListener(v -> {
            Intent intent = GameActivity.intent_factory(this);
            startActivity(intent);
            snackMaker("Starting game...");
        });

        leaderboardBtn.setOnClickListener(v -> {
            snackMaker("You clicked Leaderboard");
        });

        adminBtn.setOnClickListener(v -> {
            snackMaker("You clicked admin congrats!");
        });

        logoutBtn.setOnClickListener(v ->{
            logout();
        });
    }

    private void login(){
        if(mPrefs == null){
            getPrefs();
        }
        mUserId = mPrefs.getString(USER_KEY, "none");
    }

    private void logout(){
        // Maybe add alert like "Do you really want to log out? Y/N"
        ParseUser.logOut();
        removeUserFromPrefs();
        Intent intent = LandingActivity.intent_factory(this);
        startActivity(intent);
    }

    private void removeUserFromPrefs(){
        mEdit.remove(USER_KEY);
        mEdit.commit();
        mEdit.apply();
    }

    private void getDatabase(){
        //mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).allowMainThreadQueries().build().getUserDAO();

    }
    private void getPrefs(){
        mPrefs = getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        mEdit = mPrefs.edit();
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
