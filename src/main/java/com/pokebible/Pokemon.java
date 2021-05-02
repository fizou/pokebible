package com.pokebible;

import com.pokebible.validator.PokemonNumberUniqueConstraint;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.*;
import com.pokebible.validator.OnInsertGroup;

@Entity
public class Pokemon {
 
   /*
    * Pokemon Entity : Num, Name, type (type1 and type2), picture url, ...
    *   
    */

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
 
    @NotBlank(message="This field is required.")
    @Length(min=3, max=3, message="Lenght must be 3.")
    @Pattern(regexp = "[0-9]+",  message="Only numeric format (001, 002, ...)  is accepted.")
    @PokemonNumberUniqueConstraint(groups = OnInsertGroup.class) // Unique cosntraint on insert only. However we couldn't update the pokemon with its current number ;)
    private String number;
    
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    @NotBlank (message="This field is required.")
    private String name;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    @NotBlank (message="This field is required.")
    @Pattern(regexp = "^(?!NONE$).*$",  message="This field is required.")
    private String type1;
    
    public String getType1() {
        if (type1==null||type1.equals("")) {
            return Type.NONE.getName();
        } else {
            return type1;
        }
    }
    public String getType1Label() {
        return Type.valueOf(getType1()).label;
    }
    public String getType1PictureUrl() {
        return "/images/types/"+getType1().toLowerCase()+".png";
    }
    public void setType1(String type1) {
    	if (type1==null||type1.equals("")) {
            this.type1 = Type.NONE.getName();
        } else {
            this.type1 = type1;
        }
    }
    public boolean isType1Of(String type) {
        if (type==null||!getType1().equals(type)) {
            return false;
        } else {
            return true;
        }
    }

    private String type2;
    
    public String getType2() {
        if (type2==null||type2.equals("")) {
            return Type.NONE.getName();
        } else {
            return type2;
        }
    }
    public String getType2Label() {
        return Type.valueOf(getType2()).label;
    }
    public String getType2PictureUrl() {
        return "/images/types/"+getType2().toLowerCase()+".png";
    }
    public void setType2(String type2) {
    	if (type2==null||type2.equals("")) {
            this.type2 = Type.NONE.getName();
        } else {
            this.type2 = type2;
        }
    }
    public boolean isType2Of(String type) {
        if (type==null||!getType2().equals(type)) {
            return false;
        } else {
            return true;
        }
    }
    
    public String getPictureUrl() {
        return "/images/pokemons/"+getNumber()+".png";
    }
    public String getPictureHtmlTag() {
        return "<img src='"+getPictureUrl()+"' height='30'/>";
    }

    public String getType() {
        return getType1() + ", " + getType2();
    }
 
    public void setType(String type) {
        if (type==null||type.equals("")) {
            setType1("");
            setType2("");
        }
        if (type.indexOf(',')!=-1) {
            setType1(type.substring(0, type.indexOf(',')));
            setType2(type.substring(type.indexOf(',')+1,type.length()));
        } else {
            setType1(type);
            setType2("");        	
        }
    }

    public String toString(){
        return "("+this.number+") "+this.name+" - "+this.getType();
    }
    
    public Pokemon() {
        setNumber("");
        setName("");
        setType1("");
        setType2("");
    	//logger.debug("Contructor - No args");
    }
    
    public Pokemon(String num, String name, String type) {
        setNumber(num);
        setName(name);
        setType(type);
        logger.debug("Contructor - 3 args - {}", this);
    }    

    public Pokemon(String num, String nameEn, Type type1, Type type2) {
        setNumber(num);
        setName(name);
        setType1(type1.getName());
        setType2(type2.getName());
        logger.debug("Contructor - 4 args - {}", this);
    }    

    public Pokemon(String num, String nameEn, Type type1, Type type2, String nameFr) {
        this.number = num;
        this.name = nameEn;
        this.type1 = type1.getName();
        this.type2 = type2.getName();
        logger.debug("Contructor - 5 args - {}", this);
    }    

}