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
public class Troops extends RelativeLayout {

    protected SVG resource;
    protected ImageView background;
    protected TextView count;
    protected com.test.nanowar.model.Troops model;

    private Troops(Context context) {
        super(context);
    }

    public static Troops createForPlayer(com.test.nanowar.model.Troops model, Context context, RelativeLayout layout) {
        Troops troops = new Troops(context);
        troops.setModel(model);
        //troops.init(layout);

        troops.setOwner(model.getOwner());
        troops.initAnimation();

        return troops;
    }

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

    protected void init(Context context, RelativeLayout layout) {
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (1.5*radius), (int) (1.5*radius));
//        params.alignWithParent = true;
//        params.leftMargin = center.x - radius;
//        params.topMargin = center.y - radius;
//        layout.addView(this, params);
    }

    protected void initAnimation() {
        // utworzenie animacji, uruchomienie i sprawdzanie czy się dotarło, jeśli tak, to wywołaj metodę z modelu
    }

    protected void setTextColor(int color) {
        count.setTextColor(color);
    }
}
