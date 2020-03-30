/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import com.jme3.export.Savable;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
 
public class ShootControl extends AbstractControl implements Savable, Cloneable
{
    BoxGuy thing;
 
    public ShootControl(BoxGuy t)
    {
        thing = t;
    }
 
    public Control cloneForSpatial(Spatial arg0)
    {
        return null;
    }
 
    protected void controlRender(RenderManager arg0, ViewPort arg1) {}
 
    public void hurt()
    {
        thing.hurt();
    }
 
    @Override
    protected void controlUpdate(float arg0) {}
}