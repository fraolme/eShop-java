package io.github.fraolme.services.ordering.api.domain.base;

import java.util.Objects;
import java.util.List;

public abstract class ValueObject {

    protected abstract List<Object> getEqualityComponents();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValueObject valObj)) return false;
        if(this.getClass() != o.getClass()) return false;

        return this.getEqualityComponents().equals(valObj.getEqualityComponents());
    }

    @Override
    public int hashCode() {
        return this.getEqualityComponents().stream()
                .map(x -> x != null ? Objects.hash(x) : 0)
                .reduce((x, y) -> x ^ y).orElse(super.hashCode());
    }
}
