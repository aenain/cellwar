/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.view;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
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
public class Tower extends RelativeLayout {
    public static final int MIN_RADIUS_PERCENTAGE = 6;
    public static final int MAX_RADIUS_PERCENTAGE = 16;
    protected SVG innerResource, outerResource;
    protected int textColor;
    protected com.test.nanowar.model.Tower model;
    public ImageView outerBackground, innerBackground;
    public TextView count;
    protected Point center;
    protected Rect position;
    protected MainLayout layout;
    protected long touchStartAt;

    private Tower(MainLayout layout) {
        super(layout.getContext());
        this.layout = layout;
    }

    public static Tower createForModel(com.test.nanowar.model.Tower model, MainLayout layout) {
        Tower tower = new Tower(layout);
        tower.setOwner(model.getOwner());
        tower.setModel(model);

        tower.setCenter(layout.convertToPx(model.getRelativeCenter()));
        tower.setPosition(tower.computePosition());
        tower.buildView();

        return tower;
    }

    protected Rect computePosition() {
        int actualRadius = layout.getRadius(computeExternalRadius());
        return new Rect(center.x - actualRadius, center.y - actualRadius, center.x + actualRadius, center.y + actualRadius);
    }

    protected void setPosition(Rect position) {
        this.position = position;
    }

    protected Rect getPosition() {
        return position;
    }
    
    public Point getCenter() {
        return this.center;
    }
    
    protected void buildView() {
        buildContainer();
        buildOuterBackground();
        buildInnerBackground();
        buildCountElement();
        setClickCallback();
    }

    protected void buildContainer() {
        // tower container
        int actualRadius = layout.getRadius(computeExternalRadius());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(actualRadius, actualRadius);
        params.alignWithParent = true;
        params.leftMargin = center.x - actualRadius;
        params.topMargin = center.y - actualRadius;
        layout.addView(this, params);
    }

    protected void buildOuterBackground() {
        outerBackground = new ImageView(layout.getContext());
        outerBackground.setImageDrawable(outerResource.createPictureDrawable());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        this.addView(outerBackground, params);
    }

    protected void buildInnerBackground() {
        int actualRadius = layout.getRadius(computeInternalRadius());
        innerBackground = new ImageView(layout.getContext());
        innerBackground.setImageDrawable(innerResource.createPictureDrawable());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(actualRadius, actualRadius);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(innerBackground, params);
    }

    protected void buildCountElement() {
        count = new TextView(layout.getContext());
        synchronized(model.getTroopsCount()) {
            int troopsCount = (int)Math.floor(model.getTroopsCount());
            count.setText(Integer.toString(troopsCount) + " ");
        }

        count.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        count.setTextColor(textColor);
        count.setTextSize(10);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(count, params);
    }

    // ten event dotyczy tylko uzytkownika
    protected void setClickCallback() {
        setClickable(false);
        setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        touchStartAt = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        long selectionDuration = System.currentTimeMillis() - touchStartAt;
                        model.getGamePanel().getUserPlayer().selectTower(model, selectionDuration);
                        break;
                }

                return true;
            }
        });
    }

    public void select(com.test.nanowar.model.Tower.Selection selection) {
        if (model.getOwner().isUser()) {
            if (selection == com.test.nanowar.model.Tower.Selection.SELECTED) {
                setBackgroundColor(Color.BLACK);
            }
            else {
                setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    protected void setCenter(Point center) {
        this.center = center;
    }

    public void changeOwner(Player owner) {
        setOwner(owner);
        
        innerBackground.post(new Runnable() {
            public void run() {
                innerBackground.setImageDrawable(innerResource.createPictureDrawable());
            }
        });
        outerBackground.post(new Runnable() {
            public void run() {
                outerBackground.setImageDrawable(outerResource.createPictureDrawable());
            }
        });
        
        /*innerBackground.setImageDrawable(innerResource.createPictureDrawable());
        outerBackground.setImageDrawable(outerResource.createPictureDrawable());*/
    }

    public void setOwner(Player owner) {
        if (owner.isComputer()) {
            setOuterResource(R.raw.celloutercomputer);
            setInnerResource(R.raw.cellinnercomputer);
            setTextColor(Color.WHITE);
        } else if (owner.isUser()) {
            setOuterResource(R.raw.cellouteruser);
            setInnerResource(R.raw.cellinneruser);
            setTextColor(Color.WHITE);
        } else {
            setOuterResource(R.raw.cellouternone);
            setInnerResource(R.raw.cellinnernone);
            setTextColor(Color.WHITE);
        }
    }

    // w procentach
    public double computeExternalRadius() {
        double ratio = Math.sqrt((double) model.getCapacity() / com.test.nanowar.model.Tower.MAX_CAPACITY);
        return Math.max(ratio * Tower.MAX_RADIUS_PERCENTAGE, Tower.MIN_RADIUS_PERCENTAGE);
    }

    // w procentach
    public double computeInternalRadius() {
        double ratio = Math.sqrt((double) model.getTroopsCount() / model.getCapacity());
        return Math.min(Tower.MAX_RADIUS_PERCENTAGE, Math.max(ratio * computeExternalRadius(), Tower.MIN_RADIUS_PERCENTAGE));
    }

    public void update() {
        int actualRadius = layout.getRadius(computeInternalRadius());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(actualRadius, actualRadius);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        innerBackground.setLayoutParams(params);

        synchronized(model.getTroopsCount()) {
            int troopsCount = (int) Math.floor(model.getTroopsCount());
            count.setText(Integer.toString(troopsCount));
        }
    }

    protected void setModel(com.test.nanowar.model.Tower model) {
        this.model = model;
    }

    protected void setOuterResource(int resourceId) {
        outerResource = SVGParser.getSVGFromResource(getResources(), resourceId);
    }

    protected void setInnerResource(int resourceId) {
        innerResource = SVGParser.getSVGFromResource(getResources(), resourceId);
    }

    protected void setTextColor(int color) {
        textColor = color;
    }
}
