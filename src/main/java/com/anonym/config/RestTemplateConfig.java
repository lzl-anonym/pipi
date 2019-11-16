package com.anonym.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


@Configuration
public class RestTemplateConfig {

    @Value("${http.pool.max-total}")
    private Integer maxTotal;

    @Value("${http.pool.default-max-per-route}")
    private Integer defaultMaxPerRoute;

    @Value("${http.pool.socket-timeout}")
    private Integer socketTimeout;

    @Value("${http.pool.connect-timeout}")
    private Integer connectTimeout;

    @Value("${http.pool.connection-request-timeout}")
    private Integer connectionRequestTimeout;

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory, List<HttpMessageConverter<?>> converters) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setMessageConverters(converters);
        return restTemplate;
    }

    @Bean
    public List<HttpMessageConverter<?>> converters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        converters.add(converter);
        converters.add(fastConverter);
        return converters;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory httpClient() {
        try {
            TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
            CloseableHttpClient httpClient = httpClientBuilder.build();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setHttpClient(httpClient);
            factory.setConnectTimeout(10 * 1000);
            factory.setReadTimeout(30 * 1000);
            return factory;
        } catch (Exception e) {
            throw new RuntimeException("创建HttpsRestTemplate失败", e);
        }
    }

}
