package com.example.SpringBootGraalVMPythonGateway.service;
import com.example.SpringBootGraalVMPythonGateway.dto.PricatFilterRequestDTO;
import com.example.SpringBootGraalVMPythonGateway.exceptions.XMLParsingException;
import com.example.SpringBootGraalVMPythonGateway.model.Pricat;
import com.example.SpringBootGraalVMPythonGateway.repositories.PricatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RecadvService {

    private final PricatRepository pricatRepository;
    private final EdocService edocService;

    @Autowired
    public RecadvService(PricatRepository pricatRepository, EdocService edocService) {
        this.pricatRepository = pricatRepository;
        this.edocService = edocService;
    }

    @Transactional
    public long importRecadv(MultipartFile file) {
        Pricat pricat = parsingXMLtoRecadv(EdocService.trimXML(EdocService.convertXMLtoString(file)), new Pricat());
        pricat.setTP("RECADV");
        return edocService.importEdoc(pricat);
    }

    @Transactional
    public void sendRecadv(long id) {
        edocService.sendEdoc("RECADV", id);
    }


    public String findRecadvById(long id) {
        return edocService.findEdocById("RECADV", id);
    }

    public List<Pricat> findRecadvByState(String state, PricatFilterRequestDTO filterDTO, int page, int size) {
        return edocService.findEdocByState("RECADV", state, filterDTO, page, size);
    }

    public Node findChildNode(Node node, String child) {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            if (node.getChildNodes().item(i).getNodeName().equals(child)) {
                return node.getChildNodes().item(i);
            }
        }
        return null;
    }

    public Pricat parsingXMLtoRecadv(String xml, Pricat pricat) {
        try {
            String NDE = "", DTDOC = "", RECEIVER = "", SENDER = "";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));

            if(!document.getDocumentElement().getTagName().equals("RECADV")){
                throw new XMLParsingException();
            }

            NodeList BGMList = document.getDocumentElement().getElementsByTagName("BGM");
            for (int i = 0; i < BGMList.getLength(); i++) {
                if (BGMList.item(i).getParentNode().getNodeName().equals("RECADV")) {
                    NDE = findChildNode(findChildNode(BGMList.item(i), "C106"), "E1004").getTextContent();
                }
            }

            NodeList DTMList = document.getDocumentElement().getElementsByTagName("DTM");
            for (int i = 0; i < DTMList.getLength(); i++) {
                if (DTMList.item(i).getParentNode().getNodeName().equals("RECADV")) {
                    DTDOC = findChildNode(findChildNode(DTMList.item(i), "C507"), "E2380").getTextContent();
                }
            }

            NodeList SG4List = document.getDocumentElement().getElementsByTagName("SG4");
            for (int i = 0; i < SG4List.getLength(); i++) {
                Node node = findChildNode(findChildNode(SG4List.item(i), "NAD"), "E3035");
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


}
