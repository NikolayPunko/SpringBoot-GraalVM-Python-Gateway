package com.example.SpringBootGraalVMPythonGateway.controller;
import com.example.SpringBootGraalVMPythonGateway.service.PricatService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    private final PricatService pricatService;

    public TestController(PricatService pricatService) {
        this.pricatService = pricatService;
    }

    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        return "Локальный: " + request.getLocalAddr() + "; Удаленный: "+ request.getRemoteAddr()
                +"; Remote host: " + request.getRemoteHost() + "; Header \"X-FORWARDED-FOR\":" +request.getHeader("X-FORWARDED-FOR");
    }



}
