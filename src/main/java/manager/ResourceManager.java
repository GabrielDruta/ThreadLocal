package manager;

import resource.Resource;


/**
 * THREAD oriented specific resource MANAGER
 * This MANAGER is usually designed as a SINGLETON
 * <p>
 * OBSERVE the MAP "behavior" of a ThreadLocal
 * A ThreadLocal is like a "Map" for which the KEY is always the {@link Thread#currentThread()}
 * <p>
 * OBSERVE the "cache" approach through the usage of java.lang.ref.WeakReference under the hood (java.lang.ThreadLocal.ThreadLocalMap)
 *
 * @see ThreadLocal#get()
 * @see ThreadLocal#set(Object)
 * @see ThreadLocal#remove()
 */
public class ResourceManager extends ThreadLocal<Resource> {

    /*
     * Returns the current thread's "initial value" for this thread-local variable.
     */


    @Override
    public Resource initialValue(){
        return new Resource();
    }
}
