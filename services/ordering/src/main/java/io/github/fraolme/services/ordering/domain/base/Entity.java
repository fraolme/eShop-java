package io.github.fraolme.services.ordering.domain.base;

import an.awesome.pipelinr.Notification;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    @Transient
    List<Notification> domainEvents;

    public List<Notification> getDomainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }

    public void addDomainEvent(Notification event) {
        if(this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public  void removeDomainEvent(Notification event) {
        if(this.domainEvents != null) {
            this.domainEvents.remove(event);
        }
    }

    public void clearDomainEvents(Notification event) {
        if(this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    private boolean isTransient() {
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
