package com.pokebible;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final PokemonRepository repository;

    @Autowired
    public DatabaseLoader(PokemonRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception {
    	Tracer.warn("DatabaseLoader - run - Filling database");
        this.repository.save(new Pokemon("Bulbizarre", "Plante"));
        this.repository.save(new Pokemon("Herbizarre", "Plante"));
        this.repository.save(new Pokemon("Florizarre", "Plante"));
        this.repository.save(new Pokemon("Salamèche", "Feu"));
    }
}
