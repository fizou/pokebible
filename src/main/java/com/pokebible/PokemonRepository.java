package com.pokebible;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

//@Api(tags = "Pokemon Services")
//@RepositoryRestResource(path = "pokemons")
@RepositoryRestResource()
public interface PokemonRepository extends CrudRepository<Pokemon, Long> {

	// Default API (Auto generate)  
	
        // Specifics API   
	@ApiOperation("find all pokemon by name")
	//List<Pokemon> findByName(@Param("name") @ApiParam(value="Name of the pokemon") String name);
	 
	@Query("select c from Pokemon c where c.name like CONCAT('',:name,'%')")
	List<Pokemon> findByName(@Param("name") @ApiParam(value="Name of the pokemon") String name);
        
        List<Pokemon> findByNameContainingIgnoreCase(String name);

}
