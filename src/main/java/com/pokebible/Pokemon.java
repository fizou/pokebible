package com.pokebible;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pokemon {
 
    private static final Logger logger = LoggerFactory.getLogger(Pokemon.class);

    // Pokemon Type: NORMAL, GRASS, ... 
    public enum Type 
    {

        NONE(""),     
        NORMAL("Normal"),     
        GRASS("Grass"),       
        FIRE("Fire"),         
        WATER("Water"),       
        FIGHTING("Fighting"), 
        FLYING("Flying"),     
        POISON("Poison"),     
        GROUND("Ground"),     
        ROCK("Rock"),         
        BUG("Bug"),           
        GHOST("Ghost"),       
        ELECTRIC("Electric"), 
        PSYCHIC("Psychic"),   
        ICE("Ice"),           
        DRAGON("Dragon"),     
        DARK("Dark"),         
        STEEL("Steel"),       
        FAIRY("Fairy");       

        private String label;

        private Type(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public String getName() {
            return name();
        }
    }
    public ArrayList<Type> getAllTypes() {
        ArrayList<Type> listType = new ArrayList<Type>();
        for(Type type : Type.values())
        {
            listType.add(type);
        }
        return listType;
    }
        
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
 
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
                if (type1!=null) {
    		return type1.toUpperCase();
                } else {
                    return "";                    
                }
    	}
    	
    }
 
    public void setType(String type) {
        if (type.indexOf(',')!=-1) {
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
        if (type1==null) return "";
        if (type1.equals("")) {
            return "/images/types/none.gif";
        } else {
            return "/images/types/"+type1.toLowerCase()+".gif";
        }
    }
    public void setType1(String type1) {
        this.type1 = type1;
    }
    public boolean isType1Of(String type) {
        if (getType1().equals(type)) {
            return true;
        } else {
            return false;
        }
    }

    private String type2;
    
    public String getType2() {
        return type2;
    }
    public String getType2PictureUrl() {
        if (type1==null) return "";
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

    public Pokemon(String num, String nameEn, Type type1, Type type2) {
        this.num = num;
        this.name = nameEn;
        this.type1 = type1.getLabel();
        this.type2 = type2.getLabel();
        logger.info("Pokemon - 4 args - {}", this);
    }    

    public Pokemon(String num, String nameEn, Type type1, Type type2, String nameFr) {
        this.num = num;
        this.name = nameEn;
        this.type1 = type1.getLabel();
        this.type2 = type2.getLabel();
        logger.info("Pokemon - 5 args - {}", this);
    }    

}