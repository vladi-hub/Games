/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.network.synch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import mirrormonkey.core.context.ClientSyncContext;
import mirrormonkey.core.context.ServerSyncContext;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface RpcTarget {
    
    public Class<?>[] sending() default ServerSyncContext.class;
    public Class<?>[] sendingHierarchy() default {};
    public Class<?>[] receiving() default ClientSyncContext.class;
    public Class<?>[] receivingHierarchy() default {};
}
