package com.doterob.transparencia.connector.contract.extractor.local.scq;

import com.doterob.transparencia.connector.contract.extractor.pdf.pdfbox.PDFContractArea;
import com.doterob.transparencia.connector.contract.extractor.pdf.pdfbox.PDFPage;

import java.awt.*;

/**
 * Created by dotero on 12/05/2016.
 */
public class PDFContractAreaScqBuilder {

    public static PDFPage get(int firstRow, int total, float step){

        final PDFPage result = new PDFPage();

        for(int i = 0; i < total; i++){

            int row = firstRow + Math.round(i*step);

            result.addArea(new PDFContractArea(String.valueOf(row), new Rectangle(40,row,45,8),
                 new Rectangle(100,row,27,8),
                 new Rectangle(136,row+10,180,30),
                 new Rectangle(136,row,24,8),
                 new Rectangle(166,row,130,8),
                 new Rectangle(370,row+10,100,30),
                 new Rectangle(750,row,50,8)
            ));
        }

        return result;
    }

    public static PDFPage get(int phase){
        return get(ScqPDFRowConstants.ROW_1 + phase, ScqPDFRowConstants.ROWS, ScqPDFRowConstants.STEP);
    }
}
