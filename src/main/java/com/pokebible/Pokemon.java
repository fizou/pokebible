package com.pokebible;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pokebible.validator.EnableNotMatchConstraint;
import com.pokebible.validator.NotMatch;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.*;
import com.pokebible.validator.UniqueNumberConstraint;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Entity
@EnableNotMatchConstraint
@UniqueNumberConstraint
@JsonPropertyOrder({"id", "number", "name", "type", "pictureUrl", "type1", "type1Label", "type1PictureUrl", "type2", "type2Label", "type2PictureUrl"})
@JsonIgnoreProperties("allTypes") 
public class Pokemon {
 
   /*
    * Pokemon Entity : number, name, type (type1 and type2)
    *
    *   "001","Bulbasaur",Pokemon.Type.GRASS,Pokemon.Type.POISON
    * 
    * Some utilities method like:
    *
    *   pokemon picture url, type1/2 picture url, type concatenation (GRASS,POISON), ...
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
    @ApiModelProperty(position = 0)
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
    @ApiModelProperty(position = 1)
    private String number;
    
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    @NotBlank (message="This field is required.")
    @ApiModelProperty(position = 2)
    private String name;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    @NotBlank (message="This field is required.")
    //@Pattern(regexp = "^(?!NONE$).*$",  message="NONE is not possible for this field.")
    @Pattern(regexp = "(?:(?:^|, )(NORMAL|GRASS|FIRE|WATER|FIGHTING|FLYING|POISON|GROUND|ROCK|BUG|GHOST|ELECTRIC|PSYCHIC|ICE|DRAGON|DARK|STEEL|FAIRY))+$",  message="NORMAL, GRASS, FIRE, WATER, FIGHTING, FLYING, POISON, GROUND, ROCK, BUG, GHOST, ELECTRIC, PSYCHIC, ICE, DRAGON, DARK, STEEL, FAIRY are the only value possible for this field.")
    @ApiModelProperty(position = 3)
    private String type1;
    
    public String getType1() {
        if (type1==null||type1.equals("")) {
            return Type.NONE.getName();
        } else {
            return type1;
        }
    }

    @ApiModelProperty(position = 4)
    public String getType1Label() {
        try {
            return Type.valueOf(getType1()).label;
        } catch (Exception e) {
            logger.warn("type1 value is '"+getType1()+"' but is unknown in Enum Type.");
            return getType1();            
        }
    }
    @ApiModelProperty(position = 5)
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

    @Pattern(regexp = "(?:(?:^|, )(NONE|NORMAL|GRASS|FIRE|WATER|FIGHTING|FLYING|POISON|GROUND|ROCK|BUG|GHOST|ELECTRIC|PSYCHIC|ICE|DRAGON|DARK|STEEL|FAIRY))+$",  message="NONE, NORMAL, GRASS, FIRE, WATER, FIGHTING, FLYING, POISON, GROUND, ROCK, BUG, GHOST, ELECTRIC, PSYCHIC, ICE, DRAGON, DARK, STEEL, FAIRY are the only value possible for this field.")
    //@PokemonType2vsType1Constraint
    @NotMatch(field = "type1")
    @ApiModelProperty(position = 6)
    private String type2;
    
    public String getType2() {
        if (type2==null||type2.equals("")) {
            return Type.NONE.getName();
        } else {
            return type2;
        }
    }
    @ApiModelProperty(position = 7)
    public String getType2Label() {
        try {
            return Type.valueOf(getType2()).label;
        } catch (Exception e) {
            logger.warn("type2 value is '"+getType2()+"' but is unknown in Enum Type.");
            return getType2();
        }
    }
    @ApiModelProperty(position = 8)
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
    
    @ApiModelProperty(position = 9)
    public String getType() {
        if (getType2().equals(Type.NONE.getName())) {
            return getType1();
        }
        else {
            return getType1() + "," + getType2();
        }  
    }
 
    @ApiModelProperty(position = 10)
    public String getPictureUrl() {
        return "/images/pokemons/"+getNumber()+".png";
    }

    public String toString(){
        return ""+this.number+"-"+this.name+" ("+this.getType()+")";
    }
    
    public Pokemon() {
        setNumber("");
        setName("");
        setType1("");
        setType2("");
    	//logger.debug("Contructor - No args");
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

    // Utility: Transform a List<Pokemon> to Page<Pokemon>
    public static Page<Pokemon> toPage(List<Pokemon> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        if(start > list.size())
            return new PageImpl<>(new ArrayList<>(), pageable, list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }        

}