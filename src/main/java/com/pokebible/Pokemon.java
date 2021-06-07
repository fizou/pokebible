package com.pokebible;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@JsonIgnoreProperties("types") 
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
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @JsonPropertyOrder({"name", "label", "picture", "strongAgainst", "vulnerableTo"})
    public static enum Type 
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

        @ApiModelProperty(position = 0)
        public String getName() {
            return name();
        }

        @ApiModelProperty(position = 1)
        public String getLabel() {
            return label;
        }

        @ApiModelProperty(position = 2)
        public String getPicture() {
            return "/images/types/"+getName().toLowerCase()+".png";
        }
                
        @ApiModelProperty(position = 3)
        public ArrayList<String> getStrongAgainst() {
            //logger.debug("getStrongAgainst - Type: "+this.getName());
            //logger.debug("getStrongAgainst - Ordinal: "+this.ordinal());
            ArrayList<String> result = new ArrayList<String>();

            for (int i=1;i<Type.values().length;i++) {
                //logger.debug("getStrongAgainst - i: "+i);
                //logger.debug("getStrongAgainst - Type.values(): "+Type.values()[i]);
                EffectLevel effectLevel = EffectTable[i][this.ordinal()];
                //logger.debug("getStrongAgainst - effectLevel: "+effectLevel);
                Double effect = effectLevel.getMultiplier();
                //logger.debug("getStrongAgainst - effect: "+effect);
                if (effect>1) {
                    result.add(""+Type.values()[i]);
                    //logger.debug("getStrongAgainst - result int: "+result);
                }
            }
            //logger.debug("getStrongAgainst - "+this.getName()+" - result: "+result);
            return result;
        }

        @ApiModelProperty(position = 4)
        public ArrayList<String> getVulnerableTo() {
            //logger.debug("getVulnerableTo - Type: "+this.getName());
            //logger.debug("getVulnerableTo - Ordinal: "+this.ordinal());
            ArrayList<String> result = new ArrayList<String>();

            for (int i=1;i<Type.values().length;i++) {
                //logger.debug("getVulnerableTo - i: "+i);
                //logger.debug("getVulnerableTo - Type.values(): "+Type.values()[i]);
                EffectLevel effectLevel = EffectTable[this.ordinal()][i];
                //logger.debug("getVulnerableTo - effectLevel: "+effectLevel);
                Double effect = effectLevel.getMultiplier();
                //logger.debug("getVulnerableTo - effect: "+effect);
                if (effect>1) {
                    result.add(""+Type.values()[i]);
                    //logger.debug("getStrongAgainst - result int: "+result);
                }
            }
            //logger.debug("getVulnerableTo - "+this.getName()+" - result: "+result);
            return result;
        }
        
        // getEffect with Simple Type     
        public static EffectLevel getEffect(String attackerType, String defenderType) {
            Type attackTypeEnum = Type.valueOf(attackerType);
            Type defenderTypeEnum = Type.valueOf(defenderType);

            return EffectTable[defenderTypeEnum.ordinal()][attackTypeEnum.ordinal()];
            
        }

        // getEffect with Double Type     
        //    - We are taking the type1 of attacker
        //    - We are calculate multiplierType1 for type1 of attacker against type1 of defender
        //    - We are modify (*) this multiplierType1 for type1 of attacker against type2 of defender (If defender has one)
        //    - THEN we are calculate multiplierType2 based on the type2 of attacker, redo the same with type1 and type2 of defender and keep the best multiplier (multiplierType1 or multiplierType2)
        public static EffectLevel getEffect(String attackerType1, String attackerType2, String defenderType1, String defenderType2) {
            
            double result=0;
            
            double multiplierType1 = getEffect(attackerType1, defenderType1).getMultiplier();
            //logger.debug("multiplierA1 ("+attackerType1+" vs "+defenderType1+"): "+multiplierType1);
            multiplierType1 = multiplierType1 * getEffect(attackerType1, defenderType2).getMultiplier();
            //logger.debug("multiplierA2 ("+attackerType1+" vs "+defenderType2+"): "+multiplierType1);
            double multiplierType2 = getEffect(attackerType2, defenderType1).getMultiplier();
            //logger.debug("multiplierA3 ("+attackerType2+" vs "+defenderType1+"): "+multiplierType2);
            multiplierType2 = multiplierType2 * getEffect(attackerType2, defenderType2).getMultiplier();
            //logger.debug("multiplierA4 ("+attackerType2+" vs "+defenderType2+"): "+multiplierType2);
            if (multiplierType2>multiplierType1) multiplierType1=multiplierType2;
            
            result=multiplierType1;
            
            if (result <= EffectLevel.INEFFECTIVE.getMultiplier()) return EffectLevel.INEFFECTIVE;
            if (result <= EffectLevel.WEAK.getMultiplier()) return EffectLevel.WEAK;
            if (result <= EffectLevel.NORMAL.getMultiplier()) return EffectLevel.NORMAL;
            if (result <= EffectLevel.STRONG.getMultiplier()) return EffectLevel.STRONG;
            return EffectLevel.SUPEREFFECTIVE;
            
        }

        // getEffect with 2 Pokemons
        public static EffectLevel getEffect(Pokemon attackerPokemon, Pokemon defenderPokemon) {

            return getEffect(attackerPokemon.type1, attackerPokemon.type2, defenderPokemon.type1, defenderPokemon.type2);
            
        }
        
        // Deliver the effect of attack and the multiplierType2 associated 
        public enum EffectLevel {
            INEFFECTIVE(0.25), WEAK(0.5), NORMAL(1.0), STRONG(2.0), SUPEREFFECTIVE(4.0);

            private double value;

            EffectLevel(final double newValue) {
                value = newValue;
            }

            public double getMultiplier() {
                return value;
            }
        }

        private static final EffectLevel norm = EffectLevel.NORMAL;
        private static final EffectLevel weak = EffectLevel.WEAK;
        private static final EffectLevel str  = EffectLevel.STRONG;
        private static final EffectLevel inef = EffectLevel.INEFFECTIVE;
        private static final EffectLevel[][] EffectTable =
        {        //                                                 ATT
                 // none  norm  grss  fire  wter  fght  fly   pois  grnd  rock  bug   ghst  elec  psyc  ice   drag  dark  stel  fair
        /* none */ {norm, norm, norm, norm, norm, norm, norm, norm, norm, norm, norm, norm, norm, norm, norm, norm, norm, norm, norm}, /* none */
        /* norm */ {norm, norm, norm, norm, norm, str , norm, norm, norm, norm, norm, inef, norm, norm, norm, norm, norm, norm, norm}, /* norm */
        /* grss */ {norm, norm, weak, str , weak, norm, str , str , weak, norm, str , norm, weak, norm, str , norm, norm, norm, norm}, /* grss */
        /* fire */ {norm, norm, weak, weak, str , norm, norm, norm, str , str , weak, norm, norm, norm, weak, norm, norm, weak, weak}, /* fire */
        /* wter */ {norm, norm, str , weak, weak, norm, norm, norm, norm, norm, norm, norm, str , norm, weak, norm, norm, weak, norm}, /* wter */
        /* fght */ {norm, norm, norm, norm, norm, norm, str , norm, norm, norm, norm, inef, norm, str , norm, norm, norm, norm, str }, /* fght */
        /* fly  */ {norm, norm, weak, norm, norm, weak, norm, norm, inef, str , weak, norm, str , norm, str , norm, norm, norm, norm}, /* fly  */
        /* pois */ {norm, norm, weak, norm, norm, weak, norm, weak, str , norm, weak, norm, norm, str , norm, norm, norm, norm, weak}, /* pois */
        /* grnd */ {norm, norm, str , norm, str , norm, norm, weak, norm, weak, norm, norm, inef, norm, str , norm, norm, norm, norm}, /* grnd */
    /* DEF rock */ {norm, weak, str , norm, str , str , weak, weak, str , norm, norm, norm, norm, norm, norm, norm, norm, str , norm}, /* rock DEF */
        /* bug  */ {norm, norm, weak, str , norm, weak, str , norm, weak, str , norm, norm, norm, norm, norm, norm, norm, norm, norm}, /* bug  */
        /* ghst */ {norm, inef, norm, norm, norm, inef, norm, weak, norm, norm, weak, str , norm, norm, norm, norm, str , norm, norm}, /* ghst */
        /* elec */ {norm, norm, norm, norm, norm, norm, weak, norm, str , norm, norm, norm, weak, norm, norm, norm, norm, weak, norm}, /* elec */
        /* psyc */ {norm, norm, norm, norm, norm, weak, norm, norm, norm, norm, str , str , norm, weak, norm, norm, str , norm, norm}, /* psyc */
        /* ice  */ {norm, norm, norm, str , norm, str , norm, norm, norm, str , norm, norm, norm, norm, weak, norm, norm, str , norm}, /* ice  */
        /* drag */ {norm, norm, weak, weak, weak, norm, norm, norm, norm, norm, norm, norm, weak, norm, str , str , norm, norm, str }, /* drag */
        /* dark */ {norm, norm, norm, norm, norm, str , norm, norm, norm, norm, str , norm, norm, inef, norm, norm, weak, norm, str }, /* dark */
        /* stel */ {norm, weak, weak, str , norm, str , weak, inef, str , weak, weak, weak, norm, weak, weak, weak, weak, weak, norm}, /* stel */
        /* fair */ {norm, norm, norm, norm, norm, weak, norm, str,  norm, norm, weak, norm, norm, norm, norm, inef, norm, str , weak}  /* fair */
                 // none  norm  grss  fire  wter  fght  fly   pois  grnd  rock  bug   ghst  elec  psyc  ice   drag  dark  stel  fair
        };       //                                                 ATT
        

    }
    
    public static List<Type> getTypes() {
        List<Type> listType = new ArrayList<Type>();
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