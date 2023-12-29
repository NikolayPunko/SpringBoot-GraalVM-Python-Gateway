package com.example.SpringTestGraalVM.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
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
//@JacksonXmlRootElement(localName = "Person")
public class Person {

    @JacksonXmlProperty(localName = "firstName")
    private String gln1 ;

    @JacksonXmlProperty(localName = "lastName")
    private String gln2;

    @JacksonXmlProperty(localName = "name2")
    private String name3;

    @Override
    public String toString() {
        return "Person{" +
                "gln1='" + gln1 + '\'' +
                ", gln2='" + gln2 + '\'' +
                ", name3='" + name3 + '\'' +
                '}';
    }
}
