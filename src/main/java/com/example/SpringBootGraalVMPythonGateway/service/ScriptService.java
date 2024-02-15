package com.example.SpringBootGraalVMPythonGateway.service;

import com.example.SpringBootGraalVMPythonGateway.model.UserOrg;
import com.example.SpringBootGraalVMPythonGateway.model.ScriptContext;
import com.example.SpringBootGraalVMPythonGateway.pool.ContextPool;
import com.example.SpringBootGraalVMPythonGateway.scriptConfiguration.PythonConfiguration;
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
        String result = PythonConfiguration.getPythonService(context).testMethod_3(new UserOrg("Ivan", "ivan@mail.ru"));
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ContextPool.returnContext(context);
        return result;
    }

    public UserOrg getMethod4() {
        ScriptContext context = ContextPool.borrowContext();
        UserOrg result = PythonConfiguration.getPythonService(context).testMethod_4(new UserOrg("Ivan", "ivan@mail.ru"));
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ContextPool.returnContext(context);
        return result;
    }

}
