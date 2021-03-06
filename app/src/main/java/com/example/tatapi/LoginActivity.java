package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    protected static final String PREF_KEY = LandingActivity.PREF_KEY;
    protected static final String USER_KEY = LandingActivity.USER_KEY;

    public boolean m_amLogin;

    public TextView welcomeText;
    public Button continueBtn;

    private EditText m_UsernameField;
    private String m_Username;
    private EditText m_PasswordField;
    private String m_Password;

    private SharedPreferences mPrefs = null;
    private SharedPreferences.Editor mEdit;

    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        mPrefs = getSharedPreferences(PREF_KEY, 0);
        mEdit = mPrefs.edit();
    }

    private void addUserToPrefs(String mUserId){
        if(mPrefs == null){
            getPrefs();
        }
        mEdit.putString(USER_KEY, mUserId);
        mEdit.commit();
        mEdit.apply();
    }

    private void loginUser(){
        if(m_amLogin){
            ParseUser.logInInBackground(m_Username, m_Password, (parseUser, e) -> {
                if (parseUser != null) {
                    mUserId = parseUser.getObjectId();
                    snackMaker("Successful Login");
                    loginSuccess();
                } else {
                    ParseUser.logOut();
                    snackMaker(e.getMessage());
                }
            });
        } else {
                ParseUser user = new ParseUser();
                user.setUsername(m_Username);
                user.setPassword(m_Password);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            snackMaker("Account Created.");

                        } else {
                            ParseUser.logOut();
                            snackMaker(e.getMessage());
                        }
                    }
                });
        }
    }

    private void loginSuccess(){
        addUserToPrefs(mUserId);
        Intent intent = HomeActivity.intent_factory(this);
        startActivity(intent);
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

    public static Intent intent_factory(Context context, boolean login){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("amILogin", login);
        return intent;
    }
}