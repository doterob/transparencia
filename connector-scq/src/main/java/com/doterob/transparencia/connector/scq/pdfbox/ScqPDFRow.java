package com.doterob.transparencia.connector.scq.pdfbox;

import com.doterob.transparencia.model.Contract;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.*;

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

    public  Rectangle getEntityId(){
        return new Rectangle(136,row,24,8);
    }

    public  Rectangle getEntityName(){
        return new Rectangle(166,row,130,8);
    }

    public  Rectangle getArea(){
        return new Rectangle(370,row+10,100,30);
    }

    public  Rectangle getAmount(){
        return new Rectangle(750,row,50,8);
    }

    public void addRegion(PDFTextStripperByArea stripper){
        stripper.addRegion(row+"-id", getId());
        stripper.addRegion(row+"-date", getDate());
        stripper.addRegion(row+"-subject",getSubject());
        stripper.addRegion(row+"-entityId",getEntityId());
        stripper.addRegion(row+"-entityName",getEntityName());
        stripper.addRegion(row+"-area",getArea());
        stripper.addRegion(row+"-amount",getAmount());
    }

    public Contract getContact(PDFTextStripperByArea stripper){
        return new Contract(stripper.getTextForRegion(row+"-id").replace("\r\n"," ").trim(),
                stripper.getTextForRegion(row+"-date").replace("\r\n"," ").trim(),
                stripper.getTextForRegion(row+"-subject").replace("\r\n"," ").trim(),
                stripper.getTextForRegion(row+"-entityId").replace("\r\n"," ").trim(),
                stripper.getTextForRegion(row+"-entityName").replace("\r\n"," ").trim(),
                stripper.getTextForRegion(row+"-area").replace("\r\n"," ").trim(),
                stripper.getTextForRegion(row+"-amount").replace("\r\n"," ").trim());
    }

    @Override
    public String toString(){
        return "ScqPDFRow{row:" + row + ", id:" + getId() + ", date:" + getDate() + ", subject:" + getSubject()
                + ", entityId:" + getEntityId() + ", entityName:" + getEntityName() + ", area:" + getArea()
                + ", amount:" + getAmount() + "}";
    }
}
