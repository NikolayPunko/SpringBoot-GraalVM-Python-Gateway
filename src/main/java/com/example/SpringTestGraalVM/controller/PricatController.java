package com.example.SpringTestGraalVM.controller;

import com.example.SpringTestGraalVM.dto.PricatResponseDTO;
import com.example.SpringTestGraalVM.model.Pricat;
import com.example.SpringTestGraalVM.service.PricatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pricat")
public class PricatController {

    private final PricatService pricatService;

    @Autowired
    public PricatController(PricatService pricatService) {
        this.pricatService = pricatService;
    }

    @GetMapping()
    public List<PricatResponseDTO> getPricats(){
        return pricatService.findAll().stream().map(x -> convertToPricatResponseDTO(x)).collect(Collectors.toList());
    }

    private PricatResponseDTO convertToPricatResponseDTO(Pricat pricat) {
        return new ModelMapper().map(pricat, PricatResponseDTO.class);
    }
}
