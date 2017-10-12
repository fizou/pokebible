package com.pokebible;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Controllers {
	
	@RequestMapping("/")
    String root() {
		Tracer.warn("Controllers - root");
        //return "index.html";
        return "home";
    }
	@RequestMapping("/home")
    String home() {
		Tracer.warn("Controllers - home");
		
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			Tracer.warn(auth.getName() +" User");
		} else {
			Tracer.warn("Anonymous User");
			
		}
		
		
        return "home";
    }
	@RequestMapping("/help")
    String about() {
		Tracer.warn("Controllers - help");
        return "help";
    }

	@RequestMapping("/login")
    String login() {
		Tracer.warn("Controllers - login");
        return "login";
    }
	
	@RequestMapping("/testRunning")
    @ResponseBody
    String testRunning() {
		Tracer.warn("Controllers - testRunning");
        return "<H1>Pokemon Bible</H1><HR/>is running well...";
    }

	
}
