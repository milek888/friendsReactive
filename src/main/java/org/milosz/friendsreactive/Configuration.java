package org.milosz.friendsreactive;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@org.springframework.context.annotation.Configuration
public class Configuration {
//https://stackoverflow.com/questions/59729163/spring-webclient-and-eureka-server
//https://piotrminkowski.com/2018/05/04/reactive-microservices-with-spring-webflux-and-spring-cloud/
//https://cloud.spring.io/spring-cloud-static/spring-cloud-commons/2.1.6.RELEASE/multi/multi__spring_cloud_commons_common_abstractions.html

    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClient(){
        return WebClient.builder().baseUrl("http://localhost:8083");
    }
}
