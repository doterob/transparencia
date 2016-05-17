package com.doterob.transparencia.connector.contract.extractor.xunta;

import com.doterob.transparencia.connector.HttpService;
import com.doterob.transparencia.model.ContractComplex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dotero on 14/05/2016.
 */
public class XuntaConnectorExecutor extends HttpService implements XuntaConnector {

    private static final Logger LOG = LogManager.getLogger(XuntaConnectorExecutor.class);

    @Override
    public List<ContractComplex> extract(Date start, Date end) {

        final List<ContractComplex> result = new ArrayList<>();

        try {

            client.execute(XuntaRequestBuilder.session());

            final List<String> codes = new ArrayList<>();
            XuntaContractPage page = client.execute(XuntaRequestBuilder.search(start,end), new XuntaContractPageResponseHandler());
            do {
                codes.addAll(page.getCodes());
                page = page.getNext() != null ? client.execute(XuntaRequestBuilder.next(page.getNext()), new XuntaContractPageResponseHandler()) : page;
            } while (page.getNext() != null);
            return get(codes);

        } catch (IOException e){
            LOG.error("Error obteniendo contratos", e);
        }

        return result;
    }

    public List<ContractComplex> get(final List<String> codes){

        final List<ContractComplex> result = new ArrayList<>();
        for(Map.Entry<String, List<ContractComplex>> entry : requestAll(XuntaRequestBuilder.findAll(codes), new ContractResponseHandler()).entrySet()){
            result.addAll(entry.getValue());
        }
        return result;
    }
}
