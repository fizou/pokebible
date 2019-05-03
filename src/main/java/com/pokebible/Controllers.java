package com.pokebible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Controllers {
	
    private static final Logger logger = LoggerFactory.getLogger(Controllers.class);

    @GetMapping(path = "/")
    public String root() {
	logger.warn("Controllers - root");
        //return "index.html";
        return "home";
    }
    
    @Autowired
    private PokemonRepository repository;

    @ModelAttribute("pokemonSelection")
    public Iterable<Pokemon> pokemonSelection(@RequestParam(defaultValue="") String searchString) {

	logger.warn("Controllers - pokemonSelection - SearchString: {}", searchString);
		
        if (searchString.equals("")){
            return this.repository.findAll();
        } else {
            return repository.findByName(searchString);
        }

    }
    
    @GetMapping(path = "/home")
    @PostMapping(path = "/home")
    public String home(@RequestParam(defaultValue="") String searchString) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
	if (auth != null) {
            logger.warn("Controllers - home - User: {} User", auth.getName());
	} else {
            logger.warn("Controllers - home - User: Anonymous User");
	}
		
        return "home";
    }
    
    @GetMapping(path = "/help")
    public String about() {
	logger.warn("Controllers - help");
        return "help";
    }

    @GetMapping(path = "/login")
    public String login() {
	logger.warn("Controllers - login");
        return "login";
    }

    @Autowired
    Messages messages;
    
    @GetMapping(path = "/monitoring")
    @ResponseBody
    public String testRunning() {
	logger.warn("Controllers - test - message.properties "+messages.get("monitoring.title"));
        return "<H1>"+messages.get("monitoring.title")+"</H1><HR>"+messages.get("monitoring.keySentence")+"...";
    }
	
}
