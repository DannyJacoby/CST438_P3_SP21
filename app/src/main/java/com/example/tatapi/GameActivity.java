package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.tatapi.db.AppDatabase;
import com.example.tatapi.db.User;
import com.example.tatapi.db.UserDAO;
import com.google.android.material.snackbar.Snackbar;

public class GameActivity extends AppCompatActivity {
    private static final String PREF_KEY = "com.example.tatapi.PREFERENCES_KEY";
    private static final String USER_KEY = "com.example.tatapi.USERS_KEY";

    private int mUserId = -1;
    private User mUser;

    private UserDAO mUserDAO;

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

    public static Intent intent_factory(Context context, int userId){
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("userId", userId);
        return intent;
    }
}