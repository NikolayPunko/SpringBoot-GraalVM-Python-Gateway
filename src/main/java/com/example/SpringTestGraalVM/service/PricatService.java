package com.example.SpringTestGraalVM.service;

import com.example.SpringTestGraalVM.model.Pricat;
import com.example.SpringTestGraalVM.repositories.PricatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PricatService {

    private final PricatRepository pricatRepository;

    @Autowired
    public PricatService(PricatRepository pricatRepository) {
        this.pricatRepository = pricatRepository;
    }

    public List<Pricat> findAll() {
        return pricatRepository.findAll();
    }
}
