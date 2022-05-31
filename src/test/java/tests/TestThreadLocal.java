package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resource.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;




public class TestThreadLocal {

    /**
     * Usually the container will control the thread pool what container idk
     */
    final public static int NO_OF_THREADS=10;
    public final List<Thread> threadPool=new ArrayList<>();

    // used for testing purpose
    // ensure all threads are done when exiting
    final private static CountDownLatch doneSignal = new CountDownLatch(NO_OF_THREADS);

    @BeforeEach
    public void before(){
        for(int i=0; i<NO_OF_THREADS; i++){
            Thread thread=new Thread(new MyRunnable()) ;
            thread.setName("thread"+i);
            threadPool.add(thread);

            }
        }


    @Test
    public void testThreadLocal() throws InterruptedException {

        // now the container starts its threadPool
        for(Thread thread : threadPool){
            thread.start();
        }

        // wait for all threadPool to complete
        doneSignal.await();

        //verify thread resources did not overlap
        int i=0;
        for(Thread thread:threadPool){
            String threadName="thread"+i;
            Assertions.assertEquals(null+threadName+threadName+threadName, thread.getName());
            i++;
        }


    }

    final private static class MyRunnable implements Runnable{


        @Override
        public void run() {

            Resource resource;

            //access thread local vriable for the first time
            resource=ResourceManagerSingleton.getInstance().get();
            System.out.println(Thread.currentThread().getName()+" thread has accessed for the first time its thread loval variable " + resource);
            resource.setData(resource.getData() + Thread.currentThread().getName());

            doSomeImportantWork(100);

            //should acess the same thread local variable the second ttime around
            resource=ResourceManagerSingleton.getInstance().get();
            System.out.println(Thread.currentThread().getName()+" thread has accessed for the second time its thread loval variable " + resource);
            resource.setData(resource.getData() + Thread.currentThread().getName());

            doSomeImportantWork(500);

            //should access the same thread loval variable and now remove it
            resource=ResourceManagerSingleton.getInstance().get();
            resource.setData(resource.getData() + Thread.currentThread().getName());
            System.out.println("removing thread specific variable for " + Thread.currentThread().getName() + " and variable " + resource);

            Thread.currentThread().setName(resource.getData());
            ResourceManagerSingleton.getInstance().remove();

            // XXX , NOT IMPORTANT , for testing purpose notify this latch to "close"
            doneSignal.countDown();
        }

        private void doSomeImportantWork(int i) {
            try {
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
