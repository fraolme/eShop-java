package io.github.fraolme.services.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@EnableConfigurationProperties
@Component
public class AppProperties {

    @Value("${spring.application.paymentsucceeded}")
    private boolean paymentSucceeded;

    public boolean getPaymentSucceeded() {
        return this.paymentSucceeded;
    }
}
