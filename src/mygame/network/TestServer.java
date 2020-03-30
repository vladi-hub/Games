/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.network;

import mirrormonkey.core.appstate.ServerSyncAppState;
import mirrormonkey.core.context.ClientSyncContext;
import mirrormonkey.core.context.ServerSyncContext;
import mirrormonkey.lifecycle.module.ServerLifecycleModule;
import com.jme3.app.SimpleApplication;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.system.JmeContext;
import mirrormonkey.core.context.SyncContext;
import mirrormonkey.rpc.module.RpcModule;
import mirrormonkey.update.module.UpdateModule;
import mygame.network.synch.RpcCallable;
import mygame.network.synch.RpcEntity;

public class TestServer extends SimpleApplication {
    
    public static void main(String... args) {
        TestServer app = new TestServer();
        app.start(JmeContext.Type.Headless);
    }
 
    private float nextCall = 1f;
 
    private SyncContext context;
 
    private RpcCallable broadcast;
 
    @Override
    public void simpleInitApp() {
        try {
            Server server = Network.createServer(10000);
 
            ServerSyncAppState appState = new ServerSyncAppState(this, server);
            getStateManager().attach(appState);
 
            appState.getModule(ServerLifecycleModule.class);
            appState.getModule(UpdateModule.class);
            RpcModule rpcModule = appState.getModule(RpcModule.class);
            
            SyncContext positionControl = new MoveServerContext(appState, MoveServerContext.class);
            
            context = new ServerSyncContext(appState, ClientSyncContext.class);
            RpcEntity entity = new RpcEntity();
            context.add(entity);
            positionControl.add(entity);
            broadcast = rpcModule.getBroadcastProxy(RpcEntity.class, context,
                    RpcCallable.class);
 
            server.addConnectionListener(new ConnectionListener() {
 
                @Override
                public void connectionAdded(Server server,
                        final HostedConnection conn) {
                    context.add(conn);
                }
 
                @Override
                public void connectionRemoved(Server server,
                        HostedConnection conn) {
                }
            });
 
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
 
    @Override
    public void simpleUpdate(float tpf) {
        nextCall -= tpf;
        if (nextCall < 0) {
            nextCall = 1f;
            broadcast.rpcCall();
        }
    }
 
}