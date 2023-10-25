package com.example.SpringTestGraalVM.service;

import com.example.SpringTestGraalVM.model.Person;
import com.example.SpringTestGraalVM.model.ScriptContext;
import com.example.SpringTestGraalVM.pool.ContextPool;
import com.example.SpringTestGraalVM.scriptConfiguration.PythonConfiguration;
import org.springframework.stereotype.Service;

@Service
public class ScriptService {

    public int getMethod1() {
        ScriptContext context = ContextPool.borrowContext();
        int result = PythonConfiguration.getPythonService(context).testMethod_1(5, 10);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ContextPool.returnContext(context);
        return result;
    }

    public String getMethod2() {
        ScriptContext context = ContextPool.borrowContext();
        String result = PythonConfiguration.getPythonService(context).testMethod_2();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ContextPool.returnContext(context);
        return result;
    }

    public String getMethod3() {
        ScriptContext context = ContextPool.borrowContext();
        String result = PythonConfiguration.getPythonService(context).testMethod_3(new Person("Ivan", "ivan@mail.ru"));
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ContextPool.returnContext(context);
        return result;
    }

    public Person getMethod4() {
        ScriptContext context = ContextPool.borrowContext();
        Person result = PythonConfiguration.getPythonService(context).testMethod_4(new Person("Ivan", "ivan@mail.ru"));
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ContextPool.returnContext(context);
        return result;
    }

}
