package ru.otus.crm.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "address")
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    @Override
    public Address clone(){
        return new Address(this.id, this.street);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
