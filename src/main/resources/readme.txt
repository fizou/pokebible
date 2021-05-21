Readme.txt

+++ Quick Links:

Admin web site:
http://localhost:8085/home

H2 console:
http://localhost:8085/h2-console

Actuator Metrics:
http://localhost:8085/actuator/Health
http://localhost:8085/actuator/metrics
http://localhost:8085/actuator/prometheus

Swagger:
http://localhost:8085/swagger-ui
http://localhost:8085/v2/api-docs

Rest Api:
http://localhost:8085/api/generateToken?username=user&password=password
http://localhost:8085/api/pokemons


+++ Release Note:

*** Release 1.0.13-SNAPSHOT : 

* Add CRUD interface for admin
* Create a custom unique control when insert pokemon number 
* Control on Type1 presence required

- Put js in project instead CDN (webjars possibility rejected) and Migrate to last framework version for jquery / boot strap 4 / awesomefont
- Add Toaster to display successful update

- Migrate REST and Swagger to springfox-boot-starter 3.0
- Migrate Security to 5

- Add Service class instead direct access to repository for other class than controller
- Log cutomization : Rework work level display and format  


*** Release 1.0.14-SNAPSHOT :

* Add Pagination, Sort and Fix search
* Add HealthCheck page with actuator 

- No More Type Key display in Upcase on interface. Type Label is used. (the Key Type in UPCASE is only used in the Database)
- Put Field in Red in case of error
- Fix transparency instead white background on pictos
- Display Toaster in case of error 
- Fix redirection model attribute in case of insert / update

- Fix context-path of element 
- Retest in tomcat mode


*** Release 1.0.15-SNAPSHOT :

* Secure REST API with JWT Token and role (only admin can update pokemon via API)
* Add counter metrics to healthcheck, actuator metrics, and prometheus

- Create Rest Controller with 1 custom findPokemon method

- Remove Pokemon<pictureHtmlTag> method
- Remove Pokemon<setType> method


*** Release 1.0.16-SNAPSHOT :

FEATURE:
* Add search by Name, Number, Type1, Type2 and Type (Type1 or Type2)
* Add same data Control on Rest API than WEB Interface 

TECH:
- Overide all Standard API with our own method in RestController
- Add JWT Token authentification to swagger interface
- Elimitate /api/profile and Controller /api/search from swagger interface
- Remove findPokemon from Rest Controller 
- Add an /api/test Method
- Add pagination, size, sort, filter on GET /api/pokemons 
- Add API REST Error management
- Add API REST Standard JSON message

ISSUES:
- Fix Click on search without sort 
- Fix No API display if context-path is set to /pokebible (tomcat run)

REFACTORING:
- Migrate all access to repository in service 
- Comment all Classes 
- Rework DEBUG/INFO information 


+++ Backlog: 

FEATURE:
- Add Control constraint on Type 2!= Type1  
- Manage FR & EN language
- Page Statistics par type reprendre CRMDashboard + Webservices.
- Page "About" Hardcoded on controller side with pokebible image in remplacmeent of monitoring with copyright fizou
- Page "Fight" on controller side 
- Page Stats on fight
- Pokebible API will be access by an front office application to do battle (Find Random pokemon to Battle only based on Type table, Result)
      Do it in Spring boot first, Then port it in angular and then port it Quarkus 123 or 1 3 2 
- Add PC / ATT / DEF / ENDU
- Add GENERATION and Evolution

TECH:
- Control with Jquery validate and or REST 
  Create control webservice 
  https://www.javacodegeeks.com/2012/02/spring-mvc-and-jquery-for-ajax-form.html
  https://stackoverflow.com/questions/56344495/remote-ajax-call-from-jquery-validate-to-check-email-validity-not-working
- REST enhancement
  // DONE Rename PokemonRestApiApi en PokemonRest
  // Rename PokemonRes en Pokemon
  // add findbynumberNumber ???? ===> searchFilter / searchfield (name, number)
  // add searchNumber ????
  // add fight method...
- protect web controller by role ? Done or not ?
- Migrate 1 control in Service to see ?
- Increment detail metrics for read, update, delete for all database operation
- Logo at launch 
        LOGGER.info("  _   _                      _    ____ ___ ");
        LOGGER.info(" | | | | ___ _ __ ___       / \\  |  _ \\_ _|");
        LOGGER.info(" | |_| |/ _ \\ '__/ _ \\     / _ \\ | |_) | | ");
        LOGGER.info(" |  _  |  __/ | | (_) |   / ___ \\|  __/| | ");
        LOGGER.info(" |_| |_|\\___|_|  \\___/   /_/   \\_\\_|  |___|");
        LOGGER.info("                         Powered by Quarkus");

ISSUES:
bug: http://localhost:8085/api/profile/pokemons proteger par token (Ne pas publier profile et comprendre a quoi çà sert)

REFACTORING
- move CustomResponseAttributes format method to APIResponse ?
- Eliminate API Expception ?


+++ Postpone out of Backlog:

- Datatable with API (Do it a new Projet)
- JSP Vue instead Theamleaf in NEW project (Not the future)
- Add type image in new/edit type combo before label (Not compatible to all browser without js)
- NOT RECOMMENDED Suppress path in type1PictureUrl pictureUrl ? or put complete link type1PictureUrl pictureUrl for REST
    

