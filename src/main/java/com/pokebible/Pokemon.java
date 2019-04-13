package com.pokebible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pokemon {
 
    private final static Logger logger = LoggerFactory.getLogger(Pokemon.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
 
    private String num;
    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }

    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
    	if (type2!=null&&!type2.equals("")) {
    		return type1.toUpperCase() + ", " + type2.toUpperCase();
    	} else {
    		return type1.toUpperCase();
    	}
    	
    }
 
    public void setType(String type) {
        if (type!=null&&type.indexOf(",")!=-1) {
        	this.type1 = type.substring(0, type.indexOf(","));
        	this.type2 = type.substring(type.indexOf(",")+1,type.length());
        } else {
	        this.type1 = type;
        	this.type2 = "";        	
        }
    }

    private String type1;
    
    public String getType1() {
        return type1;
    }
 
    public void setType1(String type1) {
        this.type1 = type1;
    }

    private String type2;
    
    public String getType2() {
        return type2;
    }
 
    public void setType2(String type2) {
        this.type2 = type2;
    }
    
    public String getPicture() {
        return "images/pokemons/"+num+".png";
    }
    public String getPicture2() {
        return "../images/pokemons/"+num+".png";
    }
    public String getPicture3() {
        return "<img src='../images/pokemons/"+num+".png' height='30'/>";
    }

    public String getPicture4() {
        return "assets/pokemons/"+num+".png";
    }

    public String toString(){
        return "("+this.num+") "+this.name+" - "+this.getType();
    }
    
    public Pokemon() {
    	logger.debug("Pokemon - No args");
    }
    
    public Pokemon(String num, String name, String type) {
        this.num = num;
        this.name = name;
        this.setType(type);
        logger.info("Pokemon - 3 args - "+this.toString());
    }    

    public Pokemon(String num, String name_en, String type1, String type2, String name) {
        this.num = num;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        logger.info("Pokemon - 5 args - "+this.toString());
    }    

}