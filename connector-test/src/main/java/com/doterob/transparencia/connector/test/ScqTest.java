package com.doterob.transparencia.connector.test;

import com.doterob.transparencia.connector.company.CompanyService;
import com.doterob.transparencia.connector.contract.extractor.pdf.PDFManager;
import com.doterob.transparencia.connector.contract.extractor.pdf.pdfbox.PDFBoxManager;
import com.doterob.transparencia.connector.contract.extractor.xunta.XuntaConnector;
import com.doterob.transparencia.connector.contract.extractor.xunta.XuntaConnectorExecutor;
import com.doterob.transparencia.model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ScqTest {

	private static final String INDEX = "contratacion";
	//private static final Organization ORGANIZATION = new Organization("Santiago de Compostela", OrganizationType.local);
	private static final String URL = "http://www.santiagodecompostela.gal/medi/transparencia/Contratacion_municipal/Contratos_menores/Relacion_de_contratos_menores_2016/C-2016-";

	public static void main(String[] args) throws IOException, URISyntaxException, ParseException {

		final Date start = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2016");
		final Date end = new SimpleDateFormat("dd-MM-yyyy").parse("31-01-2016");

		final XuntaConnector xc = new XuntaConnectorExecutor();
		final CompanyService cs = new CompanyService();

		final List<ContractComplex> contracts = xc.extract(start, end);

		final List<String> nifs = new ArrayList<String>();
		for (ContractComplex c : contracts){
			nifs.add(c.getNif());
		}
		final Map<String, Company> companies = cs.getInfo(nifs);

		final List<Publishing> p = new ArrayList<>();
		for (ContractComplex c : contracts){
			p.add(new Publishing.Builder().setContract(c.getContract()).setOrganization(c.getOrganization()).setCompany(companies.containsKey(c.getNif()) ? companies.get(c.getNif()) : new Company(c.getNif(), c.getCompanyName(), null)).build());
		}


	}
}
