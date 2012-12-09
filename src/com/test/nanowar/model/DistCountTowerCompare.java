/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import java.util.Comparator;

/**
 *
 * @author KaMyLuS
 */
public class DistCountTowerCompare implements Comparator<Tower> {

    public int compare(Tower t, Tower t1) {
        /*double w1 = 5000/t.getDistance() + t.getTroopsCount() - (t.getOwner().isNone() ? 1 : 0);
        double w2 = 5000/t1.getDistance() + t1.getTroopsCount() - (t1.getOwner().isNone() ? 1 : 0);*/
        
        double w1 = t.getDistance();
        double w2 = t1.getDistance();
        
        if(w1 > w2) return -1;
        if(w1 < w2) return 1;
        return 0;
    }
    
}
