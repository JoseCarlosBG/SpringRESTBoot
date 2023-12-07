package com.epam.esm.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "usergift")
@IdClass(UserGiftID.class)
public class UserGift {
    @Id
    @Column(name = "id_user")
    private @Getter @Setter Integer idUser;
    @Id
    @Column(name = "id_gift")
    private @Getter @Setter Integer idGift;
    @Column(name = "name")
    private @Getter @Setter String name;
    @Column(name = "cost")
    private @Getter @Setter Double cost;
    @Column(name = "create_date")
    private @Getter @Setter LocalDateTime createDate;

    public UserGift(UserGift ug) {
        this.idUser = ug.getIdUser();
        this.idGift = ug.getIdGift();
        this.name=ug.getName();
        this.cost = ug.getCost();
        this.createDate=ug.getCreateDate();
    }
}
