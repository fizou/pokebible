package com.pokebible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Controllers {
	
    private static final Logger logger = LoggerFactory.getLogger(Controllers.class);

    @Autowired
    private PokemonRepository repository;

    @GetMapping(path = "/")
    public String root() {
	logger.debug("Controllers - root");
        //return "index.html";
        return "home";
    }
    
    @ModelAttribute("pokemonSelection")
    public Iterable<Pokemon> pokemonSelection(@RequestParam(defaultValue="") String searchString) {

	logger.debug("Controllers - pokemonSelection - SearchString: {}", searchString);
		
        if (searchString.equals("")){
            return repository.findAll();
        } else {
            return repository.findByNameContainingIgnoreCase(searchString);
        }

    }
    
    @GetMapping(path = "/home")
    @PostMapping(path = "/home")
    public String home(@RequestParam(defaultValue="") String searchString) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
	if (auth != null) {
            logger.info("Controllers - home - User: {} User", auth.getName());
	} else {
            logger.info("Controllers - home - User: Anonymous User");
	}
		
        return "home";
    }
/*
    @GetMapping("/detail")
    public void detail(Model model) {
	logger.debug("Controllers - detail - ");
        model.addAttribute("attribute1", "attributeValue1");
        
        //return "detail";
        //return "detail"; 
        //return "ID: " + id;
    }    
*/
    
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable String id, Model model) {
	logger.debug("Controllers - detail - "+id);
        
        Pokemon pokemon = repository.findOne(new Long(id));
                
        model.addAttribute("pokemon", pokemon);
        
        return "detail"; 
        //return "ID: " + id;
    }    

    @GetMapping(path = "/help")
    public String about() {
	logger.debug("Controllers - help");
        return "help";
    }

    @GetMapping(path = "/login")
    public String login() {
	logger.debug("Controllers - login");
        return "login";
    }

    // Monitoring Page : Without Template page (ResponseBody) and with messages taken from translation label file
    @Autowired
    Messages messages;
    
    @GetMapping(path = "/monitoring")
    @ResponseBody
    public String testRunning() {
	logger.debug("Controllers - test - message.properties "+messages.get("monitoring.title"));
        return "<H1>"+messages.get("monitoring.title")+"</H1><HR>"+messages.get("monitoring.keySentence")+"...";
    }
	
}
