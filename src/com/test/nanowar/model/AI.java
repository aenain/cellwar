/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author KaMyLuS
 */
public class AI {
    
    protected Player comp, user, none;
    int count = 1, mod = 1; // ruch zostanie wykonany jesli count%mod == 0
    Random random;
    
    AI(Player comp, Player user, Player none, int mod) {
        this.comp = comp;
        this.user = user;
        this.none = none;
        this.mod = (mod <= 0 ? 1 : mod);
        
        random = new Random();
    }

    public Player getComp() {
        return comp;
    }

    public void setComp(Player comp) {
        this.comp = comp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMod() {
        return mod;
    }

    public void setMod(int mod) {
        this.mod = mod;
    }
    
    public void doMove() {
        if(count%mod == 0) {
            List<Tower> myTowers = comp.getTowers();
            
            List<Tower> otherTowers = new ArrayList();
            otherTowers.addAll(user.getTowers());
            otherTowers.addAll(none.getTowers());    
            DistCountTowerCompare compare = new DistCountTowerCompare();
            
            //Log.d("userTowers", user.getTowers().size() + " <- przy ruchu kompa");
            
            double myTowersSum = 0;
            for(Tower myTower : myTowers) {
                myTowersSum += myTower.getTroopsCount();
            }
            
            // jesli gosciowi zostala 1 wieza -> KONCZYMY Z NIM !!!
            if(user.getTowers().size() == 1 && myTowersSum >= 5) {
                comp.sendTroops(myTowers, user.getTowers().iterator().next(), 99);
                return;
            }
            
            boolean loopBreak = false;
            for(Tower myTower : myTowers) {

                Tower toAttack = null;
                ArrayList tower = new ArrayList();
                tower.add(myTower);
                
                for(Tower otherTower : otherTowers) {
                    
                    double otherCapacity = otherTower.getCapacity() + 50/myTowers.size();
                    if((myTowersSum > otherCapacity) || 
                            (myTowersSum > otherTower.getTroopsCount() + 70/myTowers.size())) {
                        
                        double percent = Math.min(100*otherCapacity/myTowersSum, 99);
                        comp.sendTroops(myTowers, otherTower, Math.max(50, (int)percent));
                        loopBreak = true;
                        
                        myTowersSum = (1-percent/100)*myTowersSum;
                        if(myTowersSum > 5) continue; 
                        break;
                    }
                    
                    otherTower.calculateDistance(myTower);
                    
                    if(toAttack == null) {
                        toAttack = otherTower;
                    }
                    else if (compare.compare(toAttack, otherTower) == -1) {
                        toAttack = otherTower;
                    }
                }
                
                if(toAttack != null) {
                    double percent = Math.min((toAttack.getTroopsCount()+15)/myTower.getTroopsCount(), 0.9999);
                    int perc = Math.max(50, (int)(percent*100)); 
                    int rand = random.nextInt(10);
                    
                    double troopsToSend = percent*myTower.getTroopsCount();
                    if(troopsToSend >= 50 || troopsToSend > toAttack.getCapacity()) {
                        comp.sendTroops(tower, toAttack, perc);
                    }
                    else if(troopsToSend >= 15 || rand%2 == 0) {
                        comp.sendTroops(tower, toAttack, perc);
                    }
                    else if(troopsToSend >= 10 || rand%3 == 0) {
                        comp.sendTroops(tower, toAttack, perc);
                    }
                    else if(rand%4 == 0) {
                        comp.sendTroops(tower, toAttack, perc);
                    }
                    
                    if(loopBreak) break;
                }
            }
        }
        count++;
    }
}
