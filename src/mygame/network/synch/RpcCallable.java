/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.network.synch;

import mirrormonkey.rpc.interfaces.RpcSpec;
 
public interface RpcCallable extends RpcSpec {
 
    public void rpcCall();
 
}