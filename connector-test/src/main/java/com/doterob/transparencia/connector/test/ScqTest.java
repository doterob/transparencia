package com.doterob.transparencia.connector.test;

import com.doterob.transparencia.connector.scq.PDFManager;
import com.doterob.transparencia.connector.scq.pdfbox.PDFBoxManager;
import com.doterob.transparencia.elasticsearch.Client;
import com.doterob.transparencia.elasticsearch.ESClient;
import com.doterob.transparencia.elasticsearch.JestClient;
import com.doterob.transparencia.model.Contract;
import com.doterob.transparencia.model.Organization;
import com.doterob.transparencia.model.OrganizationType;
import com.doterob.transparencia.model.Publishing;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class ScqTest {

	private static final String INDEX = "contratacion";
	private static final Organization ORGANIZATION = new Organization("Santiago de Compostela", OrganizationType.local);
	private static final String URL = "http://www.santiagodecompostela.gal/medi/transparencia/Contratacion_municipal/Contratos_menores/Relacion_de_contratos_menores_2016/C-2016-1.pdf";

	public static void main(String[] args) throws IOException, URISyntaxException {

		final PDFManager pdfManager = new PDFBoxManager(URL);

		final List<Publishing> publishing = new Publishing.Builder()
				.setOrganization(ORGANIZATION)
				.setSource(URL)
				.build(pdfManager.extract());

		assert publishing.size() > 1;

		final Client es = new JestClient();
		es.indexContracts(INDEX, publishing);
	}
}
