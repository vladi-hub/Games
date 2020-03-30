/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.network;

import mirrormonkey.core.appstate.SyncAppState;
import mirrormonkey.core.context.ServerSyncContext;

/**
 *
 * @author ches
 */
public class MoveServerContext extends ServerSyncContext {
    
    public MoveServerContext(SyncAppState appState,
            Class<?> connectedContextClass) {
        super(appState, connectedContextClass);
    }
}