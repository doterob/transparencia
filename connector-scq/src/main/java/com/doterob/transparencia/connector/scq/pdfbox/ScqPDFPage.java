package com.doterob.transparencia.connector.scq.pdfbox;

import com.doterob.transparencia.model.Contract;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dotero on 07/05/2016.
 */
public class ScqPDFPage {

    private final int firstRow;
    private final int total;
    private final float step;
    private final List<ScqPDFRow> rows;

    private ScqPDFPage(int firstRow, int total, float step, List<ScqPDFRow> rows) {
        this.firstRow = firstRow;
        this.total = total;
        this.step = step;
        this.rows = rows;
    }

    public static ScqPDFPage get(int firstRow, int total, float step){

        final List<ScqPDFRow> rows = new ArrayList<ScqPDFRow>();

        for(int i = 0; i < total; i++){
         rows.add(new ScqPDFRow(firstRow + Math.round(i*step)));
        }

        return new ScqPDFPage(firstRow,total,step,rows);
    }

    public static ScqPDFPage get(int phase){
        return ScqPDFPage.get(ScqPDFRowConstants.ROW_1 + phase, ScqPDFRowConstants.ROWS, ScqPDFRowConstants.STEP);
    }

    public static ScqPDFPage get(){
        return ScqPDFPage.get(0);
    }

    public void addRegions(PDFTextStripperByArea stripper){

        for(ScqPDFRow row : rows){
            row.addRegion(stripper);
        }
    }

    public Map<String, Contract> extractContracts(PDFTextStripperByArea stripper){

        final Map<String, Contract> result = new HashMap<String, Contract>();

        for(int i = 0; i < rows.size();i++){

            final Contract contract = rows.get(i).getContact(stripper);
            if(contract.isValid()) {
                result.put(contract.getId(), contract);
            }
        }

        return result;
    }

    public void printRegions(){

        for(ScqPDFRow row : rows){
            System.out.println(row.toString());
        }
    }
}
