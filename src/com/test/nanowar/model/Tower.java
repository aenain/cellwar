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
 * klasa reprezentujaca wszystko, co zwiazane z duza komorka (nazwana wieza)
 * 
 */
public class Tower extends GameObject {
    // określa wielkosc wiezy (pojemnosc), w ktorej szybkosc powstawania jednostek jest
    // proporcjonalna do wielkosci.
    protected Integer capacity,
    // okresla aktualna ilosc wojsk w wiezy, nie moze przekroczyc _capacity_
                      troopsCount;

    protected double internalTroopsCount;

    protected MainGamePanel panel;
    protected Rect internalLocation;
    protected com.test.nanowar.view.Tower view;

    // in percentage of width and height of the screen
    protected Point relativeCenter;

    public Tower() {
        super(null, null);
    }

    public Tower(Player owner, Integer capacity, int initTroopsCount, Point center, MainGamePanel panel) {
        super(owner, center);
        this.capacity = capacity;
        this.troopsCount = initTroopsCount;
        this.internalTroopsCount = initTroopsCount;
        this.panel = panel;
        updateLocation(getRadius());

        internalLocation = new Rect(center.y + getInternalRadius(), center.x - getInternalRadius(), center.x + getInternalRadius(), center.y + getInternalRadius());
    }

    public void createRectangle() {
        // TODO!
    }

    public void addView(com.test.nanowar.view.Tower view) {
        this.view = view;
    }

    public void setRelativeCenter(Point center) {
        this.relativeCenter = center;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setInitTroopsCount(int count) {
        this.troopsCount = count;
        this.internalTroopsCount = count;
    }
    
    public Integer getTroopsCount() {
        return troopsCount;
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

    /*
     * wysyla wojsko do wiezy.
     * @param percentageShare - ile % jednostek obecnych w wiezy powinno zostac wyslanych (50-99)
     */
    public Troops sendTroops(Integer percentageShare, Tower destination) {
        Troops bubble = null;
        int count = (int)Math.floor(troopsCount * percentageShare / 100);
        
        if (count > 0) {
            bubble = new Troops(owner, count, this.center);
            bubble.sendBetween(this, destination);
        }

        return bubble;
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

        panel.getPlayerTroops(bubble.getOwner()).remove(bubble);
    }

    protected void changeOwner(Player newOwner) {
        Player oldOwner = this.owner;
        this.owner = newOwner;
        // TODO! update color

        panel.changeOwner(this, oldOwner, newOwner);
        /*oldOwner.deleteTower(this);
        newOwner.addTower(this);*/
    }

    // podczas rozgrywki należy używać metody changeOwner!
    // tej metody można użyć podczas inicjalizacji obiektu przed pojawieniem się go na ekranie
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
    
}
