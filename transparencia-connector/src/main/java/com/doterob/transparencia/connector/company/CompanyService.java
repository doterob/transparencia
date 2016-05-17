package com.doterob.transparencia.connector.company;

import com.doterob.transparencia.connector.HttpService;
import com.doterob.transparencia.connector.geocoding.GoogleGeocodingService;
import com.doterob.transparencia.model.Company;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by dotero on 14/05/2016.
 */
public class CompanyService extends HttpService {

    private static final Logger LOG = LogManager.getLogger(CompanyService.class);

    private static final String API_ENDPOINT = "http://www.infocif.es/general/empresas-informacion-listado-empresas.asp?Buscar=";

    public CompanyService(){
        super();
    }

    public Company find(String nif) {

        Company result = request(InfocifRequestBuilder.find(nif), new InfocifResponseHandler());
        if(result == null){
            LOG.warn("Empresa no encontrada en infocif -> " + nif);
            Company aux = request(AxesorRequestBuilder.find(nif), new AxesorResponseHandler());
            if(aux != null){
                result = new Company(nif, aux.getName(), aux.getAddress());
            } else {
                LOG.warn("Empresa no encontrada en axesor -> " + nif);
            }
        }

        return result;
    }


    public Map<String, Company> findAll(List<String> nifs) {
        final Map<String, Company> result = requestAll(InfocifRequestBuilder.findAll(nifs), new InfocifResponseHandler());
        final List<String> notFound = new ArrayList<>();
        for (String code : result.keySet()){
            if(result.get(code) == null){
                LOG.warn("Empresa no encontrada en infocif -> " + code);
                notFound.add(code);
            }
        }
        for(Map.Entry<String, Company> entry : requestAll(AxesorRequestBuilder.findAll(notFound), new AxesorResponseHandler()).entrySet()) {
            if(entry.getValue() == null){
                LOG.warn("Empresa no encontrada en axesor -> " + entry.getKey());
            }
            result.put(entry.getKey(), parseAxesor(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    private Company parseAxesor(String nif, Company company){
        if(company != null){
            return new Company(nif, company.getName(), company.getAddress());
        }
        return null;
    }
}
