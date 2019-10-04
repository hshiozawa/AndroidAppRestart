package com.x.androidapprestart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class RestartActivity extends Activity {
    public static final String EXTRA_MAIN_PID = "RestartActivity.main_pid";

    public static Intent createIntent(Context context) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), RestartActivity.class.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(RestartActivity.EXTRA_MAIN_PID, android.os.Process.myPid());
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kill the main process.
        Intent intent = getIntent();
        int mainBrowserPid = intent.getIntExtra(EXTRA_MAIN_PID, -1);

        assert mainBrowserPid != -1;
        assert mainBrowserPid != android.os.Process.myPid();

        android.os.Process.killProcess(mainBrowserPid);

        // Fire an Intent to restart the app
        Context context = getApplicationContext();
        Intent restartIntent = new Intent(Intent.ACTION_MAIN);
        restartIntent.setClassName(context.getPackageName(), MainActivity.class.getName());
        restartIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(restartIntent);

        // Kill this process.
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
