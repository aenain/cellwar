/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import com.test.nanowar.R;
import android.content.Context;
import com.test.nanowar.GameActivity;
import com.test.nanowar.MainLayout;
import java.util.*;
import java.util.Map.Entry;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

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
    protected HashMap< Player, List<Tower>> selectedPlayerTowers;
    protected HashMap< Player, List<Troops>> playerTroops;
    protected MainLayout layout;
    protected Level level;
    protected MainThread thread;
    protected Player userPlayer, computerPlayer, nonePlayer, winner;
    
    protected Vibrator vibrator;
    protected SoundPool soundPool;

    public MainGamePanel(MainLayout layout, int levelNumber) {
        this.layout = layout;

        userPlayer = new Player(Player.PlayerType.USER);
        computerPlayer = new Player(Player.PlayerType.COMPUTER);
        nonePlayer = new Player(Player.PlayerType.NONE);

        playerTowers = new HashMap();
        playerTowers.put(userPlayer, new LinkedList());
        playerTowers.put(nonePlayer, new LinkedList());
        playerTowers.put(computerPlayer, new LinkedList());

        selectedPlayerTowers = new HashMap();
        selectedPlayerTowers.put(userPlayer, new LinkedList());
        selectedPlayerTowers.put(nonePlayer, new LinkedList());
        selectedPlayerTowers.put(computerPlayer, new LinkedList());

        playerTroops = new HashMap();
        playerTroops.put(userPlayer, new LinkedList());
        playerTroops.put(nonePlayer, new LinkedList()); // NOTE: to chyba zbedne, right?
        playerTroops.put(computerPlayer, new LinkedList());

        this.winner = null;
        this.level = new Level(levelNumber);
        
        vibrator = (Vibrator)layout.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
    }

    public MainGamePanel(Player user, Player comp, Level lvl) {
        /*user.clearObjects();
         comp.clearObjects();*/

        playerTowers = new HashMap();
        playerTowers.put(user, user.getTowers());
        playerTowers.put(comp, comp.getTowers());

        selectedPlayerTowers = new HashMap();
        selectedPlayerTowers.put(user, new LinkedList());
        selectedPlayerTowers.put(comp, new LinkedList());

        playerTroops = new HashMap();
        playerTroops.put(user, user.getTroops());
        playerTroops.put(comp, comp.getTroops());
    }

    public void initPlayers() {
        userPlayer.setGamePanel(this);
        computerPlayer.setGamePanel(this);
        nonePlayer.setGamePanel(this);
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
                } else {
                    i++;
                }
            }
        }
    }

    public HashMap< Player, List<Tower>> getPlayerTowers() {
        return playerTowers;
    }

    public List<Tower> getPlayerTowers(Player owner) {
        return playerTowers.get(owner);
    }

    public HashMap< Player, List<Tower>> getSelectedPlayerTowers() {
        return playerTowers;
    }

    public List<Tower> getSelectedPlayerTowers(Player owner) {
        return selectedPlayerTowers.get(owner);
    }

    public HashMap< Player, List<Troops>> getPlayerTroops() {
        return playerTroops;
    }

    public List<Troops> getPlayerTroops(Player owner) {
        return playerTroops.get(owner);
    }

    public void selectTower(Tower tower, Tower.Selection selectionType) {
        if (selectionType == Tower.Selection.SELECTED) {
            getSelectedPlayerTowers(tower.owner).add(tower);
        }
        else {
            getSelectedPlayerTowers(tower.owner).remove(tower);
        }
        tower.select(selectionType);
    }

    public void sendTroops(Integer percentageShare, Player player, Tower destination) {
        sendTroops(percentageShare, getSelectedPlayerTowers(player), destination);
        getSelectedPlayerTowers(player).clear();
        player.selectionMode = Player.SelectionMode.SELECT;
    }

    // wysyla wojska z wiez zrodlowych do wiezy docelowej
    public void sendTroops(Integer percentageShare, List<Tower> sources, Tower destination) {
        for (Tower source : sources) {
            Troops bubble = source.sendTroops(percentageShare, destination);
            
            if (bubble != null) {
                com.test.nanowar.view.Troops troopsView = com.test.nanowar.view.Troops.createForPlayer(bubble, layout);
                bubble.setView(troopsView);
                playerTroops.get(source.getOwner()).add(bubble);
            }
            source.select(Tower.Selection.NONE);
        }
    }

    public void initLevel() {
        level.setPanel(this);
        level.readDataAndInitTowers(layout.getContext());

        buildViews();
    }

    protected void buildViews() {
        Iterator< Entry< Player, List<Tower>>> iter;

        for (iter = playerTowers.entrySet().iterator(); iter.hasNext();) {
            Entry< Player, List<Tower>> entry = iter.next();
            for (Tower towerModel : entry.getValue()) {
                com.test.nanowar.view.Tower towerView = com.test.nanowar.view.Tower.createForModel(towerModel, layout);
                towerModel.addView(towerView);
            }
        }
    }

    public void startGame() {
        AI comp = new AI(computerPlayer, userPlayer, nonePlayer, 70);
        thread = new MainThread(this, comp);
        thread.setRunning(true);
        thread.start();
    }

    public void stopGame() {
        thread.setRunning(false);
        if (winner != null) {
            final GameActivity activity = (GameActivity)layout.getContext();

            if (userPlayer.equals(winner)) {
                level.setTimeAndScore(thread.getTime());
                level.saveResults(activity);
            } else {
                level.setScore(0);
            }

            layout.post(new Runnable() {
                public void run() {
                    activity.onGameFinished();
                }
            });
            
        }
    }

    public void changeOwner(Tower tower, Player oldOwner, Player newOwner) {
        vibrate(300);
        if(newOwner.isComputer()) {
            playSound(R.raw.alarm);
        }
        else if(newOwner.isUser()) {
            playSound(R.raw.fanfare);
        }
        
        List<Tower> oldOwnerTowers = playerTowers.get(oldOwner);
        oldOwnerTowers.remove(tower);

        List<Tower> oldOwnerSelectedTowers = selectedPlayerTowers.get(oldOwner);
        for (Tower selectedTower : oldOwnerSelectedTowers) {
            selectedTower.select(Tower.Selection.NONE);
        }
        oldOwnerSelectedTowers.remove(tower);

        List<Tower> newOwnerTowers = playerTowers.get(newOwner);
        newOwnerTowers.add(tower);

        if (oldOwnerTowers.isEmpty()) {
            if (oldOwner.isComputer() || oldOwner.isUser()) {
                winner = newOwner;
                stopGame();
            }
        }
    }
    
    public void vibrate(int time) {
        vibrator.vibrate(time);
    }
   
    public void playSound(int sound) {
        AudioManager mgr = (AudioManager)layout.getContext().getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax; 
 
        soundPool.play(sound, volume, volume, 1, 0, 1f);
    }

    public Integer getScore() {
        if (winner == null) { return null; }
        return level.getScore();
    }

    public Player getWinner() {
        return winner;
    }
}
