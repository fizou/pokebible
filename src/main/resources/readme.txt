Readme.txt

Version

Release 1.0.13-SNAPSHOT :
- Add CRUD interface for admin
- Create a custom unique control when insert pokemon number 
- Control on Type1 presence required
- Put js instead CDN (webjars possibility rejected) and Migrate to last framework version for jquery / boot strap 4 / awesomefont
- Toaster to display successful update
- Migrate REST and Swagger to springfox-boot-starter 3.0
- Migrate Security to 5
- Add Service class instead direct access to repository for other class than controller

- Log cutomization : Rework work level display and format  
- Refactoring code 

Release 1.0.14-SNAPSHOT :

- No More Type Key display in Upcase on interface. Type Label is used. (the Key Type in UPCASE is only used in the Database)
- Put Field in Red in case of error
- Add Pagination and Sort
- Fix search
- Fix Put transparency instead white background on pictos
- Display Toaster in case of error 
- Fix redirection in case of insert / update
- Fix context-path of element 
- Retest in tomcat mode
- HealthCheck with actuator

- Refactoring code 

To do: 

manage FR & EN language

Statistics par type reprendre CRMDashboard + Webservices.

Control with Jquery validate and or REST

Page "About" Hardcoded on controller side with pokebible image in remplacmeent of monitoring


Canceled:
- JSP Vue instead Theamleaf in NEW project (Not the future)
- Add type image in new/edit type combo before label (Not compatible to all browser without js)
- Datatable with API (Do a new Projet)

