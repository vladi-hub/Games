/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.network.synch;

import mirrormonkey.core.interfaces.SyncEntity;
import mirrormonkey.rpc.annotations.RpcTarget;
 
public class RpcEntity implements SyncEntity, RpcCallable {
 
    @Override
    @RpcTarget
    public void rpcCall() {
        System.out.println("Hello RPC ");
    }
}