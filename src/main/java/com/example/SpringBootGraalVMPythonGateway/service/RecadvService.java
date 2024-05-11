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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class RecadvService {

    private final EdocRepository edocRepository;
    private final EdocService edocService;

    @Autowired
    public RecadvService(EdocRepository edocRepository, EdocService edocService) {
        this.edocRepository = edocRepository;
        this.edocService = edocService;
    }

    @Transactional
    public long importRecadv(MultipartFile file) {
        Edoc edoc = parsingXMLtoRecadv(EdocService.trimXML(EdocService.convertXMLtoString(file)), new Edoc());
        edoc.setTP("RECADV");
        return edocService.importEdoc(edoc);
    }

    @Transactional
    public void sendRecadv(long id) {
        edocService.sendEdoc("RECADV", id);
    }


    public String findRecadvById(long id) {
        return edocService.findEdocById("RECADV", id).getDOC();
    }

    public List<Edoc> findRecadvByState(String state, EdocFilterRequestDTO filterDTO, int page, int size) {
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

    public Edoc parsingXMLtoRecadv(String xml, Edoc edoc) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));

            Map<String,String> fields = new HashMap<>();

            switch (document.getDocumentElement().getTagName()){
                case "RECADV" -> fields = parseOld(document,fields);
                case "BLRADF" ->  fields = parseNew(document,fields);
                default -> throw new XMLParsingException("Не удалось распарсить xml файл, главный тег не соответствует ожидаемому!");
            }


            edoc.setNDE(fields.get("NDE"));
            edoc.setDTDOC(LocalDateTime.parse(fields.get("DTDOC"), EdocService.DATE_FORMAT));
            edoc.setRECEIVER(Long.parseLong(fields.get("RECEIVER")));
            edoc.setSENDER(Long.parseLong(fields.get("SENDER")));
            edoc.setDOC(xml);

            return edoc;
        } catch (Exception e) {
            throw new XMLParsingException("Не удалось распарсить xml файл!");
        }
    }

    private Map<String,String> parseOld(Document document, Map<String,String> fields){

        NodeList BGMList = document.getDocumentElement().getElementsByTagName("BGM");
        for (int i = 0; i < BGMList.getLength(); i++) {
            if (BGMList.item(i).getParentNode().getNodeName().equals("RECADV")) {
                fields.put("NDE",findChildNode(findChildNode(BGMList.item(i), "C106"), "E1004").getTextContent());
            }
        }

        NodeList DTMList = document.getDocumentElement().getElementsByTagName("DTM");
        for (int i = 0; i < DTMList.getLength(); i++) {
            if (DTMList.item(i).getParentNode().getNodeName().equals("RECADV")) {
                fields.put("DTDOC",findChildNode(findChildNode(DTMList.item(i), "C507"), "E2380").getTextContent());
            }
        }

        NodeList SG4List = document.getDocumentElement().getElementsByTagName("SG4");
        for (int i = 0; i < SG4List.getLength(); i++) {
            Node node = findChildNode(findChildNode(SG4List.item(i), "NAD"), "E3035");
            if (node.getTextContent().equals("BY")) {
                fields.put("RECEIVER",findChildNode(findChildNode(node.getParentNode(), "C082"), "E3039").getTextContent());
            } else if (node.getTextContent().equals("SU")) {
                fields.put("SENDER",findChildNode(findChildNode(node.getParentNode(), "C082"), "E3039").getTextContent());
            }
        }

        return fields;
    }

    private Map<String,String> parseNew(Document document, Map<String,String> fields){

        NodeList nde = document.getDocumentElement().getElementsByTagName("Actdif");
        fields.put("NDE",findChildNode(nde.item(0),"ID").getTextContent());

        NodeList dtdoc = document.getDocumentElement().getElementsByTagName("Actdif");
        fields.put("DTDOC",findChildNode(dtdoc.item(0),"ActDifDate").getTextContent());

        NodeList sender = document.getDocumentElement().getElementsByTagName("Shipper");
        fields.put("SENDER",findChildNode(sender.item(0),"GLN").getTextContent());

        NodeList receiver = document.getDocumentElement().getElementsByTagName("Receiver");
        fields.put("RECEIVER",findChildNode(receiver.item(0),"GLN").getTextContent());

        return fields;
    }

}
