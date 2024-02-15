package com.example.SpringBootGraalVMPythonGateway.controller;
import com.example.SpringBootGraalVMPythonGateway.model.UserOrg;
import com.example.SpringBootGraalVMPythonGateway.scriptConfiguration.PythonConfiguration;
import com.example.SpringBootGraalVMPythonGateway.service.ScriptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/py")
public class ScriptController {

    private final ScriptService scriptService;

    public ScriptController(ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    @GetMapping("/reset-source-script") //обновить скрипты
    public void resetSourceScript() {
        PythonConfiguration.assignSource();
    }


    @GetMapping("/method1")
    public int testMethod1() {
        return scriptService.getMethod1();
    }

    @GetMapping("/method2")
    public String testMethod2() {
        return scriptService.getMethod2();
    }


    @GetMapping("/method3")
    public String testMethod3() {
        return scriptService.getMethod3();
    }

    @GetMapping("/method4")
    public UserOrg testMethod4() {
        return scriptService.getMethod4();
    }

    @GetMapping("/test")
    public String test() {
        return "The service is working!";
    }




}
