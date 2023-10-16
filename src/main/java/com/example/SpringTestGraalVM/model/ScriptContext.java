package com.example.SpringTestGraalVM.model;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.HostAccess;

public class ScriptContext {

    private final Context context;
    private final int id;

    public ScriptContext(int id, Engine engine){
//        System.out.println("Creating new Context with ID: " + id);
        this.id = id;
        this.context = Context
                .newBuilder("python")
                .allowHostAccess(HostAccess.ALL)
                .allowAllAccess(true)
                .option("python.ForceImportSite", "true")
                .engine(engine)
                .build();
    }

    public Integer getId() {
        return id;
    }

    public Context getContext() {
        return context;
    }
}
