package com.pokebible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pokemon {
 
    private static final Logger logger = LoggerFactory.getLogger(Pokemon.class);

    public static final String TYPE_NORMAL = "Normal";
    public static final String TYPE_GRASS = "Grass";
    public static final String TYPE_FIRE = "Fire";
    public static final String TYPE_WATER = "Water";
    public static final String TYPE_FIGHTING = "Fighting";
    public static final String TYPE_FLYING = "Flying";
    public static final String TYPE_POISON = "Poison";
    public static final String TYPE_GROUND = "Ground";
    public static final String TYPE_ROCK = "Rock";
    public static final String TYPE_BUG = "Bug";
    public static final String TYPE_GHOST = "Ghost";
    public static final String TYPE_ELECTRIC = "Electric";
    public static final String TYPE_PSYCHIC = "Psychic";
    public static final String TYPE_ICE = "Ice";
    public static final String TYPE_DRAGON = "Dragon";
    public static final String TYPE_DARK = "Dark";
    public static final String TYPE_STEEL = "Steel";
    public static final String TYPE_FAERY = "Faery";
        
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
        if (type!=null&&type.indexOf(',')!=-1) {
        	this.type1 = type.substring(0, type.indexOf(','));
        	this.type2 = type.substring(type.indexOf(',')+1,type.length());
        } else {
	        this.type1 = type;
        	this.type2 = "";        	
        }
    }

    private String type1;
    
    public String getType1() {
        return type1;
    }
    public String getType1PictureUrl() {
        if (type1.equals("")) {
            return "/images/types/none.gif";
        } else {
            return "/images/types/"+type1.toLowerCase()+".gif";
        }
    }
    public void setType1(String type1) {
        this.type1 = type1;
    }

    private String type2;
    
    public String getType2() {
        return type2;
    }
    public String getType2PictureUrl() {
        if (type2.equals("")) {
            return "/images/types/none.gif";
        } else {
        return "/images/types/"+type2.toLowerCase()+".gif";
        }
    }
 
    public void setType2(String type2) {
        this.type2 = type2;
    }
    
    public String getPictureUrl() {
        return "/images/pokemons/"+num+".png";
    }
    public String getPictureHtmlTag() {
        return "<img src='"+getPictureUrl()+"' height='30'/>";
    }

    public String toString(){
        return "("+this.num+") "+this.name+" - "+this.getType();
    }
    
    public Pokemon() {
    	//logger.debug("Pokemon - No args");
    }
    
    public Pokemon(String num, String name, String type) {
        this.num = num;
        this.name = name;
        this.setType(type);
        logger.info("Pokemon - 3 args - {}", this);
    }    

    public Pokemon(String num, String nameEn, String type1, String type2, String name) {
        this.num = num;
        this.name = nameEn;
        this.type1 = type1;
        this.type2 = type2;
        logger.info("Pokemon - 5 args - {}", this);
    }    

}