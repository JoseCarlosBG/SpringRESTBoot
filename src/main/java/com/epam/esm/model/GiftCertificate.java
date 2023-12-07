package com.epam.esm.model;

import org.springframework.hateoas.RepresentationModel;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Entity
@Table(name = "GiftCertificate")
public class GiftCertificate extends RepresentationModel<GiftCertificate>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Getter @Setter Integer id;
    @Column(name = "name")
    private @Getter @Setter String name;
    @Column(name = "description")
    private @Getter @Setter String description;
    @Column(name = "price")
    private @Getter @Setter Double price;
    @Column(name = "duration")
    private @Getter @Setter Integer duration;
    @Column(name = "create_date")
    private @Getter @Setter LocalDateTime createDate;
    @Column(name = "last_update_date")
    private @Getter @Setter LocalDateTime lastUpdateDate;

    public GiftCertificate(Integer id, String name, String description, Double price, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = LocalDateTime.now();
        this.lastUpdateDate = LocalDateTime.now();
    }

    public GiftCertificate() {
        this.createDate = LocalDateTime.now();
        this.lastUpdateDate = LocalDateTime.now();
    }

    public GiftCertificate (GiftCertificate gift){
        this.id=gift.getId();
        this.name=gift.getName();
        this.description=gift.getDescription();
        this.price = gift.getPrice();
        this.duration = gift.getDuration();
        this.createDate = gift.getCreateDate();
        this.lastUpdateDate = gift.getLastUpdateDate();
    }
}