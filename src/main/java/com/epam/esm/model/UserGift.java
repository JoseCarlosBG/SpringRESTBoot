package com.epam.esm.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usergift")
@IdClass(UserGiftID.class)
public class UserGift {
    @Id
    @Column(name = "id_user")
    private Integer idUser;

    @Id
    @Column(name = "id_gift")
    private Integer idGift;

    @Column(name = "name")
    private String name;
    @Column(name = "cost")
    private Double cost;
    @Column(name = "create_date")
    private LocalDateTime createDate;

    public UserGift(Integer idUser, Integer idGift, String name, Double cost, LocalDateTime createDate) {
        this.idUser = idUser;
        this.idGift = idGift;
        this.name=name;
        this.cost = cost;
        this.createDate=createDate;
    }

    public UserGift() {
    }

    public UserGift(UserGift ug) {
        this.idUser = ug.getIdUser();
        this.idGift = ug.getIdGift();
        this.name=ug.getName();
        this.cost = ug.getCost();
        this.createDate=ug.getCreateDate();
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
    public String getName(){
        return name;
    }

    public void setName (String createDate) {
        this.name = name;
    }
    public Double getCost(){
        return cost;
    }

    public void setCost (Double cost) {
        this.cost = cost;
    }
    public LocalDateTime getCreateDate(){
        return createDate;
    }

    public void setCreateDate (LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
