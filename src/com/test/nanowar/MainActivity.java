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

public class MainActivity extends Activity {

    protected RelativeLayout l;

    protected void addTower(Point center, int radius, Integer troopsCount) {
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
            svg = SVGParser.getSVGFromResource(getResources(), R.raw.towerouter);
            outerBackground.setImageDrawable(svg.createPictureDrawable());
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            tower.addView(outerBackground, params);

            // inner background
            ImageView innerBackground = new ImageView(this);
            svg = SVGParser.getSVGFromResource(getResources(), R.raw.towerinter);
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
            numberOfTroops.setTextColor(Color.GRAY);
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
        addTower(getPoint(50, 50), getRadius(8), 27);
        addTower(getPoint(75, 75), getRadius(16), 135);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
