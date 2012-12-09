/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

/**
 *
 * @author artur klasa reprezentujaca wszystko, co zwiazane z duza komorka
 * (nazwana wieza)
 *
 */
public class Tower {

    public enum Selection {
        SELECTED, NONE
    }

    public static final int MAX_CAPACITY = 100;
    public static final int MIN_TROOPS_SHARE = 50;
    public static final int MAX_TROOPS_SHARE = 100;

    // określa wielkosc wiezy (pojemnosc), w ktorej szybkosc powstawania jednostek jest
    // proporcjonalna do wielkosci.
    protected Integer capacity;
    // okresla aktualna ilosc wojsk w wiezy, nie moze przekroczyc _capacity_
    protected Double troopsCount;
    protected MainGamePanel panel;
    protected Rect location;
    protected com.test.nanowar.view.Tower view;
    protected Selection selection;
    // in percentage of width and height of the screen
    protected Point relativeCenter;
    protected Player owner;

    public Tower(MainGamePanel panel) {
        this.panel = panel;
        // this.selection = Selection.NONE;
    }

    public void addView(com.test.nanowar.view.Tower view) {
        this.view = view;
    }

    public MainGamePanel getGamePanel() {
        return panel;
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

    public com.test.nanowar.view.Tower getView() {
        return view;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setInitTroopsCount(int count) {
        this.troopsCount = (double)count;
    }

    public Double getTroopsCount() {
        return troopsCount;
    }

    // w kazdej iteracji liczba jednostek rosnie
    public void update() {
        synchronized(troopsCount) {
            if(troopsCount < capacity) {
                double deltaCountPerFrame = (double)capacity / 2000;
                troopsCount += deltaCountPerFrame;
            }
        }

        view.post(new Runnable() {
            public void run() {
                view.update();
            }
        });
    }

    /*
     * wysyla wojsko do wiezy.
     * @param percentageShare - ile % jednostek obecnych w wiezy powinno zostac wyslanych (50-99)
     */
    public Troops sendTroops(Integer percentageShare, Tower destination) {
        Troops bubble = null;
        int count = (int) Math.floor(troopsCount * percentageShare / 100);

        if (count > 0) {
            synchronized(troopsCount) {
                troopsCount -= count;
            }
            bubble = new Troops(owner, count, this.relativeCenter);
            bubble.sendBetween(this, destination);
        }
        
        Log.d("liczymy", count + "  ");
        return bubble;
    }

    /*
     * metoda wywolywana, gdy jednostki (bubble) dotarly do wiezy
     */
    public void troopsArrived(Troops bubble) {
        synchronized(troopsCount) {
            if (owner == bubble.getOwner()) {
                troopsCount += bubble.count();
            } else {
                troopsCount -= bubble.count();
                // jesli bylo wiecej jednostek wroga, to nastepuje przejecie wiezy
                if (troopsCount < 0) {
                    troopsCount *= -1;
                    changeOwner(bubble.getOwner());
                }
            }
        }

        panel.getPlayerTroops(bubble.getOwner()).remove(bubble);
        
        bubble.getView().remove();
    }

    public Selection select(final Selection selection) {
        this.selection = selection;

        view.post(new Runnable() {
            public void run() {
                view.select(selection);
            }
        });

        return this.selection;
    }

    public boolean isSelected() {
        return (selection == Selection.SELECTED);
    }

    protected void changeOwner(Player newOwner) {
        Player oldOwner = this.owner;
        this.owner = newOwner;
        panel.changeOwner(this, oldOwner, newOwner);
        view.changeOwner(newOwner);
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
