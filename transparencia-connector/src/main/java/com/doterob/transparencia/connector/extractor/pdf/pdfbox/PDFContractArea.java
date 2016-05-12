package com.doterob.transparencia.connector.extractor.pdf.pdfbox;

import com.doterob.transparencia.model.Contract;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dotero on 07/05/2016.
 */
public class PDFContractArea {

    private final String id;
    private final Rectangle code;
    private final Rectangle date;
    private final Rectangle subject;
    private final Rectangle contractorId;
    private final Rectangle contractorName;
    private final Rectangle organizationArea;
    private final Rectangle amount;

    public PDFContractArea(String id, Rectangle code, Rectangle date, Rectangle subject,
                           Rectangle contractorId, Rectangle contractorName, Rectangle organizationArea, Rectangle amount) {
        this.id = id;
        this.code = code;
        this.date = date;
        this.subject = subject;
        this.contractorId = contractorId;
        this.contractorName = contractorName;
        this.organizationArea = organizationArea;
        this.amount = amount;
    }

    public void addRegion(PDFTextStripperByArea stripper){
        stripper.addRegion(id+"-id", code);
        stripper.addRegion(id+"-date", date);
        stripper.addRegion(id+"-subject",subject);
        stripper.addRegion(id+"-contractorId", contractorId);
        stripper.addRegion(id+"-contractorName", contractorName);
        stripper.addRegion(id+"-organizationArea", organizationArea);
        stripper.addRegion(id+"-amount",amount);
    }

    public Contract extractContact(PDFTextStripperByArea stripper){

        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        Float amount = null;
        try {
            date = formatter.parse(stripper.getTextForRegion(id + "-date").replace("\r\n", " ").trim());
            amount = Float.parseFloat(stripper.getTextForRegion(id+"-amount").replace("\r\n"," ").replace(".","").replace(",",".").trim());
        } catch (ParseException|NumberFormatException e){}

        return new Contract(stripper.getTextForRegion(id+"-id").replace("\r\n"," ").trim(),
                date,
                stripper.getTextForRegion(id+"-subject").replace("\r\n"," ").trim(),
                stripper.getTextForRegion(id+"-contractorId").replace("\r\n"," ").trim(),
                stripper.getTextForRegion(id+"-contractorName").replace("\r\n"," ").trim(),
                stripper.getTextForRegion(id+"-organizationArea").replace("\r\n"," ").trim(),
                amount);
    }

    @Override
    public String toString(){
        return "ScqPDFRow{row:" + id + ", code:" + code + ", date:" + date + ", subject:" + subject
                + ", contractorId:" + contractorId + ", contractorName:" + contractorName + ", organizationArea:" + organizationArea
                + ", amount:" + amount + "}";
    }
}
