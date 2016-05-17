package com.doterob.transparencia.connector.test;

import com.doterob.transparencia.connector.company.CompanyService;
import com.doterob.transparencia.connector.contract.extractor.xunta.XuntaConnector;
import com.doterob.transparencia.connector.contract.extractor.xunta.XuntaConnectorExecutor;
import com.doterob.transparencia.connector.geocoding.GoogleGeocodingService;
import com.doterob.transparencia.elasticsearch.JestClient;
import com.doterob.transparencia.model.Company;
import com.doterob.transparencia.model.ContractComplex;
import com.doterob.transparencia.model.Publishing;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ScqTest {

	private static final Logger LOG = LogManager.getLogger(ScqTest.class);
	private static final String INDEX = "contratacion";
	//private static final Organization ORGANIZATION = new Organization("Santiago de Compostela", OrganizationType.local);
	private static final String URL = "http://www.santiagodecompostela.gal/medi/transparencia/Contratacion_municipal/Contratos_menores/Relacion_de_contratos_menores_2016/C-2016-";

	public static void main(String[] args) throws IOException, URISyntaxException, ParseException {

		final Date start = new SimpleDateFormat("dd-MM-yyyy").parse("01-04-2016");
		final Date end = new SimpleDateFormat("dd-MM-yyyy").parse("30-04-2016");

		final XuntaConnector xc = new XuntaConnectorExecutor();
		final CompanyService cs = new CompanyService();

		LOG.debug("INICIO");
		cs.find("B15970692");
		final List<ContractComplex> contracts = xc.extract(start, end);

		final List<String> nifs = new ArrayList<String>();
		for (ContractComplex c : contracts){
			nifs.add(c.getNif());

		}
		final Map<String, Company> companies = cs.findAll(nifs);
		final ObjectMapper m = new ObjectMapper();
		final List<Publishing> ps = new ArrayList<>();
		for (ContractComplex c : contracts){
			final Publishing p = new Publishing.Builder().setContract(c.getContract()).setOrganization(c.getOrganization()).setCompany(companies.containsKey(c.getNif()) ? companies.get(c.getNif()) : new Company(c.getNif(), c.getCompanyName(), null)).build();
			ps.add(p);
			LOG.debug(m.writeValueAsString(p));
		}

		xc.close();
		cs.close();
		GoogleGeocodingService.getInstance().close();

		final JestClient es = new JestClient();
		es.indexContracts("transparencia", "contrato", ps);

		LOG.debug("FIN");
	}
}
