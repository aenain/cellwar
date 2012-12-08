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
public class Tower {
    public static final int MAX_CAPACITY = 100;

    // określa wielkosc wiezy (pojemnosc), w ktorej szybkosc powstawania jednostek jest
    // proporcjonalna do wielkosci.
    protected Integer capacity,
    // okresla aktualna ilosc wojsk w wiezy, nie moze przekroczyc _capacity_
                      troopsCount;

    protected double internalTroopsCount;

    protected MainGamePanel panel;
    protected Rect location;
    protected com.test.nanowar.view.Tower view;

    // in percentage of width and height of the screen
    protected Point relativeCenter;
    protected Player owner;

    public Tower(MainGamePanel panel) {
        this.panel = panel;
    }

    public void addView(com.test.nanowar.view.Tower view) {
        this.view = view;
    }

    public Point getRelativeCenter() {
        return relativeCenter;
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

    // w kazdej iteracji liczba jednostek rosnie
    public void update() {
        double deltaCountPerFrame = capacity / 200;
        internalTroopsCount += deltaCountPerFrame;
        troopsCount = (int)Math.floor(internalTroopsCount);
        view.update();
    }

    /*
     * wysyla wojsko do wiezy.
     * @param percentageShare - ile % jednostek obecnych w wiezy powinno zostac wyslanych (50-99)
     */
    public Troops sendTroops(Integer percentageShare, Tower destination) {
        Troops bubble = null;
        int count = (int)Math.floor(troopsCount * percentageShare / 100);
        
        if (count > 0) {
            bubble = new Troops(owner, count, this.relativeCenter);
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
