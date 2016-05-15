package com.doterob.transparencia.connector.contract.extractor.xunta;

import com.doterob.transparencia.model.Contract;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by dotero on 14/05/2016.
 */
public class XuntaConnector {

    private static final Logger LOG = LogManager.getLogger(XuntaConnector.class);

    private static final String SESSION_ENDPOINT = "http://www.contratosdegalicia.es/";
    private static final String FORM_ENDPOINT = "http://www.contratosdegalicia.es/resultadoFormalizados.jsp";
    private static final String CONTRACT_ENDPOINT = "http://www.contratosdegalicia.es/licitacion?N=";
    private static final int CLIENTS = 2;

    private List<CloseableHttpClient> initClients(PoolingHttpClientConnectionManager manager, CookieStore cookies){

        final List<CloseableHttpClient> result = new ArrayList<>();
        for(int i = 0; i < CLIENTS; i++){
            result.add(HttpClientBuilder.create().setDefaultCookieStore(cookies).build());
        }

        return result;
    }

    private void closeClients(List<CloseableHttpClient> clients) throws IOException{
        for (CloseableHttpClient client : clients){
            client.close();
        }
    }

    public List<Contract> extract() {

        final List<Contract> result = new ArrayList<>();

        final CookieStore cookies = new BasicCookieStore();
        final PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(CLIENTS);
        final List<CloseableHttpClient> clients = initClients(manager, cookies);

        try {

            startSession(clients.get(0));

            final List<FindThread> threads = new ArrayList<FindThread>();
            final List<String> codes = search(clients.get(0));
            for(final String code : codes){
                final FindThread thread = new FindThread(clients.get(codes.indexOf(code) % CLIENTS),code);
                threads.add(thread);
            }

            for (FindThread thread : threads){
                thread.start();
            }

            for (FindThread thread : threads){
                thread.join();
            }

            for (FindThread thread : threads){
                result.add(thread.getResult());
            }

            return result;

        } catch (IOException | InterruptedException e){
            LOG.error(e);
            System.out.println(e);
        } finally {

            try {
                closeClients(clients);
                manager.close();
            } catch (IOException e){
                    LOG.error(e);
                    System.out.println(e);
                }
        }

        return null;
    }

    private void startSession(HttpClient client) throws IOException{

        final HttpGet request = new HttpGet(SESSION_ENDPOINT);
        final HttpResponse response = client.execute(request);
    }

    private List<String> search(HttpClient client) throws  IOException{

        final HttpPost request = new HttpPost(FORM_ENDPOINT);

        List<NameValuePair> values = new ArrayList<NameValuePair>();
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
        values.add(new BasicNameValuePair("diaDende", "7"));
        values.add(new BasicNameValuePair("mesDende", "4"));
        values.add(new BasicNameValuePair("anoDende", "2016"));
        values.add(new BasicNameValuePair("diaAta", "14"));
        values.add(new BasicNameValuePair("mesAta", "4"));
        values.add(new BasicNameValuePair("anoAta", "2016"));
        values.add(new BasicNameValuePair("tab", "3"));
        values.add(new BasicNameValuePair("ID", "709"));
        values.add(new BasicNameValuePair("lang", "gl"));
        values.add(new BasicNameValuePair("DI", ""));
        values.add(new BasicNameValuePair("DF", ""));
        values.add(new BasicNameValuePair("DIO", "07-05-2016"));
        values.add(new BasicNameValuePair("DFO", "14-05-2016"));
        values.add(new BasicNameValuePair("II", ""));
        values.add(new BasicNameValuePair("IF", ""));

        request.setEntity(new UrlEncodedFormEntity(values, Consts.UTF_8));

        final HttpResponse response = client.execute(request);
        final String html = StringEscapeUtils.unescapeHtml4(EntityUtils.toString(response.getEntity()));

        final Document doc = Jsoup.parse(html);
        return Arrays.asList(doc.select("td.c115").select("span").text().split(" "));
    }

    private static class FindThread extends Thread {

        private final CloseableHttpClient client;
        private final HttpContext context;
        private final String code;
        private Contract result;

        public FindThread(CloseableHttpClient client, String code) {
            this.client = client;
            this.context = HttpClientContext.create();
            this.code = code;
        }

        @Override
        public void run() {
            try {

                final HttpGet request = new HttpGet(CONTRACT_ENDPOINT + code);
                final CloseableHttpResponse response = client.execute(request, context);

                try {

                    System.out.println("INICIO[" + code + "] " + new Timestamp(System.currentTimeMillis()));

                    final HttpEntity entity = response.getEntity();
                    final String html = StringEscapeUtils.unescapeHtml4(EntityUtils.toString(entity));
                    final Document doc = Jsoup.parse(html);

                    final String _date = doc.getElementById("tabs-4").select("td.c65").get(1).text().trim();
                    final String subject = doc.getElementById("tabs-1").select("td.c468").get(1).text().trim();
                    final String contractorName = doc.getElementById("tabs-3").select("td.c464").get(1).text().trim();
                    final String contractorId = doc.getElementById("tabs-4").select("td.c242").get(1).text().trim().replace(contractorName, "").trim();
                    final String organization = "Xunta de Galicia";
                    final String area = doc.select("div.titulo").text().trim();
                    final String _amount = doc.getElementById("tabs-4").select("td.celdaDerecha.c148").text().replace("€","").replace(".","").trim();

                    final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = null;
                    Float amount = null;
                    try {
                        date = formatter.parse(_date);
                        amount = Float.parseFloat(_amount);
                    } catch (ParseException |NumberFormatException e){}

                    result = new Contract(code, date, subject, contractorId, contractorName, area, amount);

                    System.out.println("FIN[" + code + "] " + new Timestamp(System.currentTimeMillis()));

                } finally {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error(e);
            }
        }

        public Contract getResult(){
            return result;
        }

    }
}
