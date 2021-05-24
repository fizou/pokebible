package com.pokebible;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

/*
 *
 * Pokemon Repository : Declare all specific access Method to access to pokemon database : 
 * 
 * Note: 
 * - Default API are auto generated : findAll(), findById(int long), save(), delete(), ...  
 * - JpaRepository extends PagingAndSortingRepository which in turn extends CrudRepository.
 *   
 */
    
//@Api(tags = "Pokemon Repository")
//@RepositoryRestResource()
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {

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
    List<Pokemon> findByNumberEqualsIgnoreCaseOrderByNumberAsc(String number);
    
    // Pageable by Name, Number, Type1, Type2 
    Page<Pokemon> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Pokemon> findByNumberEqualsIgnoreCase(String name, Pageable pageable);
    Page<Pokemon> findByType1ContainingIgnoreCase(String name, Pageable pageable);
    Page<Pokemon> findByType2ContainingIgnoreCase(String name, Pageable pageable);

    // Pageable by Type (Custom query but sort is not possible) 
    //Query("select c from Pokemon c where lower(c.type1) like lower(CONCAT('%',:name,'%')) or lower(c.type2) like lower(CONCAT('%',:name,'%'))")
    //Page<Pokemon> findByTypeContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    // List by Type (Custom query, sort/pageable is done in service) 
    @Query("select c from Pokemon c where lower(c.type1) like lower(CONCAT('%',:name,'%')) or lower(c.type2) like lower(CONCAT('%',:name,'%'))")
    List<Pokemon> findByTypeContainingIgnoreCaseOrderByNumberAsc(@Param("name") String name);
    
}
