package com.pokebible;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Controllers {
	
    Logger logger = LoggerFactory.getLogger(Controllers.class);

    @Autowired
    private PokemonRepository repository;

    @ModelAttribute("pokemonSelection")
    public Iterable<Pokemon> pokemonSelection() {
        return this.repository.findAll();
    }
    
    @RequestMapping("/")
    public String root() {
	logger.warn("Controllers - root");
        //return "index.html";
        return "home";
    }
    
    @RequestMapping("/home")
    public String home(@RequestParam(defaultValue="") String searchString) {

	logger.warn("Controllers - home - SearchString: '"+searchString+"'");
		
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	if (auth != null) {
            logger.warn("Controllers - home - User: " + auth.getName() +" User");
	} else {
            logger.warn("Controllers - home - User: Anonymous User");
	}
		
        return "home";
    }
    
    @RequestMapping("/help")
    String about() {
	logger.warn("Controllers - help");
        return "help";
    }

    @RequestMapping("/login")
    String login() {
	logger.warn("Controllers - login");
        return "login";
    }
	
    @RequestMapping("/testRunning")
    @ResponseBody
    String testRunning() {
	logger.warn("Controllers - testRunning");
        return "<H1>Pokemon Bible</H1><HR/>is running well...";
    }
	
}
