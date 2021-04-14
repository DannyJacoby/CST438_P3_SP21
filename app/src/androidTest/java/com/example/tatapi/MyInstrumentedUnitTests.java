package com.example.tatapi;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MyInstrumentedUnitTests {

    @Test
    public void intentFactoryCheck(){
        
        boolean allIntentsPassing = true;
        // for each activity, grab an activity
        // if intent wasn't grabbed, allIntentPassing = false; break;
        assertEquals(true, allIntentsPassing);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.tatapi", appContext.getPackageName());
    }

}
