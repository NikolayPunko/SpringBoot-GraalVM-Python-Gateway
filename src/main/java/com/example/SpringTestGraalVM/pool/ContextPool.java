package com.example.SpringTestGraalVM.pool;

import com.example.SpringTestGraalVM.model.ScriptContext;
import org.graalvm.polyglot.Engine;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ContextPool {

    private static final Engine engine = Engine.create();
    private static final ContextPool pool_manager = new ContextPool();
    private static final int POOL_SIZE = 200;
    private final BlockingQueue<ScriptContext> pool;

    private ContextPool() {
        pool = new LinkedBlockingQueue<>();
        for (int i = 0; i < POOL_SIZE; i++) pool.add(new ScriptContext(i, engine));
    }

    public static ScriptContext borrowContext()  {
//        System.out.println("Thread [" + Thread.currentThread().getName() + "] is waiting for borrow request");
        try {
            return pool_manager.pool.take();
        } catch (InterruptedException e) {
            System.out.println("===Error taking from pool!===");
            throw new RuntimeException(e);
        }
    }

    public static void returnContext(ScriptContext scriptContext) {
//        System.out.println("Thread [" + Thread.currentThread().getName() + "] is returning Context with ID [" + scriptContext.getId() + "]");
        try {
            pool_manager.pool.put(scriptContext);
        } catch (InterruptedException e) {
            System.out.println("===Error returning to pool!===");
            throw new RuntimeException(e);
        }
    }
}
