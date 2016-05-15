package com.doterob.transparencia.connector.contract.extractor.xunta;

import com.doterob.transparencia.model.Contract;

import java.util.Date;
import java.util.List;

/**
 * Created by dotero on 15/05/2016.
 */
public interface XuntaConnector {

    int MAX_CONNECTIONS = 10;

    List<Contract> extract(Date start, Date end);
}
