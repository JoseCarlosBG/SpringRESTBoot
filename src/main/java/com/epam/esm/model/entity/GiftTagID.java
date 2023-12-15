package com.epam.esm.model.entity;

import java.io.Serializable;
import java.util.Objects;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GiftTagID implements Serializable {
    private @Getter @Setter Integer idGift;
    private @Getter @Setter Integer idTag;

    public GiftTagID(GiftTagID gtID) {
        this.idGift = gtID.getIdGift();
        this.idTag = gtID.getIdTag();
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
