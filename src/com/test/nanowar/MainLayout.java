/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

/**
 *
 * @author artur
 */
public class MainLayout extends RelativeLayout {
    public MainLayout(Activity activity) {
        super(activity);
    }

    public Point convertToPx(Point relativePoint) {
        return getPoint(relativePoint.x, relativePoint.y);
    }

    public Point getPoint(double percentageX, double percentageY) {
        int x, y;
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        x = (int)Math.round(metrics.widthPixels * percentageX / 100);
        y = (int)Math.round(metrics.heightPixels * percentageY / 100);

        return new Point(x, y);
    }

    public int getRadius(double percentage) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int)Math.round(Math.min(metrics.heightPixels, metrics.widthPixels) * percentage / 100);
    }
}