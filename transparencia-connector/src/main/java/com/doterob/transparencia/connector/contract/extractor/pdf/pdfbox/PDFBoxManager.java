package com.doterob.transparencia.connector.contract.extractor.pdf.pdfbox;

import com.doterob.transparencia.connector.contract.extractor.local.scq.PDFContractAreaScqBuilder;
import com.doterob.transparencia.connector.contract.extractor.local.scq.ScqPDFRowConstants;
import com.doterob.transparencia.connector.contract.extractor.pdf.PDFManager;
import com.doterob.transparencia.model.ContractComplex;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class PDFBoxManager implements PDFManager {

	private final PDDocument document;

	public PDFBoxManager(String url) throws IOException {

		final File file = File.createTempFile("aux",".pdf");
		FileUtils.copyURLToFile(new URL(url), file);

		final PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
		parser.parse();

		document = new PDDocument(parser.getDocument());
	}

    public String testToText() throws IOException, URISyntaxException {

		return new PDFTextStripper().getText(document);
    }

    public List<ContractComplex> extract() throws IOException, URISyntaxException {

		final Map<String, ContractComplex> result = new TreeMap<String, ContractComplex>();

		for(int phase : ScqPDFRowConstants.PHASES) {
			result.putAll(explorePhase(document, phase));
		}

		return  new ArrayList<ContractComplex>(result.values());
	}

	private Map<String, ContractComplex> explorePhase(final PDDocument document, int phase) throws IOException {

		final Map<String, ContractComplex> result = new HashMap<String, ContractComplex>();

		final PDFTextStripperByArea stripper = new PDFTextStripperByArea();
		stripper.setSortByPosition(true);

		final PDFPage pageRegion = PDFContractAreaScqBuilder.get(phase);
		pageRegion.addRegions(stripper);

		for(PDPage page : document.getDocumentCatalog().getPages()){
			stripper.extractRegions(page);
			result.putAll(pageRegion.extractContracts(stripper));
		}

		return result;
	}

}