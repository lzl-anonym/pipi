package com.anonym.module.wechat.miniprogram;

import com.anonym.module.wechat.WeChatConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

/**
 * 微信退款 rest template 配置
 */
@Configuration
public class WeChatRefundRestTemplateConfig {

    private static final String CERT_PATH = "cert/clark.p12";

    @Bean("RestTemplate4WeChatRefund")
    RestTemplate restTemplateWeChatRefund() throws Exception {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(5000);
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        CloseableHttpClient httpClient = initSSLConfig();
        factory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(factory);
        // 解决中文乱码问题
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    public CloseableHttpClient initSSLConfig() throws Exception {
        // 加载证书
        char[] pwd = WeChatConfig.PAYMENT_MERCHANT_ID.toCharArray();
        InputStream keyStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CERT_PATH);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(keyStream, pwd);
        SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, pwd).build();
        CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();
        return httpClient;
    }
}
