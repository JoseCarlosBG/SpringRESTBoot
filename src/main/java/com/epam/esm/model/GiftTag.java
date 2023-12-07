package com.epam.esm.model;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "GiftTag")
@IdClass(GiftTagID.class)
public class GiftTag {
    @Id
    @Column(name = "id_gift")
    private @Getter @Setter Integer idGift;
    @Id
    @Column(name = "id_tag")
    private @Getter @Setter Integer idTag;

    public GiftTag(GiftTag gt) {
        this.idGift = gt.getIdGift();
        this.idTag = gt.getIdTag();
    }
}