package com.doterob.transparencia.connector.scq.pdfbox;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import com.doterob.transparencia.connector.scq.PDFManager;
import com.doterob.transparencia.model.Contract;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class PDFBoxManager implements PDFManager {

	private final PDDocument document;

	public PDFBoxManager(String url) throws IOException {

		final File file = File.createTempFile("aux",".pdf");
		FileUtils.copyURLToFile(new URL(url), file);

		final PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
		parser.parse();

		document = new PDDocument(parser.getDocument());
	}

    public String toText() throws IOException, URISyntaxException {

		return new PDFTextStripper().getText(document);
    }

    public List<Contract> extract() throws IOException, URISyntaxException {

		final Map<String, Contract> result = new TreeMap<String, Contract>();

		for(int phase : ScqPDFRowConstants.PHASES) {
			result.putAll(explorePhase(document, phase));
		}

		return  new ArrayList<Contract>(result.values());
	}

	private Map<String, Contract> explorePhase(final PDDocument document, int phase) throws IOException {

		final Map<String, Contract> result = new HashMap<String, Contract>();

		final PDFTextStripperByArea stripper = new PDFTextStripperByArea();
		stripper.setSortByPosition(true);

		final ScqPDFPage pageRegion = ScqPDFPage.get(phase);
		pageRegion.addRegions(stripper);

		for(PDPage page : document.getDocumentCatalog().getPages()){
			stripper.extractRegions(page);
			result.putAll(pageRegion.extractContracts(stripper));
		}

		return result;
	}

}