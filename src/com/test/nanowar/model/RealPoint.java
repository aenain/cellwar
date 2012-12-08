/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import android.graphics.Point;
import static java.lang.Math.*;

/**
 *
 * @author KaMyLuS
 */
public class RealPoint {
    
    static final double LEN = 1;
    double x, y;
    
    RealPoint() {
        x = y = 0;
    }

    public RealPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public RealPoint(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public void add(RealPoint p) {
        this.y += p.getY();
        this.x += p.getX();
    }
    
    public double getLength() {
        return sqrt(x*x + y*y);
    }
    
    public RealPoint normalise() {
        double len = getLength();
        x /= (LEN/len);
        y /= (LEN/len);
        
        return new RealPoint(x, y);
    }
}
