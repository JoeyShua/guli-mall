package com.jxs.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfiguration {

    @Value("${es.host}")
    private String esHost;

    @Value("${es.port}")
    private int esPort;


    @Bean
    public RestHighLevelClient esRestClient() {

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(esHost, esPort, "http")));
        return client;
    }

}
