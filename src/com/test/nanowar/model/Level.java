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
    protected LinkedList<Troops> troops;
    protected boolean running;

    public Level(Player a, Player b) {
        playerTowers = new HashMap();
        playerTowers.put(a, new LinkedList());
        playerTowers.put(b, new LinkedList());
        troops = new LinkedList();
    }

    // wysyla wojska z wiez zrodlowych do wiezy docelowej
    public void sendTroops(Integer percentageShare, LinkedList<Tower> sources, Tower destination) {
        for (Tower source : sources) {
            source.sendTroops(percentageShare, destination);
        }
    }

    public Tower createTower(Player owner, Integer capacity, Integer initTroopsCount, Point center) {
        Tower tower = new Tower(owner, capacity, initTroopsCount, center, this);
        return tower;
    }

    // TODO! tworzenie wiez w okreslonych miejscach dla poszczegolnych graczy

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
