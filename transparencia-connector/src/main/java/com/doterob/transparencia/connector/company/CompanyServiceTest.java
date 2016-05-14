package com.doterob.transparencia.connector.company;

import com.doterob.transparencia.connector.geocoding.GoogleGeocodingService;
import com.doterob.transparencia.model.Address;
import com.doterob.transparencia.model.Company;
import org.junit.Assert;

/**
 * Created by dotero on 14/05/2016.
 */
public class CompanyServiceTest {
    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void getInfo() throws Exception {

        final Company expected = new Company("A33526369", "DESARROLLO DE ESTRATEGIAS EXTERIORES SA", "Plaza San Miguel 1 5º -izquierda.  33202  - (Gijon) - Asturias", null);
        final Company result = new CompanyService().getInfo("A33526369");

        Address point = new GoogleGeocodingService().getAddress(expected.getLocation());

        Assert.assertEquals(result, expected);
    }

}