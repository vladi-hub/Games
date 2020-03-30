/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.network;

import mirrormonkey.core.appstate.ClientSyncAppState;
import mirrormonkey.core.context.SyncContext;
import mirrormonkey.lifecycle.module.ClientLifecycleModule;
import mirrormonkey.update.module.UpdateModule;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.system.JmeContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import mirrormonkey.core.context.ClientSyncContext;
import mirrormonkey.core.context.ServerSyncContext;
import mirrormonkey.core.data.entitylevel.SyncEntityData;
import mirrormonkey.core.interfaces.ContextLifecycleListener;
import mirrormonkey.rpc.module.RpcModule;
import mygame.network.synch.RpcMoveable;

public class TestClient extends SimpleApplication {
    public static void main(String... args) {
        TestClient app = new TestClient();
        app.start(JmeContext.Type.Headless);
    }
    private ClientSyncAppState appState;
    private final Collection<MoveClientContext> moveables = new ArrayList<MoveClientContext>();
    private final Random random = new Random();
    private RpcModule rpcModule;
    private Client client;
    private SyncContext positionControl; 
    private float nextUpdate = 1f;
    @Override
    public void simpleInitApp() {
        try {
            client = Network.connectToServer("localhost", 10000);
            appState = new ClientSyncAppState(this, client);
            getStateManager().attach(appState);
            appState.getModule(ClientLifecycleModule.class);
            appState.getModule(UpdateModule.class);
            rpcModule = appState.getModule(RpcModule.class);
            
            appState.addContextLifecycleListener(new ContextLifecycleListener() {
                @Override
                public void contextCreated(SyncContext context) {
                    if (context.getClass().equals(MoveClientContext.class)) {
                        moveables.add((MoveClientContext) context);
                    }
                }
                @Override
                public void contextDestroyed(SyncContext context) {
                    if(context.getClass().equals(MoveClientContext.class)) {
                        moveables.remove((MoveClientContext) context);
                    }
                }
            });
            
            positionControl = new MoveServerContext(appState, MoveClientContext.class);            
            client.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private final float balFloat(float tpf) {
        return (random.nextFloat() - 0.5f) * tpf;
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        for (MoveClientContext ctx : moveables) {
            for (SyncEntityData data : ctx.getEntityData()) {
                if (!RpcMoveable.class.isAssignableFrom(data.reference.getClass())) {
                    continue;
                }
                Vector3f moveAmt = new Vector3f(balFloat(tpf), balFloat(tpf),
                        balFloat(tpf));
                RpcMoveable m = rpcModule.getSingleEntityProxy(data.reference,
                        ctx, null, client, RpcMoveable.class);
                m.requestMovement(moveAmt);
            }
        }
    }
}
