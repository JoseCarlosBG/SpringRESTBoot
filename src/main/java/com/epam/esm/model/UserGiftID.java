package com.epam.esm.model;

import java.io.Serializable;
import java.util.Objects;

public class UserGiftID implements Serializable {
    private Integer idUser;
    private Integer idGift;
    public UserGiftID(Integer idUser, Integer idGift) {
        this.idUser = idUser;
        this.idGift = idGift;
    }
    public UserGiftID() {
    }

    public UserGiftID(UserGiftID ugID) {
        this.idUser = ugID.getIdUser();
        this.idGift = ugID.getIdGift();
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer id) {
        idUser = id;
    }
    public Integer getIdGift() {
        return idGift;
    }

    public void setIdGift(Integer id) {
        idGift = id;
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
