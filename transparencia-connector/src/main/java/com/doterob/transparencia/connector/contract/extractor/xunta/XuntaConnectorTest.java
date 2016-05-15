package com.doterob.transparencia.connector.contract.extractor.xunta;

import org.junit.Test;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dotero on 14/05/2016.
 */
public class XuntaConnectorTest {
    @Test
    public void extract() throws Exception {

        Date start = new SimpleDateFormat("dd-MM-yyyy").parse("07-04-2016");
        Date end = new SimpleDateFormat("dd-MM-yyyy").parse("14-04-2016");

        XuntaConnectorPooling x = new XuntaConnectorPooling();
        Timestamp ini1 = new Timestamp(System.currentTimeMillis());
        System.out.println("INICIO1:" + ini1);
        System.out.println(x.extract(start, end));
        Timestamp fin1 = new Timestamp(System.currentTimeMillis());
        System.out.println("FIN1:" + fin1);

        XuntaConnectorExecutor x2 = new XuntaConnectorExecutor();
        Timestamp ini2 = new Timestamp(System.currentTimeMillis());
        System.out.println("INICIO2:" + ini2);
        System.out.println(x.extract(start, end));
        Timestamp fin2 = new Timestamp(System.currentTimeMillis());
        System.out.println("FIN2:" + fin2);

        System.out.println("TIEMPO 1 -> " + (fin1.getTime() - ini1.getTime()) + " @@@ TIEMPO 2 ->" + (fin2.getTime() - ini2.getTime()));
    }

}