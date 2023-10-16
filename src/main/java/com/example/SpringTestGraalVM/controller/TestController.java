package com.example.SpringTestGraalVM.controller;
import com.example.SpringTestGraalVM.model.Person;
import com.example.SpringTestGraalVM.scriptConfiguration.PythonConfiguration;
import com.example.SpringTestGraalVM.service.PythonService;
import com.example.SpringTestGraalVM.service.ScriptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/py")
public class TestController {

    private final ScriptService scriptService;

    public TestController(ScriptService scriptService) {
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
    public Person testMethod4() {
        return scriptService.getMethod4();
    }



}
