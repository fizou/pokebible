Readme.txt

REST API:
http://localhost:8085/api/generateToken?username=user&password=password
http://localhost:8085/api/pokemons
http://localhost:8085/api/pokemons/search/pokemons
Actuator:
http://localhost:8085/actuator/Health
http://localhost:8085/actuator/metrics
http://localhost:8085/actuator/prometheus

Done:

Release 1.0.13-SNAPSHOT :
- Add CRUD interface for admin
- Create a custom unique control when insert pokemon number 
- Control on Type1 presence required

- Put js in project instead CDN (webjars possibility rejected) and Migrate to last framework version for jquery / boot strap 4 / awesomefont
- Add Toaster to display successful update

- Migrate REST and Swagger to springfox-boot-starter 3.0
- Migrate Security to 5

- Add Service class instead direct access to repository for other class than controller
- Log cutomization : Rework work level display and format  

Release 1.0.14-SNAPSHOT :

- Add Pagination and Sort and Fix search
- Add HealthCheck with actuator are available

- No More Type Key display in Upcase on interface. Type Label is used. (the Key Type in UPCASE is only used in the Database)
- Put Field in Red in case of error
- Fix transparency instead white background on pictos
- Display Toaster in case of error 
- Fix redirection model attribute in case of insert / update

- Fix context-path of element 
- Retest in tomcat mode

- Refactoring code 

Release 1.0.15-SNAPSHOT :

- Secure REST API with JWT Token and role (only admin can update pokemon via API)
- Create Rest Controller with 1 custom findPokemon method
- Add DATABASE and API Counter metrics to healthcheck, actuator metrics, and prometheus

- Suppress Pokemon<pictureHtmlTag> method
- Suppress Pokemon<setType> method

To do: 

Same Control on Standard API than WEB  
Do not expose all custom repository

Increment read, update, delete metrics for all operation (controler, API)
REST Migrate access to repository to service 
Add Control constraint on Type 1!= Type2  

No more type in upcase including database ?

manage FR & EN language

Statistics par type reprendre CRMDashboard + Webservices.

Control with Jquery validate and or REST 
  https://www.javacodegeeks.com/2012/02/spring-mvc-and-jquery-for-ajax-form.html
  https://stackoverflow.com/questions/56344495/remote-ajax-call-from-jquery-validate-to-check-email-validity-not-working

Page "About" Hardcoded on controller side with pokebible image in remplacmeent of monitoring

       LOGGER.info("  _   _                      _    ____ ___ ");
        LOGGER.info(" | | | | ___ _ __ ___       / \\  |  _ \\_ _|");
        LOGGER.info(" | |_| |/ _ \\ '__/ _ \\     / _ \\ | |_) | | ");
        LOGGER.info(" |  _  |  __/ | | (_) |   / ___ \\|  __/| | ");
        LOGGER.info(" |_| |_|\\___|_|  \\___/   /_/   \\_\\_|  |___|");
        LOGGER.info("                         Powered by Quarkus");
    }

Pokebible API will be access by an front office application to do battle (Find Random pokemon to Battle only based on Type table, Result)
Do it in Spring boot first, Then port it in angular and then port it Quarkus 123 or 1 3 2 


PostPone:
- Datatable with API (Do it a new Projet)
- JSP Vue instead Theamleaf in NEW project (Not the future)
- Add type image in new/edit type combo before label (Not compatible to all browser without js)
- NOT RECOMMENDED Suppress path in type1PictureUrl pictureUrl ? or put complete link type1PictureUrl pictureUrl for REST
    

