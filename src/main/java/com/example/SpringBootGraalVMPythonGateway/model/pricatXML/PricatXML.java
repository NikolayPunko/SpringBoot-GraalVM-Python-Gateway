package com.example.SpringBootGraalVMPythonGateway.model.pricatXML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "PRICAT")
public class PricatXML {

    @JacksonXmlProperty(localName = "BGM")
    private PR_BGM pr_bgm;

    @JacksonXmlProperty(localName = "DTM")
    private PR_DTM pr_dtm;

    @JacksonXmlProperty(localName = "SG2")
    @JacksonXmlCData
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<PR_SG2> pr_SG2_list = new ArrayList<>();


    @Override
    public String toString() {
        return "PricatXML{" +
                "pr_bgm=" + pr_bgm.getPr_s106().getPr_e1004() +
                ", pr_dtm=" + pr_dtm.getPr_c507().getPr_e2380() +
                ", pr_SG2_RECEIVER=" + pr_SG2_list.get(0).getPr_nad().getPr_c082().getPr_e3039() +
                ", pr_SG2_SENDER=" + pr_SG2_list.get(1).getPr_nad().getPr_c082().getPr_e3039() +
                '}';
    }


}


