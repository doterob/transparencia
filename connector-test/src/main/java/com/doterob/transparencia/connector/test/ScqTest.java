package com.doterob.transparencia.connector.test;

import com.doterob.transparencia.connector.scq.PDFManager;
import com.doterob.transparencia.connector.scq.pdfbox.PDFBoxManager;
import com.doterob.transparencia.model.Contract;

import java.io.IOException;
import java.net.URISyntaxException;

public class ScqTest {

	public static void main(String[] args) throws IOException, URISyntaxException {

		final PDFManager pdfManager = new PDFBoxManager(
				"http://www.santiagodecompostela.gal/medi/transparencia/Contratacion_municipal/Contratos_menores/Relacion_de_contratos_menores_2016/C-2016-1.pdf");

		for(Contract contract : pdfManager.extract()){
			System.out.println(contract);
		}
	}
}
