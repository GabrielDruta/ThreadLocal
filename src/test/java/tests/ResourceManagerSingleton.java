package tests;

import manager.ResourceManager;
import resource.Resource;

final public class ResourceManagerSingleton {

    final private static ResourceManager INSTANCE= new ResourceManager();

    private ResourceManagerSingleton(){
        //singleton
    }

    public static ResourceManager getInstance(){
        return INSTANCE;
    }

}
