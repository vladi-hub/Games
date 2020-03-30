/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.network;

import mirrormonkey.core.appstate.SyncAppState;
import mirrormonkey.core.context.ClientSyncContext;

/**
 *
 * @author ches
 */
public class MoveClientContext extends ClientSyncContext {
    
    public MoveClientContext(int id, SyncAppState appState,
            Class<?> connectedContextClass) {
        super(id, appState, connectedContextClass);
    }
}