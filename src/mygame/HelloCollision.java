/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import mygame.player.BoxGuy;
/**
 * Example 9 - How to make walls and floors solid.
 * This version uses Physics and a custom Action Listener.
 * @author normen, with edits by Zathras
 */
public class HelloCollision extends SimpleApplication
  implements ActionListener {
    
  private Spatial sceneModel;
  private BulletAppState bulletAppState;
  private RigidBodyControl landscape;
  private CharacterControl player;
  private Vector3f walkDirection = new Vector3f();
  private Sphere bullet;
  private SphereCollisionShape bulletCollisionShape;
  Material mat2;
  
  private ArrayList<BoxGuy> list = new ArrayList<BoxGuy>(3);
  
  private boolean left = false, right = false, up = false, down = false;  
  
  public static void main(String[] args) {
    HelloCollision app = new HelloCollision();
    app.start();
  }
  public void simpleInitApp() {
    /** Set up Physics */
    bulletAppState = new BulletAppState();
    bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
    stateManager.attach(bulletAppState);    
    
    bullet = new Sphere(32, 32, 0.4f, true, false);
    bullet.setTextureMode(TextureMode.Projected);
    bulletCollisionShape = new SphereCollisionShape(0.4f);   
    
    initMaterial();
    initCrossHairs();
        
    for (int i = 0; i < 5; i ++)
    {
        list.add(new BoxGuy(this, list.size()));
    }
    
    // We re-use the flyby camera control for rotation, while positioning is handled by physics
    viewPort.setBackgroundColor(new ColorRGBA(0.7f,0.8f,1f,1f));
    flyCam.setMoveSpeed(100);
    setUpKeys();
    setUpLight();
    // We load the scene from the zip file and adjust its size.
    assetManager.registerLocator("town.zip", ZipLocator.class.getName());
    sceneModel = assetManager.loadModel("main.scene");
    sceneModel.setLocalScale(2f);
    // We set up collision detection for the scene by creating a
    // compound collision shape and a static physics node with mass zero.
    CollisionShape sceneShape =
      CollisionShapeFactory.createMeshShape((Node) sceneModel);
    landscape = new RigidBodyControl(sceneShape, 0);
    sceneModel.addControl(landscape);
    // We set up collision detection for the player by creating
    // a capsule collision shape and a physics character node.
    // The physics character node offers extra settings for
    // size, stepheight, jumping, falling, and gravity.
    // We also put the player in its starting position.
    CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
    player = new CharacterControl(capsuleShape, 0.05f);
    player.setJumpSpeed(20);
    player.setFallSpeed(30);
    player.setGravity(30);    
    player.setPhysicsLocation(new Vector3f(0, 10, 0));
    // We attach the scene and the player to the rootnode and the physics space,
    // to make them appear in the game world.
    rootNode.attachChild(sceneModel);
    bulletAppState.getPhysicsSpace().add(landscape);
    bulletAppState.getPhysicsSpace().add(player);
  }
  
  private void setUpLight() {
        // We add light so we see the scene
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);
        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
    }
  /** We over-write some navigational key mappings here, so we can
   * add physics-controlled walking and jumping: */
  private void setUpKeys() {
    inputManager.addMapping("Lefts",  new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Ups",    new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Downs",  new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Jumps",  new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addListener(this, "Lefts");
    inputManager.addListener(this, "Rights");
    inputManager.addListener(this, "Ups");
    inputManager.addListener(this, "Downs");
    inputManager.addListener(this, "Jumps");
    
    inputManager.addMapping("Shoot",
      new KeyTrigger(KeyInput.KEY_LSHIFT), // trigger 1: spacebar
      new MouseButtonTrigger(0));         // trigger 2: left-button click
    inputManager.addListener(this, "Shoot");
  }
  /** These are our custom actions triggered by key presses.
   * We do not walk yet, we just keep track of the direction the user pressed. */
  public void onAction(String binding, boolean value, float tpf) {
    if (binding.equals("Lefts")) {
      left = value;
    } else if (binding.equals("Rights")) {
      right = value;
    } else if (binding.equals("Ups")) {
      up = value;
    } else if (binding.equals("Downs")) {
      down = value;
    } else if (binding.equals("Jumps")) {
      player.jump();
    }
    
    if (binding.equals("Shoot") && !value) {
            Geometry bulletg = new Geometry("bullet", bullet);
            bulletg.setMaterial(mat2);
            bulletg.setLocalTranslation(cam.getLocation());
            bulletg.setLocalScale(0.4f);
            //bulletCollisionShape = new SphereCollisionShape(1f);
            //RigidBodyControl bulletNode = new BombControl(assetManager, bulletCollisionShape, 2);
    //                RigidBodyControl bulletNode = new RigidBodyControl(bulletCollisionShape, 1);
           RigidBodyControl bulletNode = new RigidBodyControl(bulletCollisionShape, 0.4f * 10);
            bulletNode.setCcdMotionThreshold(0.5f);
            bulletNode.setLinearVelocity(cam.getDirection().mult(800));
            bulletg.addControl(bulletNode);
            rootNode.attachChild(bulletg);
            getPhysicsSpace().add(bulletNode);
  
        
            CollisionResults results = new CollisionResults();
            // 2. Aim the ray from cam loc to cam direction.
            Ray ray = new Ray(cam.getLocation(), cam.getDirection());
            // 3. Collect intersections between Ray and Shootables in results list.
            getRootNode().collideWith(ray, results);
            // 4. Print the results
            System.out.println("----- Collisions? " + results.size() + "-----");

             for (int i = 0; i < results.size(); i++){
                    try{                                        
                        //list.get(results.getCollision(i).getGeometry().getControl(MyControl.class).hurt());
                        list.get(Integer.parseInt(results.getCollision(i).getGeometry().getName())).hurt();
                    }catch(Exception e){      
                        System.out.println("You can't shoot this. " + e.getMessage());
                    }
              }
      }
  }
  /**
   * This is the main event loop--walking happens here.
   * We check in which direction the player is walking by interpreting
   * the camera direction forward (camDir) and to the side (camLeft).
   * The setWalkDirection() command is what lets a physics-controlled player walk.
   * We also make sure here that the camera moves with player.
   */
  @Override
  public void simpleUpdate(float tpf) {
    Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
    Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
    walkDirection.set(0, 0, 0);
    if (left)  { walkDirection.addLocal(camLeft); }
    if (right) { walkDirection.addLocal(camLeft.negate()); }
    if (up)    { walkDirection.addLocal(camDir); }
    if (down)  { walkDirection.addLocal(camDir.negate()); }
    player.setWalkDirection(walkDirection);
    cam.setLocation(player.getPhysicsLocation()); 
    
  }
  
  protected void initCrossHairs() {
    guiNode.detachAllChildren();
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText ch = new BitmapText(guiFont, false);
    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    ch.setText("+"); // crosshairs
    ch.setLocalTranslation( // center
      settings.getWidth()/2 - guiFont.getCharSet().getRenderedSize()/3*2,
      settings.getHeight()/2 + ch.getLineHeight()/2, 0);
    guiNode.attachChild(ch);
  }    
    
  public void initMaterial() {
        mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        mat2.setTexture("ColorMap", tex2);
    }

  
  public BulletAppState getBulletAppState(){
        return bulletAppState;
  }
  
  public PhysicsSpace getPhysicsSpace(){
     return bulletAppState.getPhysicsSpace();
  }
  
}
