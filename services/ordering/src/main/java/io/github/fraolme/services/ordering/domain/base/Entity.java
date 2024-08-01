package io.github.fraolme.services.ordering.domain.base;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;

@MappedSuperclass
public abstract class Entity {
    Integer requestedHashCode;

    @Id @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //TODO: Domain Events

    //TODO: Add Domain Event

    //TODO: Remove Domain Event

    //TODO: Clear Domain Event

    public boolean isTransient() {
        return this.id == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity entity)) return false;
        if(this.getClass() != o.getClass()) return false;
        if(entity.isTransient() || this.isTransient()) return false;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        if(!isTransient()) {
            if(requestedHashCode == null ) {
                requestedHashCode = this.id.hashCode() ^ 31;
            }
            return requestedHashCode;
        }
        return super.hashCode();
    }
}
