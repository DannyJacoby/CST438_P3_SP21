package com.example.tatapi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class MyInstrumentedUnitTests {

    @Test
    public void testIntentInMainActivity(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        boolean hasPassed = true;
        try {
            Intent intent = LandingActivity.intent_factory(appContext);
        } catch (Exception e){
            e.printStackTrace();
            hasPassed = false;
        }
        assertEquals(true, hasPassed);
    }

    @Test
    public void intentFactoryCheck(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        PackageManager pm = context.getPackageManager();
        String packageName = context.getApplicationContext().getPackageName();
        ActivityInfo[] list;
        boolean allIntentsPassing = true;
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            list = info.activities;
            for (ActivityInfo activityInfo : list ) {
//                Log.d("LIST", activityInfo.name.substring(19));
            }

        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            allIntentsPassing = false;
        }

        assertEquals(true, allIntentsPassing);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.tatapi", appContext.getPackageName());
    }

}
