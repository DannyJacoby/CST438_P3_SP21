package com.example.tatapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.TextKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.tatapi.models.Enemy;
import com.example.tatapi.models.User;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

public class ModifyUserActivity extends AppCompatActivity {

    private EditText userNameField;
    private TextView userNameTag;
    private EditText userHealthField;
    private TextView userHealthTag;
    private EditText userStrField;
    private TextView userStrTag;
    private EditText userDefField;
    private TextView userDefTag;
    private EditText userLevelField;
    private TextView userLevelTag;
    private EditText userScoreField;
    private TextView userScoreTag;
    private Switch userAdminSwitch;
    private TextView userAdminTag;
    private Button loadUserButton;
    private Button saveUserButton;
    private Button deleteUserButton;

    ParseUser tempUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);
        wireUp();
    }

    private void wireUp(){
        userNameField = findViewById(R.id.UserNameField);
        userNameTag = findViewById(R.id.UserNameTag);
        userHealthField = findViewById(R.id.UserHealthField);
        userHealthTag = findViewById(R.id.UserHealthTag);
        userStrField = findViewById(R.id.UserStrengthField);
        userStrTag = findViewById(R.id.UserStrengthTag);
        userDefField = findViewById(R.id.UserDefenseField);
        userDefTag = findViewById(R.id.UserDefenseTag);
        userLevelField = findViewById(R.id.UserLevelField);
        userLevelTag = findViewById(R.id.UserLevelTag);
        userScoreField = findViewById(R.id.UserHsField);
        userScoreTag = findViewById(R.id.UserHsTag);
        userAdminSwitch = findViewById(R.id.UserAdminToggle);
        userAdminTag = findViewById(R.id.UserAdminTag);
        loadUserButton = findViewById(R.id.loadUserBtn);
        saveUserButton = findViewById(R.id.SaveUserBtn);
        deleteUserButton = findViewById(R.id.DeleteUserBtn);

        toggleFieldsAndButtons(false);

        loadUserButton.setOnClickListener(v -> {
            String name = userNameField.getText().toString();
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", name);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                        if (e == null && users.size() != 0) {
                            tempUser = users.get(0);
                            userHealthField.setText(String.valueOf(tempUser.get("overallHealth")));
                            userStrField.setText(String.valueOf(tempUser.get("strength")));
                            userDefField.setText(String.valueOf(tempUser.get("defense")));
                            userLevelField.setText(String.valueOf(tempUser.get("level")));
                            userScoreField.setText(String.valueOf(tempUser.get("highScore")));
                            if((boolean)tempUser.get("isAdmin")){
                                userAdminSwitch.setChecked(true);
                            } else {
                                userAdminSwitch.setChecked(false);
                            }
                            toggleFieldsAndButtons(true);
                        } else {
                            if (users.size() == 0) {
                                snackMaker("No such user found.");
                            } else {
                                snackMaker(e.getMessage());
                            }
                        }
                }
            });
        });

        saveUserButton.setOnClickListener(v -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("UserIdToMod", tempUser.getObjectId());
            params.put("health", Integer.parseInt(userHealthField.getText().toString()));
            params.put("overallHealth", Integer.parseInt(userHealthField.getText().toString()));
            params.put("strength", Integer.parseInt(userStrField.getText().toString()));
            params.put("defense", Integer.parseInt(userDefField.getText().toString()));
            params.put("level", Integer.parseInt(userLevelField.getText().toString()));
            params.put("highScore", Integer.parseInt(userScoreField.getText().toString()));
            if(userAdminSwitch.isChecked()){
                params.put("isAdmin", true);
            } else {
                params.put("isAdmin", false);
            }

            ParseCloud.callFunctionInBackground("modifyUser", params, new FunctionCallback<Boolean>() {
                @Override
                public void done(Boolean object, ParseException e) {
                    if(e==null&&object){
                        toggleFieldsAndButtons(false);
                        snackMaker("success");
                    }
                    else{
                        snackMaker(e.getMessage());
                    }
                }
            });
        });

        deleteUserButton.setOnClickListener(v -> {
            AlertDialog.Builder warning = new AlertDialog.Builder(this);
            warning.setCancelable(true);
            warning.setTitle("Are you sure?");
            warning.setMessage("Really delete this user?");
            warning.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HashMap<String, Object> params = new HashMap<>();
                            params.put("UserIdToDelete", tempUser.getObjectId());

                            ParseCloud.callFunctionInBackground("deleteUser", params, new FunctionCallback<Boolean>() {
                                @Override
                                public void done(Boolean object, ParseException e) {
                                    if(e==null&&object){
                                        toggleFieldsAndButtons(false);
                                        snackMaker("User deleted");
                                    }
                                    else{
                                        snackMaker(e.getMessage());
                                    }
                                }
                            });
                        }
                    });
            warning.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    snackMaker("User delete cancelled");
                }
            });

            AlertDialog dialog = warning.create();
            dialog.show();
        });
    }

    public static Intent intent_factory(Context context) {
        Intent intent = new Intent(context, ModifyUserActivity.class);
        return intent;
    }

    private void snackMaker(String message) {
        Snackbar snackBar = Snackbar.make(findViewById(R.id.ModUserLayout),
                message,
                Snackbar.LENGTH_SHORT);
        snackBar.show();
    }

    private void toggleEditText(EditText editText, boolean active) {
        if(active){
            editText.setEnabled(true);
        } else {
            editText.setEnabled(false);
        }
    }

    private void toggleFieldsAndButtons(boolean show) {
        if (show) {
            toggleEditText(userNameField, false);
            userNameTag.setVisibility(View.GONE);
            userHealthField.setVisibility(View.VISIBLE);
            userHealthTag.setVisibility(View.VISIBLE);
            userStrField.setVisibility((View.VISIBLE));
            userStrTag.setVisibility(View.VISIBLE);
            userDefField.setVisibility(View.VISIBLE);
            userDefTag.setVisibility(View.VISIBLE);
            userLevelField.setVisibility(View.VISIBLE);
            userLevelTag.setVisibility(View.VISIBLE);
            userScoreField.setVisibility(View.VISIBLE);
            userScoreTag.setVisibility(View.VISIBLE);
            userAdminSwitch.setVisibility(View.VISIBLE);
            userAdminTag.setVisibility(View.VISIBLE);
            loadUserButton.setVisibility(View.GONE);
            saveUserButton.setVisibility(View.VISIBLE);
            deleteUserButton.setVisibility(View.VISIBLE);
        } else {
            toggleEditText(userNameField, true);
            userNameTag.setVisibility(View.VISIBLE);
            userHealthField.setVisibility(View.GONE);
            userHealthTag.setVisibility(View.GONE);
            userStrField.setVisibility((View.GONE));
            userStrTag.setVisibility(View.GONE);
            userDefField.setVisibility(View.GONE);
            userDefTag.setVisibility(View.GONE);
            userLevelField.setVisibility(View.GONE);
            userLevelTag.setVisibility(View.GONE);
            userScoreField.setVisibility(View.GONE);
            userScoreTag.setVisibility(View.GONE);
            userAdminSwitch.setVisibility(View.GONE);
            userAdminTag.setVisibility(View.GONE);
            loadUserButton.setVisibility(View.VISIBLE);
            saveUserButton.setVisibility(View.GONE);
            deleteUserButton.setVisibility(View.GONE);
        }
    }
}