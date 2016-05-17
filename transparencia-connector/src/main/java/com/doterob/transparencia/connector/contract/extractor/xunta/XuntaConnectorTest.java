package com.doterob.transparencia.connector.contract.extractor.xunta;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dotero on 14/05/2016.
 */
public class XuntaConnectorTest {

    private static final Logger LOG = LogManager.getLogger(XuntaConnectorTest.class);
    
    @Test
    public void extract() throws Exception {

        Date start = new SimpleDateFormat("dd-MM-yyyy").parse("07-04-2016");
        Date end = new SimpleDateFormat("dd-MM-yyyy").parse("14-04-2016");

        XuntaConnectorPooling x = new XuntaConnectorPooling();
        Timestamp ini1 = new Timestamp(System.currentTimeMillis());
        LOG.debug("INICIO1:" + ini1);
        LOG.debug(x.extract(start, end));
        Timestamp fin1 = new Timestamp(System.currentTimeMillis());
        LOG.debug("FIN1:" + fin1);

        XuntaConnectorExecutor x2 = new XuntaConnectorExecutor();
        Timestamp ini2 = new Timestamp(System.currentTimeMillis());
        LOG.debug("INICIO2:" + ini2);
        LOG.debug(x.extract(start, end));
        Timestamp fin2 = new Timestamp(System.currentTimeMillis());
        LOG.debug("FIN2:" + fin2);

        LOG.debug("TIEMPO 1 -> " + (fin1.getTime() - ini1.getTime()) + " @@@ TIEMPO 2 ->" + (fin2.getTime() - ini2.getTime()));
    }

}