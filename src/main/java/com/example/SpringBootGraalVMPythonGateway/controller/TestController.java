package com.example.SpringBootGraalVMPythonGateway.controller;
import com.example.SpringBootGraalVMPythonGateway.service.PricatService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/test")
public class TestController {

    private final PricatService pricatService;

    public TestController(PricatService pricatService) {
        this.pricatService = pricatService;
    }

    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        return "Локальный: " + request.getLocalAddr() + "; Удаленный: "+ request.getRemoteAddr()
                +"; Remote host: " + request.getRemoteHost() + "; Header \"X-FORWARDED-FOR\":" + request.getHeader("X-FORWARDED-FOR");
    }


    @GetMapping("/checkHeaders")
    public Map<String, String> checkHeaders(@RequestHeader Map<String, String> headers) {
        return headers;
    }



}
