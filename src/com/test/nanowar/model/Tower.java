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
 * klasa reprezentujaca wszystko, co zwiazane z duza komorka (nazwana wieza)
 * 
 */
public class Tower extends GameObject {
    // określa wielkosc wiezy (pojemnosc), w ktorej szybkosc powstawania jednostek jest
    // proporcjonalna do wielkosci.
    protected Integer capacity,
    // okresla aktualna ilosc wojsk w wiezy, nie moze przekroczyc _capacity_
    // TODO! co w sytuacji, gdy wysylamy tak duzo wojska, ze sie nie miesci? zawracamy?
                      troopsCount;

    protected double internalTroopsCount;

    protected Level level;
    protected Rect internalLocation;

    public Tower(Player owner, Integer capacity, int initTroopsCount, Point center, Level level) {
        super(owner, center);
        this.capacity = capacity;
        this.troopsCount = initTroopsCount;
        this.internalTroopsCount = initTroopsCount;
        this.level = level;
        updateLocation(getRadius());

        internalLocation = new Rect(center.y + getInternalRadius(), center.x - getInternalRadius(), center.x + getInternalRadius(), center.y + getInternalRadius());
    }

    // zwraca promien dla glownej czesci wiezy, ktora "rosnie"
    public final Integer getInternalRadius() {
        // int radius = Math.min(capacity, troopsCount);
        // int radius = capacity;
        return 40;
    }

    // zwraca promien dla tej fajnej otoczki wiezy :D
    public final Integer getRadius() {
        // int radius = capacity;
        return 50;
    }

    // w kazdej iteracji liczba jednostek rosnie
    public void update() {
        double deltaCountPerFrame = capacity / 200;
        internalTroopsCount += deltaCountPerFrame;
        troopsCount = (int)Math.floor(internalTroopsCount);
    }

    public void draw(ViewGroup group) {
        
    }
    /*
     * wysyla wojsko do wiezy.
     * @param percentageShare - ile % jednostek obecnych w wiezy powinno zostac wyslanych (50-99)
     */
    public void sendTroops(Integer percentageShare, Tower destination) {
        int count = (int)Math.floor(troopsCount * percentageShare / 100);
        if (count > 0) {
            Troops bubble = new Troops(owner, count, this.center);
            bubble.sendBetween(this, destination);
        }
    }

    /*
     * metoda wywolywana, gdy jednostki (bubble) dotarly do wiezy
     */
    public void troopsArrived(Troops bubble) {
        if (owner == bubble.getOwner()) {
            troopsCount += bubble.count();
            // TODO! a co jeśli przekroczy capacity?
        }
        else {
            troopsCount -= bubble.count();
            // jesli bylo wiecej jednostek wroga, to nastepuje przejecie wiezy
            if (troopsCount < 0) {
                troopsCount = -troopsCount;
                changeOwner(bubble.getOwner());
            }
        }
    }

    protected void changeOwner(Player newOwner) {
        Player oldOwner = this.owner;
        this.owner = newOwner;
        // TODO! update color

        level.changeOwner(this, oldOwner, newOwner);
    }

    public Player getOwner() {
        return owner;
    }
}
