/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import android.graphics.Point;
import com.test.nanowar.MainLayout;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author artur klasa reprezentujaca wszystko, co zwiazane z pojedynczym
 * levelem gry odpowiada za: - utworzenie wież (tower) w odpowiednich miejscach
 * - sprawdzanie warunku zakonczenia (wszystkie wieze naleza do ktoregos gracza)
 * - informacje o rezultacie (zbudowanie okienka i wyswietlenie rezultatu) -
 * zarzadza gra
 */
public class MainGamePanel {

    protected HashMap< Player, List<Tower>> playerTowers;
    protected HashMap< Player, List<Troops>> playerTroops;
    protected MainLayout layout;
    protected Level level;
    protected MainThread thread;
    protected Player userPlayer, computerPlayer, nonePlayer;

    public MainGamePanel(MainLayout layout, int levelNumber) {
        this.layout = layout;

        userPlayer = new Player(Player.PlayerType.USER);
        computerPlayer = new Player(Player.PlayerType.COMPUTER);
        nonePlayer = new Player(Player.PlayerType.NONE);

        playerTowers = new HashMap();
        playerTowers.put(userPlayer, new LinkedList());
        playerTowers.put(nonePlayer, new LinkedList());
        playerTowers.put(computerPlayer, new LinkedList());

        playerTroops = new HashMap();
        playerTroops.put(userPlayer, new LinkedList());
        playerTowers.put(nonePlayer, new LinkedList());
        playerTroops.put(computerPlayer, new LinkedList());

        this.level = new Level(levelNumber);
    }

    public MainGamePanel(Player user, Player comp, Level lvl) {
        /*user.clearObjects();
         comp.clearObjects();*/

        playerTowers = new HashMap();
        playerTowers.put(user, user.getTowers());
        playerTowers.put(comp, comp.getTowers());

        playerTroops = new HashMap();
        playerTroops.put(user, user.getTroops());
        playerTroops.put(comp, comp.getTroops());
    }

    public Player getComputerPlayer() {
        return computerPlayer;
    }

    public void setComputerPlayer(Player computerPlayer) {
        this.computerPlayer = computerPlayer;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Player getNonePlayer() {
        return nonePlayer;
    }

    public void setNonePlayer(Player nonePlayer) {
        this.nonePlayer = nonePlayer;
    }

    public Player getUserPlayer() {
        return userPlayer;
    }

    public void setUserPlayer(Player userPlayer) {
        this.userPlayer = userPlayer;
    }

    public void update() {
        // TODO! update state of game
        updateTroops();
        updateTowers();
        handleCollisions();
        updateTowersLists();
    }

    public void updateTroops() {
        Set<Player> players = playerTroops.keySet();
        for (Player player : players) {
            List<Troops> troops = playerTroops.get(player);
            for (Troops troop : troops) {
                troop.update();
            }
        }
    }

    public void updateTowers() {
        Set<Player> players = playerTowers.keySet();
        for (Player player : players) {
            List<Tower> towerList = playerTowers.get(player);
            for (Tower tower : towerList) {
                tower.update();
            }
        }
    }

    public void handleCollisions() {
        Set<Player> players = playerTroops.keySet();
        for (Player player : players) {
            List<Troops> troops = playerTroops.get(player);
            for (int i = 0; i < troops.size();) {
                Troops troop = troops.get(i);
                if (troop.destinationReached()) {
                    troop.getDestination().troopsArrived(troop);
                    troops.remove(i);
                } else {
                    i++;
                }
            }
        }
    }

    public void updateTowersLists() {
        HashMap<Player, List<Tower>> tempPlayTowers = new HashMap();
        tempPlayTowers.put(userPlayer, new ArrayList());
        tempPlayTowers.put(computerPlayer, new ArrayList());
        tempPlayTowers.put(nonePlayer, new ArrayList());

        Iterator< Entry< Player, List<Tower>>> iter;
        for (iter = playerTowers.entrySet().iterator(); iter.hasNext();) {
            Entry< Player, List<Tower>> entry = iter.next();

            Iterator<Tower> listIter;
            for (listIter = entry.getValue().iterator(); listIter.hasNext();) {
                Tower tower = listIter.next();

                if (tower.getOwner().isComputer()) {
                    tempPlayTowers.get(computerPlayer).add(tower);
                } else if (tower.getOwner().isUser()) {
                    tempPlayTowers.get(userPlayer).add(tower);
                } else if (tower.getOwner().isNone()) {
                    tempPlayTowers.get(nonePlayer).add(tower);
                }
            }
        }

        playerTowers = tempPlayTowers;
    }

    public HashMap< Player, List<Tower>> getPlayerTowers() {
        return playerTowers;
    }

    public List<Tower> getPlayerTowers(Player owner) {
        return playerTowers.get(owner);
    }

    public HashMap< Player, List<Troops>> getPlayerTroops() {
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
        level.setPanel(this);
        level.readData(layout.getContext());

        // buildViews();
    }

    protected void buildViews() {
        Iterator< Entry< Player, List<Tower>>> iter;

        for (iter = playerTowers.entrySet().iterator(); iter.hasNext();) {
            Entry< Player, List<Tower>> entry = iter.next();
            for (Tower towerModel : entry.getValue()) {
                com.test.nanowar.view.Tower towerView = com.test.nanowar.view.Tower.createForModel(towerModel, layout.getContext());
                towerModel.createRectangle();
                towerModel.addView(towerView);
            }
        }
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
