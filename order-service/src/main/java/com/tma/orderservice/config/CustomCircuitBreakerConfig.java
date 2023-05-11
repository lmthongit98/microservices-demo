package com.tma.orderservice.config;

import feign.Feign;
import feign.Target;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.openfeign.CircuitBreakerNameResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
public class CustomCircuitBreakerConfig {

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private TimeLimiterRegistry timeLimiterRegistry;

    @Bean
    public Resilience4JCircuitBreakerFactory resilience4JCircuitBreakerFactory() {
        Resilience4JCircuitBreakerFactory resilience4JCircuitBreakerFactory =
                new Resilience4JCircuitBreakerFactory(circuitBreakerRegistry, timeLimiterRegistry, null);
        resilience4JCircuitBreakerFactory.configureDefault(this::createResilience4JCircuitBreakerConfiguration);
        return resilience4JCircuitBreakerFactory;
    }

    private Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration createResilience4JCircuitBreakerConfiguration(String id) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(id);
        CircuitBreakerConfig circuitBreakerConfig = circuitBreaker.getCircuitBreakerConfig();
        TimeLimiterConfig timeLimiterConfig = timeLimiterRegistry.timeLimiter(id)
                .getTimeLimiterConfig();
        circuitBreaker.getEventPublisher()
                .onEvent(event -> System.out.println("Circuit-breaker Event Publisher : " + event));
        return new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(circuitBreakerConfig)
                .timeLimiterConfig(timeLimiterConfig)
                .build();
    }

    /**
     * Each Circuit Breaker that created will have it’s own id. For Feign Client, Circuit Breaker will build id from Feign Client name. Below is Feign Client name convention.
     *
     * {Feign-Class-Name}#{Method-Name}({Parameter-Types}{,})
     *
     * As you can see, that Feign Client name will have name that contains ‘#’, ‘(‘, ‘)’, and ‘,’ characters. Unfortunately, Spring Boot Configuration Properties cannot contains those characters. So the id will be invalid, and we cannot set specific Circuit Breaker config based on id.
     *
     * To handle that issue, I came up with solution to change the Circuit Breaker id. I will convert all of those characters to “_” character, and remove latest “_” character. So the id will be like below convention.
     * {Feign-Class-Name}_{Method-Name}_{Parameter-Types}{_}
     */

    @Bean
    public CircuitBreakerNameResolver circuitBreakerNameResolver() {
        return (String feignClientName, Target<?> target, Method method) ->
                Feign.configKey(target.type(), method)
                        .replaceAll("[#(,]+", "_")
                        .replaceAll("\\)+", Strings.EMPTY)
                        .replaceAll("_+$", Strings.EMPTY);
    }

}
