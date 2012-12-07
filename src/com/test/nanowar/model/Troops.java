/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import android.graphics.Point;
import android.graphics.Rect;
import static java.lang.Math.*;

/**
 *
 * @author artur
 * klasa reprezentujaca jednostki, ktore sie przemieszczaja miedzy wiezami
 * 
 */
public class Troops extends GameObject {
    public static final Integer RADIUS = 20;

    protected Integer count;
    protected Tower source, destination;
    protected RealPoint realCenter, step;

    public Troops(Player owner, Integer count, Point center) {
        super(owner, center, RADIUS);
        this.realCenter = new RealPoint(center.x, center.y);
        this.owner = owner;
        this.count = count;
    }

    public void sendBetween(Tower source, Tower destination) {
        this.source = source;
        this.destination = destination;
        
        step = new RealPoint(destination.center.x - realCenter.x, destination.center.y - realCenter.y);
        step.normalise();
        // TODO! obliczenie odleglosci, wyznaczenie sciezki, narysowanie linii, uruchomienie animacji
        // w animacji sprawdzamy, czy sie nie przecielismy z docelowa wieza (jesli tak,
        // wywolaj destination.troopsArrived(this)
    }

    public Player getOwner() {
        return owner;
    }

    public Integer count() {
        return count;
    }
    
    public void update() {
        realCenter.add(step);
        center.x = (int)floor(realCenter.getX());
        center.y = (int)floor(realCenter.getY());
    }
    
    public boolean destinationReached() {
        if(destination != null) {
            return this.location.intersect(destination.getLocation());
        }
        else {
            // nie powinno sie wydarzyc, ale jakby co...
            return false;
        }
    }
    
    public Tower getDestination() {
        return this.destination;
    }
}
