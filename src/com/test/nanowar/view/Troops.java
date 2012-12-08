/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.test.nanowar.MainLayout;
import com.test.nanowar.R;
import com.test.nanowar.model.Player;
import com.test.nanowar.model.RealPoint;

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
    protected int textColor;
    protected com.test.nanowar.model.Troops model;
    
    protected Point center;
    protected RealPoint realCenter;
    protected RealPoint step;
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
        Troops troops = new Troops(layout);
        troops.setModel(model);
        troops.setOwner(model.getOwner());
        //troops.init(layout);

        troops.setCenter(layout.convertToPx(model.getRelativeCenter()));
        RealPoint realCenter = new RealPoint(troops.getCenter());
        troops.setRealCenter(realCenter);
        troops.setPosition(troops.computePosition());
        
        Point dest = model.getDestination().getView().getCenter();
        troops.setStep(new RealPoint(dest.x - realCenter.getX(), dest.y - realCenter.getY()).normalise());
        troops.buildView();
        
        Log.d("kolejny count", model.count().toString());

        return troops;
    }

    public void setOwner(Player owner) {
        if (owner.isComputer()) {
            setResource(R.raw.cellinnercomputer);
            textColor = Color.WHITE;
        } else if(owner.isUser()) {
            setResource(R.raw.cellinneruser);
            textColor = Color.WHITE;
        }
        else {
            setResource(R.raw.cellinnernone);
            textColor = Color.WHITE;
        }
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

    public RealPoint getRealCenter() {
        return realCenter;
    }

    public void setRealCenter(RealPoint realCenter) {
        this.realCenter = realCenter;
    }

    public RealPoint getStep() {
        return step;
    }

    public void setStep(RealPoint step) {
        this.step = step;
    }
    
    protected void buildView() {
        buildContainer();
        buildBackground();
        buildCountElement();
    }

    protected void buildContainer() {
        // troops container
        int actualRadius = layout.getRadius(computeRadius());
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(actualRadius, actualRadius);
        params.alignWithParent = true;
        params.leftMargin = center.x - actualRadius;
        params.topMargin = center.y - actualRadius;
        
        final Troops temp = this;
        layout.post(new Runnable() {
            public void run() {
                layout.addView(temp, params);
            }
        });
        
        //layout.addView(this, params);
    }

    protected void buildBackground() {
        background = new ImageView(layout.getContext());
        background.setImageDrawable(resource.createPictureDrawable());

        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        final Troops temp = this;
        this.post(new Runnable() {
            public void run() {
                temp.addView(background, params);
            }
        });
        
        //this.addView(background, params);
    }

    protected void buildCountElement() {
        count = new TextView(layout.getContext());
        count.setText(Integer.toString(model.count()) + " ");
        count.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        count.setTextColor(textColor);
        count.setTextSize(10);

        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        
        final Troops temp = this;
        this.post(new Runnable() {
            public void run() {
                temp.addView(count, params);
            }
        });
        
        this.addView(count, params);
        
        Log.d("tutajTezLicze", Integer.toString(model.count()));
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
        
        realCenter.add(step);
        center.x = (int)Math.floor(realCenter.getX());
        center.y = (int)Math.floor(realCenter.getY());
        position = computePosition();
        
        int actualRadius = layout.getRadius(computeRadius());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(actualRadius, actualRadius);
        params.alignWithParent = true;
        params.leftMargin = center.x - actualRadius;
        params.topMargin = center.y - actualRadius;
        layout.updateViewLayout(this, params);
        
        //Log.d("position", "x :" + realCenter.getX() + " y: " + realCenter.getY());
        //Log.d("step", "x :" + step.getX() + " y: " + step.getY());
        
    }
    
    public void remove() {
        final Troops temp = this;
        ((ViewGroup)this.getParent()).post(new Runnable() {
            public void run() {
                ((ViewGroup)temp.getParent()).removeView(temp);
            }
        });
    }
    
    public boolean destinationReached() {
        return position.intersect(model.getDestination().getView().getPosition());
    }

    private Rect computePosition() {
        int actualRadius = layout.getRadius(computeRadius());
        return new Rect(center.x - actualRadius, center.y - actualRadius, center.x + actualRadius, center.y + actualRadius);
    }
    
    public double computeRadius() {
        return Math.max( Math.min(1, model.count()/com.test.nanowar.model.Troops.BASE) * Troops.MAX_RADIUS_PERCENTAGE, Troops.MIN_RADIUS_PERCENTAGE);
    }
}
