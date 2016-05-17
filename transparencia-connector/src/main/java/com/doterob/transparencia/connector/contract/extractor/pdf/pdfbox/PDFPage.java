package com.doterob.transparencia.connector.contract.extractor.pdf.pdfbox;

import com.doterob.transparencia.model.ContractComplex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dotero on 07/05/2016.
 */
public class PDFPage {

    private static final Logger LOG = LogManager.getLogger(PDFPage.class);

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

    public Map<String, ContractComplex> extractContracts(PDFTextStripperByArea stripper){

        final Map<String, ContractComplex> result = new HashMap<String, ContractComplex>();

        for(int i = 0; i < areas.size(); i++){

            final ContractComplex contract = areas.get(i).extractContact(stripper);
           /* if(contract.isValid()) {
                result.put(contract.getId(), contract);
            }*/
        }

        return result;
    }

    public void printRegions(){

        for(PDFContractArea row : areas){
            LOG.debug(row.toString());
        }
    }
}
