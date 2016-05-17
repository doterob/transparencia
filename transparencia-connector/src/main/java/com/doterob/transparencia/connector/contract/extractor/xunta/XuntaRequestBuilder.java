package com.doterob.transparencia.connector.contract.extractor.xunta;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dotero on 15/05/2016.
 */
public class XuntaRequestBuilder {

    private static final String SESSION_ENDPOINT = "http://www.contratosdegalicia.es/";
    private static final String FORM_ENDPOINT = "http://www.contratosdegalicia.es/resultadoFormalizados.jsp";
    private static final String CONTRACT_ENDPOINT = "http://www.contratosdegalicia.es/licitacion?N=";

    public static HttpGet session(){
        return new HttpGet(SESSION_ENDPOINT);
    }

    public static HttpGet find(String code){
        return new HttpGet(CONTRACT_ENDPOINT + code);
    }

    public static Map<String, HttpGet>  findAll(List<String> codes){
        final Map<String, HttpGet> result = new HashMap<>();
        for (String code : codes){
            result.put(code, find(code));
        }
        return result;
    }

    public static HttpGet next(String next){
        return new HttpGet(SESSION_ENDPOINT + next);
    }

    public static HttpPost search(Date start, Date end){

        final Calendar cStart = Calendar.getInstance();
        cStart.setTime(start);
        final Calendar cEnd = Calendar.getInstance();
        cEnd.setTime(end);

        final List<NameValuePair> values = new ArrayList<NameValuePair>();
        values.add(new BasicNameValuePair("OB", "Palabra ou palabras a buscar"));
        values.add(new BasicNameValuePair("OBDEF", "Palabra ou palabras a buscar"));
        values.add(new BasicNameValuePair("t1", "off"));
        values.add(new BasicNameValuePair("t2", "off"));
        values.add(new BasicNameValuePair("t3", "off"));
        values.add(new BasicNameValuePair("t4", "off"));
        values.add(new BasicNameValuePair("t5", "off"));
        values.add(new BasicNameValuePair("c11", "off"));
        values.add(new BasicNameValuePair("c13", "off"));
        values.add(new BasicNameValuePair("c17", "off"));
        values.add(new BasicNameValuePair("c15", "off"));
        values.add(new BasicNameValuePair("c19", "off"));
        values.add(new BasicNameValuePair("c16", "off"));
        values.add(new BasicNameValuePair("c14", "off"));
        values.add(new BasicNameValuePair("c18", "off"));
        values.add(new BasicNameValuePair("diaDende", "-1"));
        values.add(new BasicNameValuePair("mesDende", "-1"));
        values.add(new BasicNameValuePair("anoDende", "-1"));
        values.add(new BasicNameValuePair("diaAta", "-1"));
        values.add(new BasicNameValuePair("mesAta", "-1"));
        values.add(new BasicNameValuePair("anoAta", "-1"));
        values.add(new BasicNameValuePair("importeEntre", ""));
        values.add(new BasicNameValuePair("importeAta", ""));
        values.add(new BasicNameValuePair("organo", "-1"));
        values.add(new BasicNameValuePair("diaDende", String.valueOf(cStart.get(Calendar.DAY_OF_MONTH))));
        values.add(new BasicNameValuePair("mesDende", String.valueOf(cStart.get(Calendar.MONTH))));
        values.add(new BasicNameValuePair("anoDende", String.valueOf(cStart.get(Calendar.YEAR))));
        values.add(new BasicNameValuePair("diaAta", String.valueOf(cEnd.get(Calendar.DAY_OF_MONTH))));
        values.add(new BasicNameValuePair("mesAta", String.valueOf(cEnd.get(Calendar.MONTH))));
        values.add(new BasicNameValuePair("anoAta", String.valueOf(cEnd.get(Calendar.YEAR))));
        values.add(new BasicNameValuePair("tab", "3"));
        values.add(new BasicNameValuePair("ID", "709"));
        values.add(new BasicNameValuePair("lang", "gl"));
        values.add(new BasicNameValuePair("DI", ""));
        values.add(new BasicNameValuePair("DF", ""));
        values.add(new BasicNameValuePair("DIO", new SimpleDateFormat("dd-MM-yyyy").format(start)));
        values.add(new BasicNameValuePair("DFO", new SimpleDateFormat("dd-MM-yyyy").format(end)));
        values.add(new BasicNameValuePair("II", ""));
        values.add(new BasicNameValuePair("IF", ""));

        final HttpPost request = new HttpPost(FORM_ENDPOINT);
        request.setEntity(new UrlEncodedFormEntity(values, Consts.UTF_8));

        return request;
    }
}
