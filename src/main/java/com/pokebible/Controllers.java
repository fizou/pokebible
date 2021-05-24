package com.pokebible;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * Web Controller: All Website Pages... 
 *
 * - / : Root page (redirecton /home)
 * 
 * - /home : Main Page (List of pokemon)
 * - /detail : Detail of one pokemon 
 * 
 * - CRUD /get/{id} / add /update /delete/{id}
 * 
 * - /login : Display credentials login page
 * 
 * - /help : Help on how to login with thymleaf template 
 * - /test : test page hardcoded
 * - /monitoring : test page hardcoded with translation
 * 
 */

@Controller
public class Controllers {
	
    private static final Logger logger = LoggerFactory.getLogger(Controllers.class);

    @Autowired
    private PokemonService service;
    
    @GetMapping(path = "/")
    public String root() {
        
        logger.info("/ - User: {} ask server root",  service.getLoggedUserName());

        return "redirect:/home"; // Cancel the return of static/index.html resource and replace it by home controller
        
    }
        
    @GetMapping(path = "/home")
    @PostMapping(path = "/home")
    public String home(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("sortField") Optional<String> sortField,
            @RequestParam("sortDirection") Optional<String> sortDirection,
            @RequestParam("searchField") Optional<String> searchField,
            @RequestParam("searchString") Optional<String> searchString,
            Model model) {

        logger.info("/home - User: {} is diplaying pokemon list",  service.getLoggedUserName());

        // Param
        logger.debug("Param - page: {}", page);
        logger.debug("Param - size: {}", size);
        logger.debug("Param - sortField: {}", sortField);
        logger.debug("Param - sortDirection: {}", sortDirection);
        logger.debug("Param - searchField: {}", searchField);
        logger.debug("Param - searchString: {}", searchString);

        // Pagination
        int currentPage = page.orElse(1);
        if (currentPage==0) currentPage=1;
        logger.debug("Calculated - currentPage: {}",  currentPage);
        int pageSize = size.orElse(9);
        if (pageSize==0) pageSize=9; 
        logger.debug("Calculated - pageSize: {}",  pageSize);
        String pageSortField = sortField.orElse("Number");
        if (pageSortField.equals("")) pageSortField="Number";
        logger.debug("Calculated - pageSortField: {}",  pageSortField);
        String pageSortDirection = sortDirection.orElse("asc");
        if (pageSortDirection.equals("")) pageSortDirection="asc";
        logger.debug("Calculated - pageSortDirection: {}",  pageSortDirection);
        String pageSearchField = searchField.orElse("Name");
        if (pageSearchField.equals("")) pageSearchField="Name";
        logger.debug("Calculated - pageSearchField: '{}'",  pageSearchField);
        String pageSearchString = searchString.orElse("");
        logger.debug("Calculated - pageSearchString: '{}'",  pageSearchString);

        Page<Pokemon> pokemonPage = service.findAllPaginated(currentPage, pageSize, pageSortField, pageSortDirection, pageSearchField, pageSearchString);

        model.addAttribute("pokemonPage", pokemonPage);
        logger.debug("Pagination Result - pokemonPage: {}",  pokemonPage);
        logger.debug("Pagination Result - pokemonPage getSize: {}",  pokemonPage.getSize());
        logger.debug("Pagination Result - pokemonPage getNumber: {}",  pokemonPage.getNumber());
        logger.debug("Pagination Result - pokemonPage previousOrFirstPageable: {}",  pokemonPage.previousOrFirstPageable().getPageNumber());
        logger.debug("Pagination Result - pokemonPage nextOrLastPageable: {}",  pokemonPage.nextOrLastPageable().getPageNumber());

        // Pagination Model Attibutes
        int totalPages = pokemonPage.getTotalPages();
        logger.debug("Pagination Result - totalPages {}",  totalPages);
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers); // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17]
            logger.debug("attribute - pageNumbers: {}",  pageNumbers);
        }
        // this vars are use 

        // Filter and Sorting Model Attibutes
        model.addAttribute("sortField", pageSortField);
        logger.debug("attribute - sortField: {}",  pageSortField);
        model.addAttribute("sortDirection", pageSortDirection);
        logger.debug("attribute - sortDirection: {}",  pageSortDirection);
        model.addAttribute("sortDirectionReverse", pageSortDirection.equals("asc") ? "desc" : "asc");
        logger.debug("attribute - sortDirectionReverse: {}", pageSortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("searchField", pageSearchField);
        logger.debug("attribute - searchField: {}",  pageSearchField);
        model.addAttribute("searchString", pageSearchString);
        logger.debug("attribute - searchString: {}",  pageSearchString);
        
        // Error on Add / Update. Comming here by a redirect  
        logger.debug("attribute - org.springframework.validation.BindingResult.pokemon: '{}'",  model.getAttribute("org.springframework.validation.BindingResult.pokemon"));

        // Pokemon Model attribute can be not empty if there is some error on Add / Update. Comming here by a redirect 
        if (!model.containsAttribute("pokemon")) {
            // Empty Pokemon Model attribute for Empty New and Edit modals included in home page at first display
            model.addAttribute("pokemon", new Pokemon());
        }
        logger.debug("attribute - pokemon: {}",  model.getAttribute("pokemon"));
        

        //model.addAttribute("test", "true");
        //logger.debug("attribute - test: {}",  test);

        return "home";
    }
    
    // DETAIL POKEMON
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable String id, Model model) {

        logger.info("/detail/{} - User {} ... ", id, service.getLoggedUserName());
        
        Pokemon pokemon = service.findById(new Long(id));

        logger.debug("Founded pokemon to display {} ", pokemon);
                
        model.addAttribute("pokemon", pokemon);
        
        return "detail"; 
        //return "ID: " + id;
    }    

    // CRUD POKEMON
    @RequestMapping("/get/{id}")
    @ResponseBody
    public Pokemon get(@PathVariable (name="id") long id) {
        
        logger.info("/get/{id} - User {} ", id, service.getLoggedUserName());

        Pokemon pokemon = service.findById(id);
        
        return pokemon;
    
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute("pokemon") Pokemon pokemon, BindingResult result, RedirectAttributes redirectAttributes) {
    
        logger.info("/add - User {} is creating Pokemon: {}",  service.getLoggedUserName(), pokemon);
        
        if(result.hasErrors()) {
            
            logger.info("Cannot add pokemon - Go back home");
            //model.addAttribute("modalNewHasErrors", true); // trap in home js to redisplay the right modal with errors
            //model.addAttribute("modalEditHasErrors", false);
            redirectAttributes.addFlashAttribute("toastTitle", "Error");
            redirectAttributes.addFlashAttribute("toastMessage", "Pokemon "+pokemon.getName()+" ("+pokemon.getNumber()+") not added");

            redirectAttributes.addFlashAttribute("modalNewHasErrors", true); // trap in home js to redisplay the right modal with errors
            redirectAttributes.addFlashAttribute("modalEditHasErrors", false);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.pokemon", result);
            redirectAttributes.addFlashAttribute("pokemon", pokemon);
            //redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            //redirectAttributes.addFlashAttribute("register", result);
            //redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.data", result);
            return "redirect:/home";
            //return "home";
        } else {
            service.save(pokemon);
            logger.info("Pokemon added successfully - Redirect home");
            redirectAttributes.addFlashAttribute("toastTitle", "Success");
            redirectAttributes.addFlashAttribute("toastMessage", ""+pokemon.getName()+" ("+pokemon.getNumber()+") added");
            
            return "redirect:/home";
        }
    
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("pokemon") Pokemon pokemon, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
    
        logger.info("/update - User {} is updating Pokemon: {} ... ", service.getLoggedUserName(), pokemon);
        
        if(result.hasErrors()) {
            logger.info("Cannot update pokemon  - Go back home");
            //model.addAttribute("modalNewHasErrors", false); // trap in home js to redisplay the right modal with errors
            //model.addAttribute("modalEditHasErrors", true);
            redirectAttributes.addFlashAttribute("toastTitle", "Error");
            redirectAttributes.addFlashAttribute("toastMessage", "Pokemon "+pokemon.getName()+" ("+pokemon.getNumber()+") not updated!");

            redirectAttributes.addFlashAttribute("modalNewHasErrors", false); // trap in home js to redisplay the right modal with errors
            redirectAttributes.addFlashAttribute("modalEditHasErrors", true);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.pokemon", result);
            redirectAttributes.addFlashAttribute("pokemon", pokemon);
            
            return "redirect:/home";
            //return "home";
        } else {
            service.save(pokemon);
            logger.info("Pokemon updated successfully - Redirect home");
            redirectAttributes.addFlashAttribute("toastTitle", "Success");
            redirectAttributes.addFlashAttribute("toastMessage", ""+pokemon.getName()+" ("+pokemon.getNumber()+") updated.");
        }
                
        return "redirect:/home";
    
    }
    
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable (name="id") long id, RedirectAttributes redirectAttributes) {
        
        logger.info("/delete/{id} - User {} ", id, service.getLoggedUserName());

        Pokemon pokemon = service.findById(id);

        logger.debug("Found pokemon to delete {} ", pokemon);

        service.delete(id);

        redirectAttributes.addFlashAttribute("toastTitle", "Success");
        redirectAttributes.addFlashAttribute("toastMessage", ""+pokemon.getName()+" ("+pokemon.getNumber()+") deleted");

        logger.info("Pokemon deleted successfully - Redirect home");
        
        return "redirect:/home";
    
    }    
    
    // Other Page 
    @GetMapping(path = "/help")
    public String help() {

        logger.info("/help - User {}", service.getLoggedUserName());

        return "help";
    }

    @GetMapping(path = "/login")
    public String login() {

        logger.info("/login - User: {}", service.getLoggedUserName());

        return "login";
    }

    // Monitoring Page : Without Template page (@ResponseBody) and with messages taken from translation label file
    @Autowired
    Messages messages;
    
    @GetMapping(path = "/monitoring")
    @ResponseBody
    public String monitoring() {

        logger.info("/monitoring - User: {}", service.getLoggedUserName());

        logger.debug("message.properties "+messages.get("monitoring.title"));
        
        return "<H1>"+messages.get("monitoring.title")+"</H1><HR>"+messages.get("monitoring.keySentence")+"...";
    }
    
    // Test Page : Simple Rest Controller which return HTML
    @GetMapping(path = "/test")
    @ResponseBody
    public String test() {

        logger.info("/test - User: {}", service.getLoggedUserName());

        return "<H1>Test</H1><HR>Test page is running...";

    }

}
