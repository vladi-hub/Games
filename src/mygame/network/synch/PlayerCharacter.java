/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.network.synch;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializer;
import mirrormonkey.core.interfaces.SyncEntity;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import mirrormonkey.core.annotations.SyncField;
import mirrormonkey.rpc.annotations.RpcTarget;
import mirrormonkey.update.annotations.SyncCycle;
import mirrormonkey.update.annotations.SyncDynamic;
import mygame.network.MoveClientContext;
import mygame.network.MoveServerContext;

/**
 *
 * @author ches
 */
public class PlayerCharacter implements SyncEntity,
        RpcMoveable  {
    
    @SyncField
    @SyncDynamic(cycles = @SyncCycle(freq = 0.1f))
    public CharacterControl character;
    
    
    public Node model;
    private Node rootNode;
    
    private PhysicsSpace space;
    private AssetManager assetManager;
    
    private ChaseCamera chaseCam;
    private FlyByCamera flyCam;
    private InputManager inputManager;
    
    private Camera cam;
    
    public PlayerCharacter(){        
        //Serializer.registerClass(com.jme3.scene.Node.class);
    }
    
    public void initChaseCamAndPlayer(){
        initiatePlayer();
        setupChaseCamera();
    }
    
    private void initiatePlayer() {
        CapsuleCollisionShape capsule = new CapsuleCollisionShape(3f, 4f);
        setCharacter(new CharacterControl(capsule, 0.01f));
        setModel((Node) getAssetManager().loadModel("Models/Oto/Oto.mesh.xml"));
        //model.setLocalScale(0.5f);
        getModel().addControl(getCharacter());
        getCharacter().setPhysicsLocation(new Vector3f(-140, 15, -10));
        getRootNode().attachChild(getModel());
        getSpace().add(getCharacter());
    }
    
    private void setupChaseCamera() {
        getFlyCam().setEnabled(false);
        setChaseCam(new ChaseCamera(getCam(), getModel(), getInputManager()));
        getChaseCam().setDefaultDistance(75f);
    } 
    
    public boolean onGround(){
        return getCharacter().onGround();
    }
    
    public void setWalkDirection(Vector3f walkDirection){
        getCharacter().setWalkDirection(walkDirection); 
    }
    
    public void setViewDirection(Vector3f viewDirection){
        getCharacter().setViewDirection(viewDirection); 
    }
    
    public Vector3f getPhysicsLocation(){
        return getCharacter().getPhysicsLocation();
    }
    
    public void jump(){
        getCharacter().jump();
    }
    
    public <T extends Control> T getControl(Class<T> controlType) {
        return getModel().getControl(controlType);
    }
    
    @SyncField
    @SyncDynamic(cycles = @SyncCycle(freq = 0.5f))
    public Vector3f getModelLocalTranslation(){
        return getModel().getLocalTranslation();
    }
    
     public void setModelLocalTranslation(Vector3f v){
        getModel().setLocalTranslation(v);
    }
    
    @SyncField
    @SyncDynamic(cycles = @SyncCycle(freq = 0.5f))
    public Quaternion getModelLocalRotation(){
        return getModel().getLocalRotation();
    }
    
    public void setModelLocalRotation(Quaternion q){
        getModel().setLocalRotation(q);
    }
    
    public Vector3f getWorldLocalTranslation(){
        return getModel().getWorldTranslation();
    }
    
    public Quaternion getWorldLocalRotation(){
        return getModel().getWorldRotation();
    }
    
    @SyncField
    @SyncDynamic(cycles = @SyncCycle(freq = 0.1f))
    public Vector3f getLocalTranslation() {
        return getModel().getLocalTranslation();
    }
    
    public void setLocalTranslation(Vector3f v) {
        getModel().setLocalTranslation(v);
    }
    
    @Override
    @RpcTarget(sending = MoveClientContext.class, receiving = MoveServerContext.class)
    public void requestMovement(Vector3f moveAmt) {
        Vector3f camDir = getCam().getDirection().clone().multLocal(0.1f);
        Vector3f camLeft = getCam().getLeft().clone().multLocal(0.1f);
        camDir.y = 0;
        camLeft.y = 0;
              
    }

    public void onRemoteCreate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onRemoteUpdate(float f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onRemoteDelete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onLocalUpdate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void interpolate(float f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void extrapolate(float f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the character
     */
    public CharacterControl getCharacter() {
        return character;
    }

    /**
     * @param character the character to set
     */
    public void setCharacter(CharacterControl character) {
        this.character = character;
    }

    /**
     * @return the model
     */
    public Node getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(Node model) {
        this.model = model;
    }

    /**
     * @return the rootNode
     */
    public Node getRootNode() {
        return rootNode;
    }

    /**
     * @param rootNode the rootNode to set
     */
    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * @return the space
     */
    public PhysicsSpace getSpace() {
        return space;
    }

    /**
     * @param space the space to set
     */
    public void setSpace(PhysicsSpace space) {
        this.space = space;
    }

    /**
     * @return the assetManager
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * @param assetManager the assetManager to set
     */
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * @return the chaseCam
     */
    public ChaseCamera getChaseCam() {
        return chaseCam;
    }

    /**
     * @param chaseCam the chaseCam to set
     */
    public void setChaseCam(ChaseCamera chaseCam) {
        this.chaseCam = chaseCam;
    }

    /**
     * @return the flyCam
     */
    public FlyByCamera getFlyCam() {
        return flyCam;
    }

    /**
     * @param flyCam the flyCam to set
     */
    public void setFlyCam(FlyByCamera flyCam) {
        this.flyCam = flyCam;
    }

    /**
     * @return the inputManager
     */
    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * @param inputManager the inputManager to set
     */
    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    /**
     * @return the cam
     */
    public Camera getCam() {
        return cam;
    }

    /**
     * @param cam the cam to set
     */
    public void setCam(Camera cam) {
        this.cam = cam;
    }

    
}
