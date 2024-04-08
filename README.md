# GeoGallery  

## To run application:
### user-service:
`mvn clean package` for jwt-utils  
`mvn clean package` for user-service'  
`java -jar user-service-0.0.1-SNAPSHOT.jar`  

#### Environment variables:  
`USER_SERVICE_PORT` -> variable to specify server port for user-service (`8080` by default)  
`AES_SECRET_KEY` -> secret key for handling password encryption (should contain 16-symbol A-Za-z string, e.g., `JwXdnlcDyzQjiYdy`)  
`JWT_SECRET_KEY` -> secret key for handling jwt processing (e.g., `93b762915b6a4ae48068e59621c2ef0f`)  

`application.yml` file also has `firebase.config` value, which is a reference for `json configuration file`  
Put your `json configuration file` in resources/static of user-service module.  

### front-end:  
Install Node.js: Visit the official Node.js [website](https://nodejs.org) and download the appropriate installer for your operating system.  
`npm install`.  
`npm start`.  

#### Environment variables:  
On the root level of module there's a `config.js` file.  
Change `apiUrl` value considering user-service's port.  

## Application description:  
### Stack:    
- #### Java for user-service and jwt-utils components.  
- #### React for front-end component.  
- #### Firebase as a cloud service for RealtimeDatabase, Authentication, Storage.  

### Application capabilities:  
#### For user:  
- Bind your images on any map position.  
- Manage user images  
- Add comments to every image  
#### For admin:  
- Manage users and their data  

### Application features:  
-Connecting to geographical map using leaflet.  
-Role separation using JWT tokens.  
-Using Firebase Cloud Service for storing data.  
