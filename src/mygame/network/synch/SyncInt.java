/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.network.synch;

import mirrormonkey.core.annotations.SyncField;
import mirrormonkey.core.interfaces.SyncEntity;
import mirrormonkey.update.annotations.SyncCycle;
import mirrormonkey.update.annotations.SyncDynamic;
 
import com.jme3.network.serializing.Serializable;
 
@Serializable
public class SyncInt implements SyncEntity {
    private int intValue;
    @SyncField
    @SyncDynamic(cycles = @SyncCycle(freq = 0.5f))
    public int getIntValue() {
        return intValue;
    }
    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
}