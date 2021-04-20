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
import android.widget.EditText;
import android.widget.TextView;

import com.example.tatapi.db.AppDatabase;
import com.example.tatapi.db.User;
import com.example.tatapi.db.UserDAO;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private static final String PREF_KEY = "com.example.tatapi.PREFERENCES_KEY";
    private static final String USER_KEY = "com.example.tatapi.USERNAME_KEY";

    public boolean m_amLogin;

    public TextView welcomeText;
    public Button continueBtn;

    private EditText m_UsernameField;
    private String m_Username;
    private EditText m_PasswordField;
    private String m_Password;

    private SharedPreferences mPrefs = null;
    private SharedPreferences.Editor mEdit;

    private int mUserId = -1;

    private UserDAO mUserDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getDatabase();
        wireUp();
        refreshDisplay();
        getPrefs();

    }

    private void wireUp(){
        m_amLogin = getIntent().getBooleanExtra("amILogin", false);
        welcomeText = findViewById(R.id.welcomeLoginText);
        continueBtn = findViewById(R.id.continueLoginBtn);

        continueBtn.setOnClickListener(v -> {
            grabStringsFromDisplay();
            loginUser();

        });

        m_UsernameField = findViewById(R.id.editTextNameField);
        m_PasswordField = findViewById(R.id.editTextPasswordField);
    }

    private void grabStringsFromDisplay(){
        m_Username = m_UsernameField.getText().toString();
        m_Password = m_PasswordField.getText().toString();
    }

    private void getPrefs(){
        mPrefs = this.getSharedPreferences(PREF_KEY, 0);
        mEdit = mPrefs.edit();
    }

    private void addUserToPrefs(int mUserId){
        if(mPrefs == null){
            getPrefs();
        }
        mEdit.putInt(USER_KEY, mUserId);
        mEdit.apply();
        mEdit.commit();
    }

    private boolean checkForUserInDB(){
        User user = mUserDAO.getUserByUsername(m_Username);
        return user != null;
    }

    private void loginUser(){
        if(validatePassword(m_Password)) {

            mUserId = mUserDAO.getUserByUsername(m_Username).getUserId();

            addUserToPrefs(mUserId);

            Intent intent = HomeActivity.intent_factory(this);
            startActivity(intent);
        }
    }

    private boolean validatePassword(String n_Password){
        if(m_amLogin){
            String password = mUserDAO.getUserByUsername(m_Username).getPassword();
            if(n_Password.equals(password)){
                return true;
            }
            snackMaker("Bad Password");
        } else {
            if(!checkForUserInDB()){
                User user = new User(m_Username, m_Password, 1);
                mUserDAO.insert(user);
                return true;
            }
            snackMaker("Account Already Exists");
        }
        return false;
    }

    private void refreshDisplay(){
        welcomeText.setText( (m_amLogin) ? "Login" : "Create Account" );
        continueBtn.setText( (m_amLogin) ? "Login" : "Create Account" );
    }

    private void snackMaker(String message){
        Snackbar snackBar = Snackbar.make(findViewById(R.id.LoginLayout),
                message,
                Snackbar.LENGTH_SHORT);
        snackBar.show();
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).allowMainThreadQueries().build().getUserDAO();

    }

    public static Intent intent_factory(Context context, boolean login){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("amILogin", login);
        return intent;
    }

}