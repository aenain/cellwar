package com.test.nanowar;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.test.nanowar.model.Level;
import com.test.nanowar.model.MainGamePanel;
import com.test.nanowar.model.Player;

public class MainActivity extends Activity {
    public static int BACKGROUND_COLOR = Color.rgb(16, 171, 226);
    protected MainLayout layout;

    protected RelativeLayout addTower(Point center, int radius, Integer troopsCount, Player owner) {
        int outerResourceId, innerResourceId, textColor;

        if (owner == null) {
            outerResourceId = R.raw.cellouternone;
            innerResourceId = R.raw.cellinnernone;
            textColor = Color.rgb(255, 255, 255);
        }
        if (owner.isComputer()) {
            outerResourceId = R.raw.celloutercomputer;
            innerResourceId = R.raw.cellinnercomputer;
            textColor = Color.rgb(255, 255, 255);
        }
        else {
            outerResourceId = R.raw.cellouteruser;
            innerResourceId = R.raw.cellinneruser;
            textColor = Color.rgb(16, 171, 226);
        }

        if (layout != null) {
            RelativeLayout.LayoutParams params;
            SVG svg;

            // tower container
            RelativeLayout tower = new RelativeLayout(this);
            params = new RelativeLayout.LayoutParams((int) (1.5*radius), (int) (1.5*radius));
            params.alignWithParent = true;
            params.leftMargin = center.x - radius;
            params.topMargin = center.y - radius;
            layout.addView(tower, params);

            tower.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    v.setBackgroundColor(Color.BLACK);
                }
            });

            if (owner == null || !owner.isUser()) {
                tower.setOnClickListener(null);
            }

            // outer background
            ImageView outerBackground = new ImageView(this);
            svg = SVGParser.getSVGFromResource(getResources(), outerResourceId);
            outerBackground.setImageDrawable(svg.createPictureDrawable());
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            tower.addView(outerBackground, params);

            // inner background
            ImageView innerBackground = new ImageView(this);
            svg = SVGParser.getSVGFromResource(getResources(), innerResourceId);
            innerBackground.setImageDrawable(svg.createPictureDrawable());
            params = new RelativeLayout.LayoutParams(radius, radius);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            tower.addView(innerBackground, params);

            innerBackground.getLayoutParams().height = (int) Math.floor(0.8 * radius);
            innerBackground.getLayoutParams().width = (int) Math.floor(0.8 * radius);
            
            // troops count
            TextView numberOfTroops = new TextView(this);
            numberOfTroops.setText(Integer.toString(troopsCount) + " ");
            numberOfTroops.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            numberOfTroops.setTextColor(textColor);
            numberOfTroops.setTextSize(10);
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            tower.addView(numberOfTroops, params);

            return tower;
        }
        return null;
    }

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
