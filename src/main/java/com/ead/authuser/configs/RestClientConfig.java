package com.ead.authuser.configs;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * Configuration class for RestClient with custom timeout settings.
 * Uses modern Spring Boot 3.5+ approach without deprecated APIs.
 */
@Configuration
public class RestClientConfig {

    private static final int TIMEOUT = 5000;

    /**
     * Creates a load-balanced RestClient.Builder bean with custom timeout settings.
     * The @LoadBalanced annotation enables client-side load balancing through Eureka.
     *
     * @return configured RestClient.Builder
     */
    @LoadBalanced
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .requestFactory(customRequestFactory());
    }

    private ClientHttpRequestFactory customRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(TIMEOUT));
        factory.setReadTimeout(Duration.ofMillis(TIMEOUT));
        return factory;
    }
}
