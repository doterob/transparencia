package com.doterob.transparencia.connector.contract.extractor.pdf.itext;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.doterob.transparencia.connector.contract.extractor.pdf.PDFManager;
import com.doterob.transparencia.model.ContractComplex;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class ITextManager implements PDFManager {

	private final String url;

	public ITextManager(String url) {
		this.url = url;
	}

	public String testToText() throws IOException {
		StringBuilder result = new StringBuilder();
		PdfReader reader = new PdfReader(new URL(url));
		for(int i = 1; i <= reader.getNumberOfPages();i++) {
			String page = PdfTextExtractor.getTextFromPage(reader, i, new ScqLocationTextExtractionStrategy());
			result.append(page + "@@@@@@@@@@");

		}
		return result.toString();
	}

    public List<ContractComplex> extract() throws IOException, URISyntaxException {
        return new ArrayList<ContractComplex>();
    }
}