package com.epam.esm.model;

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
public class UserGiftID implements Serializable {
    private @Getter @Setter Integer idUser;
    private @Getter @Setter Integer idGift;

    public UserGiftID(UserGiftID ugID) {
        this.idUser = ugID.getIdUser();
        this.idGift = ugID.getIdGift();
    }
    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        UserGiftID ug = (UserGiftID) o;
        return Objects.equals( idUser, ug.idUser ) &&
                Objects.equals( idGift, ug.idGift );
    }

    @Override
    public int hashCode() {
        return Objects.hash( idUser, idGift );
    }
}
