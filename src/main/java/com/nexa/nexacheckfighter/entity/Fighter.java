package com.nexa.nexacheckfighter.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Immutable;

@Entity
@Immutable
@Table(name = "fighter")
public class Fighter {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id = 0;
    @Column(name = "name", nullable = false, length = 100)
    private String name = "";
    @Column(name = "constitution", nullable = false)
    private Integer constitution = 0;
    @Column(name = "strength", nullable = false)
    private Integer strength = 0;
    @Column(name = "dexterity", nullable = false)
    private Integer dexterity = 0;
    @Column(name = "xp", nullable = false)
    private Integer xp = 0;
    @Column(name = "url", nullable = false, length = 255)
    private String url = "";
    @Column(name = "player", nullable = false, length = 100)
    private String player = "";

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getConstitution() {
        return this.constitution;
    }

    public void setConstitution(Integer constitution) {
        this.constitution = constitution;
    }

    public Integer getStrength() {
        return this.strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getDexterity() {
        return this.dexterity;
    }

    public void setDexterity(Integer dexterity) {
        this.dexterity = dexterity;
    }

    public Integer getXp() {
        return this.xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlayer() {
        return this.player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}
