package com.example.SpringTestGraalVM.service;

import com.example.SpringTestGraalVM.model.Pricat;
import com.example.SpringTestGraalVM.model.pricatXML.PricatXML;
import com.example.SpringTestGraalVM.repositories.PricatRepository;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

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

    public void importPricat(MultipartFile file) {
        try {
            XmlMapper xmlMapper = new XmlMapper();

//            Person person = xmlMapper.readValue(file.getBytes(), Person.class);

            PricatXML pricatXML = xmlMapper.readValue(file.getBytes(), PricatXML.class);


            System.out.println(pricatXML);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream outputStream = new FileOutputStream(new File(file.getOriginalFilename()));
        outputStream.write(file.getBytes());
        outputStream.close();
        return convFile;
    }
}
