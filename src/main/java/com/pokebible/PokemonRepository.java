package com.pokebible;

import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/*
 *
 * Pokemon Repository : Declare all specific access Method to access to pokemon database : 
 * 
 * Note: Default API are auto generated : findAll(), findById(int long), save(), delete(), ...  
 *   
 */
    
//@Api(tags = "Pokemon Repository")
//@RepositoryRestResource()
public interface PokemonRepository extends CrudRepository<Pokemon, Long>, PagingAndSortingRepository<Pokemon, Long> {

    //
    // findAll specific declaration
    //
    
    List<Pokemon> findAllByOrderByNumberAsc();
    
    //
    // findBy specific declaration
    //
    
    // List by Name
    //List<Pokemon> findByName(@Param("name") String name);
    List<Pokemon> findByNameContainingIgnoreCaseOrderByNumberAsc(String name);

    // List by Number
    //List<Pokemon> findByNumber(@Param("number") String number);
    List<Pokemon> findByNumberContainingIgnoreCaseOrderByNumberAsc(String number);
    
    // Pageable by Name, Number, type1, type2 
    Page<Pokemon> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Pokemon> findByNumberContainingIgnoreCase(String name, Pageable pageable);
    Page<Pokemon> findByType1ContainingIgnoreCase(String name, Pageable pageable);
    Page<Pokemon> findByType2ContainingIgnoreCase(String name, Pageable pageable);

    // Pageable by type (Custom query) 
    //Query("select c from Pokemon c where lower(c.type1) like lower(CONCAT('%',:name,'%')) or lower(c.type2) like lower(CONCAT('%',:name,'%'))")
    //Page<Pokemon> findByTypeContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    // List by type (Custom query) 
    @Query("select c from Pokemon c where lower(c.type1) like lower(CONCAT('%',:name,'%')) or lower(c.type2) like lower(CONCAT('%',:name,'%'))")
    List<Pokemon> findByTypeContainingIgnoreCaseOrderByNumberAsc(@Param("name") String name);
    
}
