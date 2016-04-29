package com.heiliuer.androidmic_client;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.io.File;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        Log.v("myLog",new File(MainActivity.PATH_NAME).getParent());
        assertTrue(new File(MainActivity.PATH_NAME).getPath().indexOf(".data") == -1);
    }


}