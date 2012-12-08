package com.test.nanowar;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.test.nanowar.model.MainGamePanel;

public class MainActivity extends Activity {
    public static int BACKGROUND_COLOR = Color.rgb(16, 171, 226);
    protected MainLayout layout;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // settings of layout and window
        layout = new MainLayout(this);
        RelativeLayout.LayoutParams mainParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(mainParams);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(layout);

        // background        
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(BACKGROUND_COLOR);
        layout.addView(imageView, mainParams);

        // models
        int levelNumber = 1;
        MainGamePanel gamePanel = new MainGamePanel(layout, levelNumber);
        gamePanel.initLevel();
        // gamePanel.startGame();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
