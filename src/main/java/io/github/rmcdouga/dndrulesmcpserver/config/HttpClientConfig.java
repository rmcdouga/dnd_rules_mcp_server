package io.github.rmcdouga.dndrulesmcpserver.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * Defines shared HTTP client infrastructure for all outbound D&D Beyond requests.
 *
 * <p>Configures a {@link RestClient} with timeout support and a base URL
 * derived from {@link DdbProperties}. Later phases can add interceptors
 * for auth/session propagation here.</p>
 */
@Configuration
public class HttpClientConfig {

    @Bean
    public RestClient ddbRestClient(DdbProperties properties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(properties.requestTimeoutSeconds()));
        factory.setReadTimeout(Duration.ofSeconds(properties.requestTimeoutSeconds()));

        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .requestFactory(factory)
                .build();
     }
}
