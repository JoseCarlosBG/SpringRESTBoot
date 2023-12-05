package com.epam.esm.model;

import java.io.Serializable;
import java.util.Objects;

public class GiftTagID implements Serializable {
    private Integer idGift;
    private Integer idTag;

    public GiftTagID(Integer idGift, Integer idTag) {
        this.idGift = idGift;
        this.idTag = idTag;
    }

    public GiftTagID() {
    }

    public GiftTagID(GiftTagID gtID) {
        this.idGift = gtID.getIdGift();
        this.idTag = gtID.getIdTag();
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

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        GiftTagID gt = (GiftTagID) o;
        return Objects.equals( idGift, gt.idGift ) &&
                Objects.equals( idTag, gt.idTag );
    }

    @Override
    public int hashCode() {
        return Objects.hash( idGift, idTag );
    }
}
