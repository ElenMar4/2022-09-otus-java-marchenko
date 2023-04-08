package ru.otus.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@Setter
@Table("address")
public class Address {

    @Id
    @Column("id")
    private Long id;

    @Column("street")
    private String street;

    @Column("client_id")
    private Long clientId;

    @PersistenceCreator
    public Address(Long id, String street, Long clientId) {
        this.id = id;
        this.street = street;
        this.clientId = clientId;
    }

    public Address(String street, Long clientId) {
        this(null, street, clientId);
    }
}
