package com.example.SpringTestGraalVM.model.pricatXML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private PR_SG2 pr_SG2_RECEIVER;

    @JacksonXmlProperty(localName = "SG2")
    private PR_SG2 pr_SG2_SENDER;


    @Override
    public String toString() {
        return "PricatXML{" +
                "pr_bgm=" + pr_bgm.getPr_s106().getPr_e1004() +
                ", pr_dtm=" + pr_dtm.getPr_c507().getPr_e2380() +
                ", pr_SG2_RECEIVER=" + pr_SG2_RECEIVER.getPr_nad().getPr_c082().getPr_e3039() +
                ", pr_SG2_SENDER=" + pr_SG2_SENDER.getPr_nad().getPr_c082().getPr_e3039() +
                '}';
    }
}

