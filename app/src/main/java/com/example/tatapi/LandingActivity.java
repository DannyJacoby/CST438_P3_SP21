package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.tatapi.db.AppDatabase;
import com.example.tatapi.db.User;
import com.example.tatapi.db.UserDAO;
import com.google.android.material.snackbar.Snackbar;

import com.parse.Parse;
import java.util.List;

public class LandingActivity extends AppCompatActivity {
    protected static final String PREF_KEY = "com.example.tatapi.PREFERENCES_KEY";
    protected static final String USER_KEY = "com.example.tatapi.USERNAME_KEY";

    public Button mLoginBtn;
    public Button mCreateBtn;

    private SharedPreferences mPrefs = null;
    private SharedPreferences.Editor mEdit;

    private String mUserId = "none";

    private UserDAO mUserDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        
        wireUp();
        getDatabase();
        checkForUser();

    }

    private void wireUp(){
        mLoginBtn = findViewById(R.id.loginMainBtn);
        mCreateBtn = findViewById(R.id.createMainBtn);

        mLoginBtn.setOnClickListener(v -> {
            Intent intent = LoginActivity.intent_factory(getApplicationContext(), true);
            startActivity(intent);
        });

        mCreateBtn.setOnClickListener(v -> {
            Intent intent = LoginActivity.intent_factory(getApplicationContext(), false);
            startActivity(intent);
        });

    }

    private void getPrefs(){
        mPrefs = getSharedPreferences(PREF_KEY, 0);
        mEdit = mPrefs.edit();
    }

    private void checkForUser(){
        if(mPrefs == null){
            getPrefs();
        }

        mUserId = mPrefs.getString(USER_KEY, "none");
        if(mUserId != "none"){
            Intent intent = HomeActivity.intent_factory(this);
            startActivity(intent);
            return;
        }

        List<User> users = mUserDAO.getAllUsers();
        if(users.size() <= 0){
            User defaultUser = new User("fake", "fake", 1, true);
            User secondsUser = new User("fake2", "fake2", 1, false);
            mUserDAO.insert(defaultUser, secondsUser);
        }

    }

    private void snackMaker(String message){
        Snackbar snackBar = Snackbar.make(findViewById(R.id.LandingLayout),
                message,
                Snackbar.LENGTH_SHORT);
        snackBar.show();
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).allowMainThreadQueries().build().getUserDAO();

    }

    public static Intent intent_factory(Context context){
        Intent intent = new Intent(context, LandingActivity.class);
        return intent;
    }

}