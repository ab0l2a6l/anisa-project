package org.example;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.ReactorResourceFactory;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableFeignClients(basePackages = {"org.example"})
public class ProductMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductMicroserviceApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestClient restClient() {

        return RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    @Bean
    public ReactorResourceFactory resourceFactory() {
        ReactorResourceFactory factory = new ReactorResourceFactory();
        factory.setUseGlobalResources(false);
        return factory;
    }
//    @Bean
//    @LoadBalanced
//    public WebClient webClient() {
//        return WebClient.builder().build();
//    }

    @Bean
    @LoadBalanced
    public WebClient.Builder lbWebClient() {
        return WebClient.builder();
    }

}
