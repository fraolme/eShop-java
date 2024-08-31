package io.github.fraolme.services.ordering.domain.base;

import an.awesome.pipelinr.Notification;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

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

    public Entity() {
        this.domainEvents = new ArrayList<>();
    }

    @DomainEvents
    public List<Notification> getDomainEvents() {
        var list = new ArrayList<>(this.domainEvents);
        // TODO: find a better way to handle the infinite loop created by domain events
        this.domainEvents.clear();
        return list;
    }

    public void addDomainEvent(Notification event) {
        this.domainEvents.add(event);
    }

    public  void removeDomainEvent(Notification event) {
        this.domainEvents.remove(event);
    }

    private boolean isTransient() {
        return this.id == null;
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
