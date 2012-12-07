/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import android.graphics.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.List;

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
public class MainGamePanel {
    protected HashMap< Player, List<Tower> > playerTowers;
    protected HashMap< Player, List<Troops> > playerTroops;

    public MainGamePanel(Player user, Player comp) {
        playerTowers = new HashMap();
        playerTowers.put(user, new LinkedList());
        playerTowers.put(comp, new LinkedList());

        playerTroops = new HashMap();
        playerTroops.put(user, new LinkedList());
        playerTroops.put(comp, new LinkedList());
    }
    
    public MainGamePanel(Player user, Player comp, Level lvl) {
        user.clearObjects();
        comp.clearObjects();
        
        playerTowers = new HashMap();
        playerTowers.put(user, user.getTowers());
        playerTowers.put(comp, comp.getTowers());
        
        playerTroops = new HashMap();
        playerTroops.put(user, user.getTroops());
        playerTroops.put(comp, comp.getTroops());
    }

    public HashMap< Player, List<Tower> > getPlayerTowers() {
        return playerTowers;
    }

    public List<Tower> getPlayerTowers(Player owner) {
        return playerTowers.get(owner);
    }

    public HashMap< Player, List<Troops> > getPlayerTroops() {
        return playerTroops;
    }

    public List<Troops> getPlayerTroops(Player owner) {
        return playerTroops.get(owner);
    }

    // wysyla wojska z wiez zrodlowych do wiezy docelowej
    public void sendTroops(Integer percentageShare, List<Tower> sources, Tower destination) {
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
        List<Tower> oldOwnerTowers = playerTowers.get(oldOwner);
        oldOwnerTowers.remove(tower);

        List<Tower> newOwnerTowers = playerTowers.get(newOwner);
        newOwnerTowers.add(tower);

        if (oldOwnerTowers.isEmpty()) {
            // TODO! koniec gry
        }
    }
}
