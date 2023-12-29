package com.example.SpringTestGraalVM.controller;

import com.example.SpringTestGraalVM.dto.PricatResponseDTO;
import com.example.SpringTestGraalVM.model.Pricat;
import com.example.SpringTestGraalVM.service.PricatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PricatController {

    private final PricatService pricatService;

    @Autowired
    public PricatController(PricatService pricatService) {
        this.pricatService = pricatService;
    }

    @GetMapping("/pricat")
    public List<PricatResponseDTO> getPricats(){
        return pricatService.findAll().stream().map(x -> convertToPricatResponseDTO(x)).collect(Collectors.toList());
    }

//    @PostMapping("/import/PRICAT/zipWithXml")
//    public void importPricat(@RequestParam("file[0]") MultipartFile file){
//        System.out.println(file.getOriginalFilename());
//        System.out.println(file.getContentType());
//
//    }

    @PostMapping("/import/PRICAT/Xml")
    public void importPricat(@RequestParam("xml") MultipartFile file){
        pricatService.importPricat(file);
    }

    @GetMapping("/PRICAT/send/ID")
    public void sendPricat(){

    }

    @PostMapping("/PRICAT/{documentState}/list") //Чтение списка документов
    public void getPricatByState(){

    }

    @GetMapping("/PRICAT/{documentId}") //Чтение документа
    public void getPricatById(){

    }

    private PricatResponseDTO convertToPricatResponseDTO(Pricat pricat) {
        return new ModelMapper().map(pricat, PricatResponseDTO.class);
    }
}
