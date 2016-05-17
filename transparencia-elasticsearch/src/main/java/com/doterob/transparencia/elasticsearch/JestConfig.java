package com.doterob.transparencia.elasticsearch;

import io.searchbox.client.config.HttpClientConfig;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by dotero on 13/05/2016.
 */
public class JestConfig {

    private static final Logger LOG = Logger.getLogger(JestConfig.class);
    private static volatile JestConfig instance;

    private static final String FILE_NAME = "aws-es.properties";
    private static final String ENDPOINT = "endpoint";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    private Properties properties;

    private JestConfig(){
        try {
            properties = new Properties();
            properties.load(Client.class.getClassLoader().getResourceAsStream(FILE_NAME));
        } catch (IOException e){
            LOG.error("No se cargaron las propiedades",e);
        }
    }

    public static JestConfig getInstance(){
        if(instance == null){
            synchronized (JestConfig.class){
                if(instance == null){
                    instance = new JestConfig();
                }
            }
        }
        return instance;
    }

    public HttpClientConfig getHttpClientConfig(){
        return new HttpClientConfig
                .Builder(properties.getProperty(ENDPOINT))
                .defaultCredentials(properties.getProperty(USER),properties.getProperty(PASSWORD))
                .multiThreaded(true)
                .build();
    }
}
