package io.github.fraolme.services.ordering.domain.exceptions;

public class OrderingDomainException extends Exception {

    public OrderingDomainException() {
    }

    public OrderingDomainException(String message) {
        super(message);
    }

    public OrderingDomainException(String message, Exception innerException) {
        super(message, innerException);
    }

}
