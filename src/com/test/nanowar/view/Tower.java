/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.view;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
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
    protected ImageView outerBackground, innerBackground;
    protected TextView count;
    protected Point center;
    protected Rect position;
    protected MainLayout layout;

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

    protected void buildView() {
        buildContainer();
        buildOuterBackground();
        buildInnerBackground();
        buildCountElement();
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
        count.setText(Integer.toString(model.getTroopsCount()) + " ");
        count.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        count.setTextColor(textColor);
        count.setTextSize(10);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(count, params);
    }

    protected void setCenter(Point center) {
        this.center = center;
    }

    public void changeOwner(Player owner) {
        setOwner(owner);
        innerBackground.setImageDrawable(innerResource.createPictureDrawable());
        outerBackground.setImageDrawable(outerResource.createPictureDrawable());
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
        double ratio = Math.sqrt((double)model.getCapacity() / com.test.nanowar.model.Tower.MAX_CAPACITY);
        return Math.max(ratio * Tower.MAX_RADIUS_PERCENTAGE, Tower.MIN_RADIUS_PERCENTAGE);
    }

    // w procentach
    public double computeInternalRadius() {
        double ratio = Math.sqrt((double)model.getTroopsCount() / model.getCapacity());
        return Math.min(Tower.MAX_RADIUS_PERCENTAGE, Math.max(ratio * computeExternalRadius(), Tower.MIN_RADIUS_PERCENTAGE));
    }

    public void update() {
        ViewGroup.LayoutParams params = innerBackground.getLayoutParams();
        int actualRadius = layout.getRadius(computeInternalRadius());
        params.height = 2 * actualRadius;
        params.width = 2 * actualRadius;
        innerBackground.setLayoutParams(params);
        count.setText(model.getTroopsCount().toString());
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
