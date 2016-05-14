package com.doterob.transparencia.connector.contract.extractor.xunta;

import org.junit.Test;


import java.sql.Timestamp;

import static org.junit.Assert.*;

/**
 * Created by dotero on 14/05/2016.
 */
public class XuntaConnectorTest {
    @Test
    public void extract() throws Exception {

        XuntaConnector x = new XuntaConnector();
        System.out.println("INICIO:" + new Timestamp(System.currentTimeMillis()));
        System.out.println(x.extract());
        System.out.println("FIN:" + new Timestamp(System.currentTimeMillis()));
    }

}