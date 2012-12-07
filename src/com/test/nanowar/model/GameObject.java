/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.ViewGroup;

/**
 *
 * @author artur
 */
public class GameObject {
    protected Player owner;
    protected Point center;
    protected Rect location;
    protected ViewGroup viewGroup;
    protected int radius;
    
    public GameObject(Player owner, Point center, int radius) {
        this.owner = owner;
        this.center = center;
        location = new Rect(center.y + radius, center.x - radius, center.x + radius, center.y - radius);
    }

    public GameObject(Player owner, Point center) {
        this.owner = owner;
        this.center = center;
        location = new Rect(center.y + 1, center.x - 1, center.x + 1, center.y - 1);
    }

    public Rect getLocation() {
        return location;
    }

    public void updateLocation(int radius) {
        location.bottom = center.y + radius;
        location.top = center.y - radius;
        location.left = center.x - radius;
        location.right = center.x + radius;
    }
}
