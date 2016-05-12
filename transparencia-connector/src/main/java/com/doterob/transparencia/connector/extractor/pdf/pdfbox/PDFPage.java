package com.doterob.transparencia.connector.extractor.pdf.pdfbox;

import com.doterob.transparencia.connector.scq.pdfbox.ScqPDFRowConstants;
import com.doterob.transparencia.model.Contract;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dotero on 07/05/2016.
 */
public class PDFPage {

    /*private final int firstRow;
    private final int total;
    private final float step;*/
    private final List<PDFContractArea> areas;

    public PDFPage(List<PDFContractArea> areas) {
        this.areas = areas;
    }

    public PDFPage() {
        this(new ArrayList<PDFContractArea>());
    }

    public void addArea(PDFContractArea area){
        this.areas.add(area);
    }

    public void addRegions(PDFTextStripperByArea stripper){

        for(PDFContractArea row : areas){
            row.addRegion(stripper);
        }
    }

    public Map<String, Contract> extractContracts(PDFTextStripperByArea stripper){

        final Map<String, Contract> result = new HashMap<String, Contract>();

        for(int i = 0; i < areas.size(); i++){

            final Contract contract = areas.get(i).extractContact(stripper);
            if(contract.isValid()) {
                result.put(contract.getId(), contract);
            }
        }

        return result;
    }

    public void printRegions(){

        for(PDFContractArea row : areas){
            System.out.println(row.toString());
        }
    }
}
