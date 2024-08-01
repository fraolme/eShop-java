package io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate;

import io.github.fraolme.services.ordering.domain.base.ValueObject;
import jakarta.persistence.Embeddable;

import java.util.List;
import java.util.Objects;

@Embeddable
public class Address extends ValueObject {
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    public Address() {}

    public Address(String street, String city, String state, String country, String zipCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getState() {
        return state;
    }

    @Override
    protected List<Object> getEqualityComponents() {
        return List.of(this.street, this.city, this.state, this.country, this.zipCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        return Objects.equals(street, address.street) && Objects.equals(city, address.city) && Objects.equals(state, address.state) && Objects.equals(country, address.country) && Objects.equals(zipCode, address.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, country, zipCode);
    }
}
