package com.doterob.transparencia.connector.scq.pdfbox;

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
public class ScqPDFRow {

    private final int row;

    public ScqPDFRow(int row) {
        this.row = row;
    }

    public Rectangle getId(){
        return new Rectangle(40,row,45,8);
    }

    public  Rectangle getDate(){
        return new Rectangle(100,row,27,8);
    }

    public  Rectangle getSubject(){
        return new Rectangle(136,row+10,180,30);
    }

    public  Rectangle getContractorId(){
        return new Rectangle(136,row,24,8);
    }

    public  Rectangle getContractorName(){
        return new Rectangle(166,row,130,8);
    }

    public  Rectangle getOrganizationArea(){
        return new Rectangle(370,row+10,100,30);
    }

    public  Rectangle getAmount(){
        return new Rectangle(750,row,50,8);
    }

    public void addRegion(PDFTextStripperByArea stripper){
        stripper.addRegion(row+"-id", getId());
        stripper.addRegion(row+"-date", getDate());
        stripper.addRegion(row+"-subject",getSubject());
        stripper.addRegion(row+"-contractorId", getContractorId());
        stripper.addRegion(row+"-contractorName", getContractorName());
        stripper.addRegion(row+"-organizationArea", getOrganizationArea());
        stripper.addRegion(row+"-amount",getAmount());
    }

    public Contract extractContact(PDFTextStripperByArea stripper){

        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        Float amount = null;
        try {
            date = formatter.parse(stripper.getTextForRegion(row + "-date").replace("\r\n", " ").trim());
            amount = Float.parseFloat(stripper.getTextForRegion(row+"-amount").replace("\r\n"," ").replace(".","").replace(",",".").trim());
        } catch (ParseException|NumberFormatException e){}

        return new Contract(stripper.getTextForRegion(row+"-id").replace("\r\n"," ").trim(),
                date,
                stripper.getTextForRegion(row+"-subject").replace("\r\n"," ").trim(),
                stripper.getTextForRegion(row+"-contractorId").replace("\r\n"," ").trim(),
                stripper.getTextForRegion(row+"-contractorName").replace("\r\n"," ").trim(),
                stripper.getTextForRegion(row+"-organizationArea").replace("\r\n"," ").trim(),
                amount);
    }

    @Override
    public String toString(){
        return "ScqPDFRow{row:" + row + ", id:" + getId() + ", date:" + getDate() + ", subject:" + getSubject()
                + ", contractorId:" + getContractorId() + ", contractorName:" + getContractorName() + ", organizationArea:" + getOrganizationArea()
                + ", amount:" + getAmount() + "}";
    }
}
