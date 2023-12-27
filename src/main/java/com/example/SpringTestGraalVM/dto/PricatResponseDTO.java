package com.example.SpringTestGraalVM.dto;

import com.example.SpringTestGraalVM.model.UserOrg;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PricatResponseDTO {

    private long F_GUID;
//    private UserOrg owner;
    private long F_ID;
    private Date F_TM;
    private int F_DEL;
    private String EDI;
    private String TP;
    private String PST;
    private String NDE;
    private Date DT;
    private Date DTDOC;
    private long RECEIVER;
    private long SENDER;
    private String DOC;
    private long ID;
    private Date DTINS;
    private Date DTUPD;

}
