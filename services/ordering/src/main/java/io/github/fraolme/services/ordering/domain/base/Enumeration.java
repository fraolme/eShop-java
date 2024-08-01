package io.github.fraolme.services.ordering.domain.base;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

@MappedSuperclass
public abstract class Enumeration {

    @Id
    private Long id;

    @Column(length = 200, nullable = false)
    private String name;

    public Enumeration() {}

    public Enumeration(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enumeration that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static <T extends Enumeration> T fromId(Long id, Class<T> classRef) {
        return Arrays.stream(classRef.getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers()))
                .map(field -> {
                    try {
                        return (T) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).filter(x -> x.getId().equals(id)).findFirst().get();
    }
}
