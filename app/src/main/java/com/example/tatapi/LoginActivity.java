package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private static final String PREF_KEY = "com.example.tatapi.PREFERENCES_KEY";
    private static final String USERNAME_KEY = "com.example.tatapi.USERNAME_KEY";

    public boolean m_amLogin;

    public TextView welcomeText;
    public Button continueBtn;

    private EditText m_UsernameField;
    private String m_Username;
    private EditText m_PasswordField;
    private String m_Password;

    private SharedPreferences mPrefs = null;
    private SharedPreferences.Editor mEdit;

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
            // check if there is a user in DB
            if(checkForUserInDB()){
                loginUser();
            } else {
                snackMaker( (m_amLogin) ? "User Does Not Exist" : "User Already Exists");
            }

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

    private void addUserToPrefs(String username){
        if(mPrefs == null){
            getPrefs();
        }
        mEdit.putString(USERNAME_KEY, username);
        mEdit.apply();
    }

    private boolean checkForUserInDB(){
        // check if user exists in DB, if so return true
        return true;
    }

    private void loginUser(){
        if(m_amLogin){
            // validate password
            // if so
            addUserToPrefs("0");
        } else {
            // check if password meets requirements
            // if so
            addUserToPrefs("0");
        }



        Intent intent  = HomeActivity.intent_factory(getApplicationContext());
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