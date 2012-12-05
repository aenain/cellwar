/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import android.graphics.Point;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author artur
 * klasa reprezentujaca wszystko, co zwiazane z pojedynczym levelem gry
 * odpowiada za:
 * - utworzenie wież (tower) w odpowiednich miejscach
 * - sprawdzanie warunku zakonczenia (wszystkie wieze naleza do ktoregos gracza)
 * - informacje o rezultacie (zbudowanie okienka i wyswietlenie rezultatu)
 * - zarzadza gra
 */
public class Level {
    protected HashMap< Player, LinkedList<Tower> > playerTowers;
    protected HashMap< Player, LinkedList<Troops> > playerTroops;
    protected boolean running;

    public Level(Player a, Player b) {
        playerTowers = new HashMap();
        playerTowers.put(a, new LinkedList());
        playerTowers.put(b, new LinkedList());

        playerTroops = new HashMap();
        playerTroops.put(a, new LinkedList());
        playerTroops.put(b, new LinkedList());
    }

    public HashMap< Player, LinkedList<Tower> > getPlayerTowers() {
        return playerTowers;
    }

    public LinkedList<Tower> getPlayerTowers(Player owner) {
        return playerTowers.get(owner);
    }

    public HashMap< Player, LinkedList<Troops> > getPlayerTroops() {
        return playerTroops;
    }

    public LinkedList<Troops> getPlayerTroops(Player owner) {
        return playerTroops.get(owner);
    }

    // wysyla wojska z wiez zrodlowych do wiezy docelowej
    public void sendTroops(Integer percentageShare, LinkedList<Tower> sources, Tower destination) {
        for (Tower source : sources) {
            Troops bubble = source.sendTroops(percentageShare, destination);
            if (bubble != null) {
                playerTroops.get(source.getOwner()).add(bubble);
            }
        }
    }

    public Tower createTower(Player owner, Integer capacity, Integer initTroopsCount, Point center) {
        Tower tower = new Tower(owner, capacity, initTroopsCount, center, this);
        return tower;
    }

    // TODO! tworzenie wiez w okreslonych miejscach dla poszczegolnych graczy
    // TODO! metoda update, ktora update'uje wszystkie obiekty, a potem je rysuje

    public void changeOwner(Tower tower, Player oldOwner, Player newOwner) {
        LinkedList<Tower> oldOwnerTowers = playerTowers.get(oldOwner);
        oldOwnerTowers.remove(tower);

        LinkedList<Tower> newOwnerTowers = playerTowers.get(newOwner);
        newOwnerTowers.add(tower);

        if (oldOwnerTowers.isEmpty()) {
            // TODO! koniec gry
        }
    }
}
