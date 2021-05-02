package com.pokebible;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@Api(tags = "Pokemon Services")
//@RepositoryRestResource(path = "pokemons")
@RepositoryRestResource()
public interface PokemonRepository extends CrudRepository<Pokemon, Long>, PagingAndSortingRepository<Pokemon, Long> {

    /*
     * Pokemon Repository : Acces to pokemon database : Declare all specific access Method
     *   
     */
    
    // Note Default API are auto generated : findAll(), findById(int long), save(), delete(), ...  

    // Specifics API DEclaration
    
    // findAll specific declaration
    @ApiOperation("find all pokemons order by ascending number")
    List<Pokemon> findAllByOrderByNumberAsc();
    
    // findBy specific declaration

    // by Name
    @ApiOperation("Find pokemons by complete name and case sensitive")
    //@Query("select c from Pokemon c where c.name like CONCAT('',:name,'%')")
    List<Pokemon> findByName(@Param("name") @ApiParam(value="Name of the pokemon") String name);

    @ApiOperation("Find pokemons by partial name order by ascending number")
    List<Pokemon> findByNameContainingIgnoreCaseOrderByNumberAsc(String name);

    @ApiOperation("Find pokemons by partial name and pageable criteria")
    Page<Pokemon> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // by Number
    @ApiOperation("Find pokemons by number")
    List<Pokemon> findByNumber(@Param("number") String number);

}
