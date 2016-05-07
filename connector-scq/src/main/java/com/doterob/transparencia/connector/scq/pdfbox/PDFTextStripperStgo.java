package com.doterob.transparencia.connector.scq.pdfbox;

import java.io.IOException;
import java.util.List;

import com.doterob.transparencia.connector.scq.RectangleStgo;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class PDFTextStripperStgo extends PDFTextStripper {

	int i = 0;

	public PDFTextStripperStgo() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void writePage() throws IOException {
		super.writePage();
		final List<List<TextPosition>> pageText = getCharactersByArticle();
		final RectangleStgo r = RectangleStgo.getInstance();
		System.out.println("writePage");
		for (final List<TextPosition> l : pageText) {
			for (final TextPosition t : l) {
				System.out.println("Matriz:" + t.getTextMatrix());
				r.add(Math.round(t.getX()), Math.round(t.getY()), Math.round(t.getWidthDirAdj()),
						Math.round(t.getHeight()));
				// System.out.println("@@@@@->(" + t.getX() + "," + t.getY() + ")-");
			}
		}
	}

	// @Override
	// public String getText(PDDocument doc) throws IOException {
	// // TODO Auto-generated method stub
	//
	// final List<List<TextPosition>> pageText = getCharactersByArticle();
	// System.out.println("writePage");
	// for (final List<TextPosition> l : pageText) {
	// for (final TextPosition t : l) {
	// System.out.println("@@@->" + t.getX());
	// }
	// }
	//
	// return super.getText(doc);
	//
	// }

}
