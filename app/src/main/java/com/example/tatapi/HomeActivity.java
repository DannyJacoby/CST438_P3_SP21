package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tatapi.db.AppDatabase;
import com.example.tatapi.db.User;
import com.example.tatapi.db.UserDAO;
import com.google.android.material.snackbar.Snackbar;

public class HomeActivity extends AppCompatActivity {

    private static final String PREF_KEY = "com.example.tatapi.PREFERENCES_KEY";
    private static final String USER_KEY = "com.example.tatapi.USERS_KEY";

    private Button playBtn;
    private Button logoutBtn;

    private int mUserId = -1;
    private User mUser;

    private UserDAO mUserDAO;

    private SharedPreferences mPrefs = null;
    private SharedPreferences.Editor mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        getDatabase();
        wireUp();
        login();

    }

    private void wireUp(){
        playBtn = findViewById(R.id.playBtn);
        logoutBtn = findViewById(R.id.logoutBtn);


        playBtn.setOnClickListener(v -> {
            Intent intent = GameActivity.intent_factory(this);
            startActivity(intent);
            snackMaker("Starting game...");
        });

        logoutBtn.setOnClickListener(v ->{
            logout();
        });
    }



    private void login(){
        if(mPrefs == null){
            getPrefs();
        }
        mUserId = mPrefs.getInt(USER_KEY, -1);
        mUser = mUserDAO.getUserByUserId(mUserId);
    }

    private void logout(){
        // Maybe add alert like "Do you really want to log out? Y/N"
        removeUserFromPrefs();
        Intent intent = LandingActivity.intent_factory(this);
        startActivity(intent);
    }

    private void removeUserFromPrefs(){
        mEdit.remove(USER_KEY);
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).allowMainThreadQueries().build().getUserDAO();

    }
    private void getPrefs(){
        mPrefs = this.getSharedPreferences(PREF_KEY, 0);
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
