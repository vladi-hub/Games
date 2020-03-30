/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.network.synch;

import com.jme3.math.Vector3f;
import mirrormonkey.rpc.interfaces.RpcSpec;

/**
 *
 * @author ches
 */
public interface RpcMoveable extends RpcSpec {
    
    public void requestMovement(Vector3f moveAmt);
}
