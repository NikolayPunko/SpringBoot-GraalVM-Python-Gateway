package com.example.SpringBootGraalVMPythonGateway.service;

import com.example.SpringBootGraalVMPythonGateway.dto.EdocFilterRequestDTO;
import com.example.SpringBootGraalVMPythonGateway.exceptions.XMLParsingException;
import com.example.SpringBootGraalVMPythonGateway.model.Edoc;
import com.example.SpringBootGraalVMPythonGateway.repositories.EdocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@Service
@Transactional(readOnly = true)
public class PricatService {

    private final EdocRepository edocRepository;

    private final EdocService edocService;

    @Autowired
    public PricatService(EdocRepository edocRepository, EdocService edocService) {
        this.edocRepository = edocRepository;
        this.edocService = edocService;
    }

    public List<Edoc> findAll() {
        return edocRepository.findAll();
    }

    @Transactional
    public long importPricat(MultipartFile file) {
        Edoc edoc = parsingXMLtoPricat(EdocService.trimXML(EdocService.convertXMLtoString(file)), new Edoc());
        edoc.setTP("PRICAT");
        return edocService.importEdoc(edoc);
    }

    @Transactional
    public void sendPricat(long id) {
        edocService.sendEdoc("PRICAT", id);
    }


    public String findPricatById(long id) {
        return edocService.findEdocById("PRICAT", id);
    }

    public List<Edoc> findPricatByState(String state, EdocFilterRequestDTO filterDTO, int page, int size) {
        return edocService.findEdocByState("PRICAT", state, filterDTO, page, size);
    }

    public Node findChildNode(Node node, String child) {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            if (node.getChildNodes().item(i).getNodeName().equals(child)) {
                return node.getChildNodes().item(i);
            }
        }
        return null;
    }

    public Edoc parsingXMLtoPricat(String xml, Edoc edoc) {
        try {
            String NDE = "", DTDOC = "", RECEIVER = "", SENDER = "";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));

            if(!document.getDocumentElement().getTagName().equals("PRICAT")){
                throw new XMLParsingException("Не удалось распарсить xml файл!");
            }

            NodeList BGMList = document.getDocumentElement().getElementsByTagName("BGM");
            for (int i = 0; i < BGMList.getLength(); i++) {
                if (BGMList.item(i).getParentNode().getNodeName().equals("PRICAT")) {
                    NDE = findChildNode(findChildNode(BGMList.item(i), "C106"), "E1004").getTextContent();
                }
            }

            NodeList DTMList = document.getDocumentElement().getElementsByTagName("DTM");
            for (int i = 0; i < DTMList.getLength(); i++) {
                if (DTMList.item(i).getParentNode().getNodeName().equals("PRICAT")) {
                    DTDOC = findChildNode(findChildNode(DTMList.item(i), "C507"), "E2380").getTextContent();
                }
            }

            NodeList SG2List = document.getDocumentElement().getElementsByTagName("SG2");
            for (int i = 0; i < SG2List.getLength(); i++) {
                Node node = findChildNode(findChildNode(SG2List.item(i), "NAD"), "E3035");
                if (node.getTextContent().equals("BY")) {
                    RECEIVER = findChildNode(findChildNode(node.getParentNode(), "C082"), "E3039").getTextContent();
                } else if (node.getTextContent().equals("SU")) {
                    SENDER = findChildNode(findChildNode(node.getParentNode(), "C082"), "E3039").getTextContent();
                }
            }

            edoc.setNDE(NDE);
            edoc.setDTDOC(LocalDateTime.parse(DTDOC, EdocService.DATE_FORMAT));
            edoc.setRECEIVER(Long.parseLong(RECEIVER));
            edoc.setSENDER(Long.parseLong(SENDER));
            edoc.setDOC(xml);

            return edoc;
        } catch (Exception e) {
            throw new XMLParsingException("Не удалось распарсить xml файл!");
        }
    }

//    public Pricat parsingXMLtoPricat(String xml, Pricat pricat) {
//        try {
//            String NDE = "", DTDOC = "", RECEIVER = "", SENDER = "";
//
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
//
//            NDE = document.getDocumentElement().getElementsByTagName("BGM").item(0).getChildNodes().item(1)
//                    .getChildNodes().item(0).getTextContent();
//
//            DTDOC = document.getDocumentElement().getElementsByTagName("DTM").item(0).getChildNodes().item(0)
//                    .getChildNodes().item(1).getTextContent();
//
//            NodeList SG2Elements = document.getDocumentElement().getElementsByTagName("SG2");
//            for (int i = 0; i < SG2Elements.getLength(); i++) {
//                Node node = SG2Elements.item(i).getChildNodes().item(0)
//                        .getChildNodes().item(0);
//                if (node.getNodeName().equals("E3035") && node.getTextContent().equals("BY")) {
//                    RECEIVER = node.getParentNode().getChildNodes().item(1).getFirstChild().getTextContent();
//                } else if (node.getNodeName().equals("E3035") && node.getTextContent().equals("SU")) {
//                    SENDER = node.getParentNode().getChildNodes().item(1).getFirstChild().getTextContent();
//                }
//            }
//
//            pricat.setNDE(NDE);
//            pricat.setDTDOC(LocalDateTime.parse(DTDOC, EdocService.DATE_FORMAT));
//            pricat.setRECEIVER(Long.parseLong(RECEIVER));
//            pricat.setSENDER(Long.parseLong(SENDER));
//            pricat.setDOC(xml);
//
//            return pricat;
//        } catch (Exception e) {
//            throw new XMLParsingException();
//        }
//    }



}
