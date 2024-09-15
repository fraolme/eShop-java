package io.github.fraolme.services.ordering.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@EnableConfigurationProperties
@Component
public class AppProperties {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${spring.application.graceperiodtime}")
    private int gracePeriodTime;

    public String getRealm() {
        return this.realm;
    }

    public  String getAuthServerUrl() {
        return this.authServerUrl;
    }

    // grace period in minutes
    public int getGracePeriodTime() {
        return this.gracePeriodTime;
    }
}
