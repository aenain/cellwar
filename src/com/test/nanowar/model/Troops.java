/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import android.graphics.Point;
import android.graphics.Rect;

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

    public Troops(Player owner, Integer count, Point center) {
        super(owner, center, RADIUS);
        this.owner = owner;
        this.count = count;
    }

    public void sendBetween(Tower source, Tower destination) {
        this.source = source;
        this.destination = destination;

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
}
