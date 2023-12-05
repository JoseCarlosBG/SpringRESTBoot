package com.epam.esm.model;
import jakarta.persistence.*;

@Entity
@Table(name = "GiftTag")
@IdClass(GiftTagID.class)
public class GiftTag {
    @Id
    @Column(name = "id_gift")
    private Integer idGift;

    @Id
    @Column(name = "id_tag")
    private Integer idTag;
    public GiftTag(Integer idGift, Integer idTag) {
        this.idGift = idGift;
        this.idTag = idTag;
    }

    public GiftTag() {
    }

    public GiftTag(GiftTag gt) {
        this.idGift = gt.getIdGift();
        this.idTag = gt.getIdTag();
    }
    public Integer getIdGift() {
        return idGift;
    }

    public void setIdGift(Integer id) {
        idGift = id;
    }

    public Integer getIdTag() {
        return idTag;
    }

    public void setIdTag(Integer id) {
        idTag = id;
    }
}