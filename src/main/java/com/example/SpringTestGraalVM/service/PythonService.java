package com.example.SpringTestGraalVM.service;

import com.example.SpringTestGraalVM.model.Person;

public interface PythonService {

    int testMethod_1(int a, int b);

    String testMethod_2();

    String testMethod_3(Person person);

    Person testMethod_4(Person person);

    void pythonConstructor();

}
