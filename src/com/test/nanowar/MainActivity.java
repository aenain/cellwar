package com.test.nanowar;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.test.nanowar.model.Player;

public class MainActivity extends Activity {
    protected RelativeLayout l;

    protected void addTower(Point center, int radius, Integer troopsCount, Player owner) {
        int outerResourceId, innerResourceId, textColor;

        if (owner.getPlayerType() == Player.PlayerType.COMPUTER) {
            outerResourceId = R.raw.celloutercomputer;
            innerResourceId = R.raw.cellinnercomputer;
            textColor = Color.rgb(255, 255, 255);
        }
        else if (owner.getPlayerType() == Player.PlayerType.USER) {
            outerResourceId = R.raw.cellouteruser;
            innerResourceId = R.raw.cellinneruser;
            textColor = Color.rgb(16, 171, 226);
        }
        else {
            outerResourceId = R.raw.cellouternone;
            innerResourceId = R.raw.cellinnernone;
            textColor = Color.rgb(255, 255, 255);
        }
        if (l != null) {
            RelativeLayout.LayoutParams params;
            SVG svg;

            // tower container
            RelativeLayout tower = new RelativeLayout(this);
            params = new RelativeLayout.LayoutParams((int) (1.5*radius), (int) (1.5*radius));
            params.alignWithParent = true;
            params.leftMargin = center.x - radius;
            params.topMargin = center.y - radius;
            l.addView(tower, params);

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
        }
    }

    protected Point getPoint(int percentageX, int percentageY) {
        int x, y;
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        x = (int)Math.round(metrics.widthPixels * percentageX / 100);
        y = (int)Math.round(metrics.heightPixels * percentageY / 100);

        return new Point(x, y);
    }

    protected int getRadius(int percentage) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int)Math.round(Math.min(metrics.heightPixels, metrics.widthPixels) * percentage / 100);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        l = new RelativeLayout(this);
        RelativeLayout.LayoutParams mainParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        l.setLayoutParams(mainParams);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(l);

        // background        
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(Color.rgb(16, 171, 226));
        l.addView(imageView, mainParams);

        // towers
        addTower(getPoint(50, 50), getRadius(8), 27, new Player(Player.PlayerType.USER, null, null));
        addTower(getPoint(25, 50), getRadius(12), 48, null);
        addTower(getPoint(75, 75), getRadius(16), 135, new Player(Player.PlayerType.USER, null, null));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
