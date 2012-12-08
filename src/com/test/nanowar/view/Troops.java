/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.test.nanowar.MainLayout;
import com.test.nanowar.R;
import com.test.nanowar.model.Player;

/**
 *
 * @author artur
 */
public class Troops extends RelativeLayout {
    
    public static final int MIN_RADIUS_PERCENTAGE = 3;
    public static final int MAX_RADIUS_PERCENTAGE = 10;

    protected SVG resource;
    protected ImageView background;
    protected TextView count;
    protected com.test.nanowar.model.Troops model;
    
    protected Point center;
    protected Rect position;
    protected MainLayout layout;

    private Troops(Context context) {
        super(context);
    }
    
    private Troops(MainLayout layout) {
        super(layout.getContext());
        this.layout = layout;
    }

    public static Troops createForPlayer(com.test.nanowar.model.Troops model, MainLayout layout) {
        Troops troops = new Troops(layout.getContext());
        troops.setModel(model);
        troops.setOwner(model.getOwner());
        //troops.init(layout);

        troops.setCenter(layout.convertToPx(model.getRelativeCenter()));
        troops.setPosition(troops.computePosition());
        troops.buildView();

        return troops;
    }
    
    /*  old
     * 
     * public static Troops createForPlayer(com.test.nanowar.model.Troops model, Context context, RelativeLayout layout) {
        Troops troops = new Troops(context);
        troops.setModel(model);
        //troops.init(layout);

        troops.setOwner(model.getOwner());
        troops.initAnimation();

        return troops;
    }*/

    public void setOwner(Player owner) {
        if (owner != null) {
            if (owner.isComputer()) {
                setResource(R.raw.cellinnercomputer);
                setTextColor(Color.WHITE);
            } else {
                setResource(R.raw.cellinneruser);
                setTextColor(Color.WHITE);
            }
        } else {
            setResource(R.raw.cellinnernone);
            setTextColor(Color.WHITE);
        }

        background.setImageDrawable(resource.createPictureDrawable());
    }

    protected void setModel(com.test.nanowar.model.Troops model) {
        this.model = model;
    }

    protected SVG setResource(int resourceId) {
        return resource = SVGParser.getSVGFromResource(getResources(), resourceId);
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public Rect getPosition() {
        return position;
    }

    public void setPosition(Rect position) {
        this.position = position;
    }
    
    protected void buildView() {
        buildContainer();
        buildBackground();
        buildCountElement();
    }

    protected void buildContainer() {
        // tower container
        int actualRadius = layout.getRadius(computeRadius());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(actualRadius, actualRadius);
        params.alignWithParent = true;
        params.leftMargin = center.x - actualRadius;
        params.topMargin = center.y - actualRadius;
        layout.addView(this, params);
    }

    protected void buildBackground() {
        /*background = new ImageView(layout.getContext());
        background.setImageDrawable(outerResource.createPictureDrawable());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        this.addView(outerBackground, params);*/
    }

    protected void buildCountElement() {
        /*count = new TextView(layout.getContext());
        count.setText(Integer.toString(model.getTroopsCount()) + " ");
        count.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        count.setTextColor(textColor);
        count.setTextSize(10);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(count, params);*/
    }

    /*protected void init(Context context, MainLayout layout) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (1.5*radius), (int) (1.5*radius));
        params.alignWithParent = true;
        params.leftMargin = center.x - radius;
        params.topMargin = center.y - radius;
        layout.addView(this, params);
    }*/

    /*protected void initAnimation() {
        // utworzenie animacji, uruchomienie i sprawdzanie czy się dotarło, jeśli tak, to wywołaj metodę z modelu
    }*/

    protected void setTextColor(int color) {
        count.setTextColor(color);
    }
    
    public void update() {
        
    }

    private Rect computePosition() {
        int actualRadius = layout.getRadius(computeRadius());
        return new Rect(center.x - actualRadius, center.y - actualRadius, center.x + actualRadius, center.y + actualRadius);
    }
    
    public double computeRadius() {
        return Math.max( Math.min(1, model.count()/com.test.nanowar.model.Troops.BASE) * Troops.MAX_RADIUS_PERCENTAGE, Troops.MIN_RADIUS_PERCENTAGE);
    }
}
