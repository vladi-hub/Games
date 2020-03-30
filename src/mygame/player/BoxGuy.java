/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.util.TangentBinormalGenerator;
import mygame.HelloCollision;
public class BoxGuy
{
    HelloCollision stage;
    Geometry guy;
    RigidBodyControl guy_control;
    int health = 100;
    public BoxGuy(HelloCollision s, int i)
    {
        stage = s;
        Material monkey = new Material(s.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        monkey.setTexture("DiffuseMap", s.getAssetManager().loadTexture("Interface/Logo/Monkey.jpg"));
        Box box  = new Box(Vector3f.ZERO, 2, 2, 2);
        guy = new Geometry(Integer.toString(i), box);
        guy.setMaterial(monkey);
        TangentBinormalGenerator.generate(guy);
        guy.setLocalTranslation((float)(Math.random() * 20) - 10, 7, (float)(Math.random() * 20) - 10);
        stage.getRootNode().attachChild(guy);
        guy_control = new RigidBodyControl(10);
        guy.addControl(guy_control);
        stage.getBulletAppState().getPhysicsSpace().add(guy_control);
    }
    public void simpleUpdate(float tpf)
    {
    }
    public void hurt()
    {
        System.out.println("Oooouuuchhhhh I've been hit!");
        guy.setLocalScale((float)Math.random() * .25f + .75f);
        health --;
        if (health < 1)
        {
            guy.removeFromParent();
            stage.getBulletAppState().getPhysicsSpace().remove(guy_control);
        }
    }
    public int getId()
    {
        return Integer.parseInt(guy.getName());
    }
}