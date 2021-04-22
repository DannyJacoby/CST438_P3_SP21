package com.example.tatapi;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.example.tatapi.db.User;
import com.parse.Parse;
import com.parse.ParseObject;

public class TATAPI extends Application {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());

        //testing writes
        User user = new User("testuser", "testpass", 1);
        ParseObject newPlayer = new ParseObject("Player");
        newPlayer.put("username", user.getUsername());
        newPlayer.put("password", user.getPassword());
        newPlayer.put("level", user.getLevel());
        newPlayer.put("health", user.getHealth());
        newPlayer.put("overallHealth", user.getOverAllHealth());
        newPlayer.put("magic", user.getMagic());
        newPlayer.put("strength", user.getStrength());
        newPlayer.saveInBackground(e -> {
            if (e != null){
                Log.e("LoginActivity", e.getLocalizedMessage());
            }else{
                Log.d("LoginActivity","Object saved.");
            }
        });
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}