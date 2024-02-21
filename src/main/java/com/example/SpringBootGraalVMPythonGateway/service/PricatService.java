package com.example.SpringBootGraalVMPythonGateway.service;

import com.example.SpringBootGraalVMPythonGateway.dto.PricatFilterRequestDTO;
import com.example.SpringBootGraalVMPythonGateway.exceptions.UserOrgNotFoundException;
import com.example.SpringBootGraalVMPythonGateway.exceptions.PricatNotFoundException;
import com.example.SpringBootGraalVMPythonGateway.exceptions.XMLParsingException;
import com.example.SpringBootGraalVMPythonGateway.model.Pricat;
import com.example.SpringBootGraalVMPythonGateway.model.UserOrg;
import com.example.SpringBootGraalVMPythonGateway.repositories.PricatRepository;
import com.example.SpringBootGraalVMPythonGateway.repositories.UsersRepository;
import com.example.SpringBootGraalVMPythonGateway.security.UserOrgDetails;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@Service
@Transactional(readOnly = true)
public class PricatService {

    private final PricatRepository pricatRepository;

    private final EdocService edocService;

    @Autowired
    public PricatService(PricatRepository pricatRepository, EdocService edocService) {
        this.pricatRepository = pricatRepository;
        this.edocService = edocService;
    }

    public List<Pricat> findAll() {
        return pricatRepository.findAll();
    }

    @Transactional
    public long importPricat(MultipartFile file) {
        Pricat pricat = parsingXMLtoPricat(EdocService.trimXML(EdocService.convertXMLtoString(file)), new Pricat());
        pricat.setTP("PRICAT");
        return edocService.importEdoc(pricat);
    }

    @Transactional
    public void sendPricat(long id) {
        edocService.sendEdoc("PRICAT", id);
    }


    public String findPricatById(long id) {
        return edocService.findEdocById("PRICAT", id);
    }

    public List<Pricat> findPricatByState(String state, PricatFilterRequestDTO filterDTO, int page, int size) {
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

    public Pricat parsingXMLtoPricat(String xml, Pricat pricat) {
        try {
            String NDE = "", DTDOC = "", RECEIVER = "", SENDER = "";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));

            if(!document.getDocumentElement().getTagName().equals("PRICAT")){
                throw new XMLParsingException();
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

            pricat.setNDE(NDE);
            pricat.setDTDOC(LocalDateTime.parse(DTDOC, EdocService.DATE_FORMAT));
            pricat.setRECEIVER(Long.parseLong(RECEIVER));
            pricat.setSENDER(Long.parseLong(SENDER));
            pricat.setDOC(xml);

            return pricat;
        } catch (Exception e) {
            throw new XMLParsingException();
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
