package com.example.SpringTestGraalVM.service;

import com.example.SpringTestGraalVM.model.UserOrg;

public interface PythonService {

    int testMethod_1(int a, int b);

    String testMethod_2();

    String testMethod_3(UserOrg userOrg);

    UserOrg testMethod_4(UserOrg userOrg);

    void pythonConstructor();

}
