package com.test.nanowar;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    public static int BACKGROUND_COLOR = Color.rgb(16, 171, 226);

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void openLevelChooser(View v) {
        Log.d("ACTIVITY", "open level chooser");
        Intent intent = new Intent(MainActivity.this, LvlChooserActivity.class);
        startActivity(intent);
    }

    public void openCredits(View v) {
        Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
        startActivity(intent);
    }

    public void openTutorial(View v) {
        Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
        startActivity(intent);
    }
}
