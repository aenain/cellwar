/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.view;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.test.nanowar.R;
import com.test.nanowar.model.Player;

/**
 *
 * @author artur
 */
public class Tower extends RelativeLayout {
    protected SVG innerResource, outerResource;
    protected int textColor;
    protected com.test.nanowar.model.Tower model;

    protected ImageView outerBackground, innerBackground;
    protected TextView count;

    private Tower(Context context) {
        super(context);
    }

    public static Tower createForModel(com.test.nanowar.model.Tower model, Context context) {
        Tower tower = new Tower(context);
        tower.setOwner(model.getOwner());
        tower.setModel(model);

        return tower;
    }

    public void setOwner(Player owner) {
        if (owner.isComputer()) {
            setOuterResource(R.raw.celloutercomputer);
            setInnerResource(R.raw.cellinnercomputer);
            setTextColor(Color.WHITE);
        }
        else if (owner.isUser()) {
            setOuterResource(R.raw.cellouteruser);
            setInnerResource(R.raw.cellinneruser);
            setTextColor(Color.WHITE);
        }
        else {
            setOuterResource(R.raw.cellouternone);
            setInnerResource(R.raw.cellinnernone);
            setTextColor(Color.WHITE);
        }
    }

    public void applyChanges() {
        
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
