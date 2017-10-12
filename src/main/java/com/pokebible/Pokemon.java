package com.pokebible;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
 
@Entity
public class Pokemon {
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
 
    private String name;
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
    
    private String type;
    
    public String getType() {
        return type;
    }
 
    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return "images/"+name+".png";
    }
    
    private Pokemon () {}
    
    public Pokemon(String name, String type) {
        this.name = name;
        this.type = type;
    }    
}