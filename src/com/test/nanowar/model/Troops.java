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
    public static final Integer BASE = 50;

    protected Integer count;
    protected Tower source, destination;
    
    protected Point relativeCenter;
    protected com.test.nanowar.view.Troops view;

    public Troops(Player owner, Integer count, Point center) {
        super(owner, center, RADIUS);
        this.relativeCenter = center;
        this.owner = owner;
        this.count = count;
    }

    public void sendBetween(Tower source, Tower destination) {
        this.source = source;
        this.destination = destination;
        
        //step = new RealPoint(destination.center.x - realCenter.x, destination.center.y - realCenter.y);
        //step.normalise();
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
        view.update();
    }
    
    public boolean destinationReached() {
        if(destination != null) {
            return true;
            //return this.location.intersect(destination.getLocation());
        }
        else {
            // nie powinno sie wydarzyc, ale jakby co...
            return false;
        }
    }
    
    public Tower getDestination() {
        return this.destination;
    }

    public Point getRelativeCenter() {
        return relativeCenter;
    }

    public void setRelativeCenter(Point relativeCenter) {
        this.relativeCenter = relativeCenter;
    }

    public com.test.nanowar.view.Troops getView() {
        return view;
    }

    public void setView(com.test.nanowar.view.Troops view) {
        this.view = view;
    }
    
    
}
