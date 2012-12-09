/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import android.util.Log;
import java.util.List;

/**
 *
 * @author KaMyLuS
 */
public class AI {
    
    protected Player comp, user, none;
    int count = 0, mod = 1; // ruch zostanie wykonany jesli count%mod == 0
    
    AI(Player comp, Player user, Player none, int mod) {
        this.comp = comp;
        this.user = user;
        this.none = none;
        this.mod = (mod <= 0 ? 1 : mod);
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
            List<Tower> userTowers = user.getTowers();
            List<Tower> noneTowers = none.getTowers();
            
            Log.d("doMove", myTowers.size() + " " + userTowers.size());
            if(noneTowers.size() > 0 && myTowers.size() > 0) {
                comp.sendTroops(myTowers, noneTowers.iterator().next(), 90);
            }
        }
        count++;
    }
}
