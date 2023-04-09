package com.tma.orderservice.client;

import com.tma.common.dto.inventory.InventoryResponse;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "inventory-service", configuration = InventoryFeignClientConfiguration.class)
public interface InventoryServiceClient {

    @GetMapping("/api/inventory/{sku-code}")
    List<InventoryResponse> isInStock(@PathVariable("sku-code") List<String> skuCodes);

}


@SuppressWarnings("deprecation")
class InventoryFeignClientConfiguration {

    @Value("${server.oauth2.token.url}")
    private String tokenURL;

    @Value("${client.notification.client.id}")
    private String clientId;

    @Value("${client.notification.client.secret}")
    private String clientSecret;

    @Value("#{'${client.notification.client.scopes}'.split(',')}")
    private List<String> scopes;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), resource());
    }

    private OAuth2ProtectedResourceDetails resource() {
        final ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setAccessTokenUri(tokenURL);
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setScope(scopes);
        return details;
    }

}
