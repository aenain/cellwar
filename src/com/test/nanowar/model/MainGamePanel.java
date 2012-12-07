/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import android.graphics.Point;
import com.test.nanowar.MainLayout;
import java.util.HashMap;
import java.util.LinkedList;
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
    protected MainLayout layout;
    protected Level level;
    protected MainThread thread;

    public MainGamePanel(MainLayout layout, Level level) {
        this.layout = layout;
        this.level = level;

        Player user = new Player(Player.PlayerType.USER);
        Player computer = new Player(Player.PlayerType.COMPUTER);
        Player none = new Player(Player.PlayerType.NONE);

        playerTowers = new HashMap();
        playerTowers.put(user, new LinkedList());
        playerTowers.put(none, new LinkedList());
        playerTowers.put(computer, new LinkedList());

        playerTroops = new HashMap();
        playerTroops.put(user, new LinkedList());
        playerTowers.put(none, new LinkedList());
        playerTroops.put(computer, new LinkedList());
    }

    public void update() {
        // TODO! update state of game
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

    public void initLevel() {
        // TODO! pobranie danych z levelu, stworzenie wież
    }

    public void startGame() {
        thread = new MainThread(this);
        thread.setRunning(true);
        thread.start();
    }

    public void stopGame() {
        thread.setRunning(false);
        // TODO! zapis stanu gry
    }

    public Tower createTower(Player owner, Integer capacity, Integer initTroopsCount, Point center) {
        Tower tower = new Tower(owner, capacity, initTroopsCount, center, this);
        return tower;
    }

    public void changeOwner(Tower tower, Player oldOwner, Player newOwner) {
        List<Tower> oldOwnerTowers = playerTowers.get(oldOwner);
        oldOwnerTowers.remove(tower);

        List<Tower> newOwnerTowers = playerTowers.get(newOwner);
        newOwnerTowers.add(tower);

        if ((oldOwner.isComputer() || oldOwner.isUser()) && oldOwnerTowers.isEmpty()) {
            // TODO! koniec gry
        }
    }
}
