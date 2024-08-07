package io.github.fraolme.services.ordering.config.auth;

import io.github.fraolme.services.ordering.config.AppProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    private static final String OAUTH_SCHEME_NAME = "oAuth";
    private static final String AUTH_URL_FORMAT = "%s/realms/%s/protocol/openid-connect/auth";

    @Bean
    OpenAPI customOpenApi(AppProperties properties) {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme(properties)))
                .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME));
    }

    private SecurityScheme createOAuthScheme(AppProperties properties) {
        OAuthFlows flows = createOAuthFlows(properties);

        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(flows);
    }

    private OAuthFlows createOAuthFlows(AppProperties properties) {
        OAuthFlow implicitFlow = createImplicitFlow(properties);

        return new OAuthFlows()
                .implicit(implicitFlow);
    }

    private OAuthFlow createImplicitFlow(AppProperties properties) {
        var authUrl = String.format(AUTH_URL_FORMAT, properties.getAuthServerUrl(), properties.getRealm());

        return new OAuthFlow()
                .authorizationUrl(authUrl)
                .scopes(new Scopes());
    }
}

