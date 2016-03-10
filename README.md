# README #

Welcome to the HSPC Reference API!  The HSPC Reference API server contains a FHIR resource server.  The project is a composition of five libraries that are available in this repository.

## reference-api-smart-support ##
[reference-api-smart-support](https://bitbucket.org/hspconsortium/reference-api-mysql) adds SMART launch endpoints to a FHIR resource server conformance statement.

## reference-api-oauth2 ##
[reference-api-oauth2](https://bitbucket.org/hspconsortium/reference-api-oauth2) configures OAuth2/OpenID Connect security for a FHIR resource server.

## reference-api-mysql ##
[reference-api-mysql](https://bitbucket.org/hspconsortium/reference-api-mysql) configures a MySQL FHIR resource repository to be used by the reference-api-fhir library.

## reference-api-fhir ##
[reference-api-fhir](https://bitbucket.org/hspconsortium/reference-api-fhir) is an extension of [Hapi FHIR](http://jamesagnew.github.io/hapi-fhir/) that includes support for SMART launch.

## reference-api-webapp ##
A deployable web application that includes configuration of a FHIR server (reference-api-fhir, reference-api-mysql) for OAuth2 (reference-api-oauth2) and SMART launch (reference-api-smart-support).  The reference-api-webapp may be used as an example for a custom HSPC FHIR Resource server.

## How do I get set up? ##

### Preconditions ###
    For secured configuration, the reference-api server must register a client with the reference-authorization server.
    From MySQL
    mysql> use oic;
    mysql> source {install path}/reference-api-mysql/src/main/resources/db/openidconnect/mysql/resource-server-client.sql;
    * note this script is included with the complete installation of the reference-impl (optional)

### Build and Run ###
    mvn clean install
    deploy reference-api-webapp/target/hspc-reference-api.war to Tomcat

### Build and Run as Spring Boot ###
    modify reference-api-webapp/pom.xml to spring-boot-starter-tomcat (not "provided")
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </dependency>
    mvn clean install
    java -jar reference-api-webapp/target/hspc-reference-api.war

### Verify ###
* http://localhost:8080/hspc-reference-api/data/metadata

## Open Mode ##
When the HSPC Reference API server is run in open mode, no security is applied.  This is very convenient for development, allowing resources to be read and written without authentication.  See reference-api-webapp/src/main/resources/application.yml.
* hsp.platform.api.security.mode=open

### Sample Operations ###
* http://localhost:8080/hspc-reference-api/data/Patient
* http://localhost:8080/hspc-reference-api/data/Observation

## Secured Mode ##
When the HSPC Reference API server is run in secured mode, authentication is required for most endpoints with the exception of the conformance statement.  See reference-api-webapp/src/main/resources/application.yml.
* hsp.platform.api.security.mode=secured

## Configuration ##

See reference-api-webapp/src/main/resources/application.yml for an initial listing of properties that may be overridden. 

|Property | Default Value | Notes
|---|---|---|
| server.contextPath | /hspc-reference-api |  |
| hsp.platform.api.security.mode | open | Options: open, secured |
| hsp.platform.api.oauth2.clientId | hsp_resource_server | OAuth client id by which the reference-api server uses to orchestrate SMART launch |
| hsp.platform.api.oauth2.clientSecret | secret | OAuth client secret |
| hsp.platform.api.oauth2.scopes | openid,launch,smart/orchestrate_launch | OAuth client scopes |
| hsp.platform.api.fhir.db.driver | com.mysql.jdbc.Driver | MySQL database configuration |
| hsp.platform.api.fhir.db.url | jdbc:mysql://localhost:3306/hapi_pu?autoReconnect=true | MySQL database configuration |
| hsp.platform.api.fhir.db.username | root | MySQL database configuration |
| hsp.platform.api.fhir.db.password | password | MySQL database configuration |
| hsp.platform.api.fhir.db.persistenceUnitName | HAPI_PU | Database schema for FHIR resources |
| hsp.platform.api.fhir.hibernate.dialect | org.hibernate.dialect.MySQL5InnoDBDialect | MySQL database configuration |
| hsp.platform.api.fhir.terminologyEndpointURL | fhir2.healthintersections.com.au/open | HSPC API Server proxies terminology calls to this server |
| hsp.platform.authorization.url | http://localhost:8080/hspc-reference-authorization | OpenID Connect token issuer |
| hsp.platform.authorization.authorizeUrl | http://localhost:8080/hspc-reference-authorization/authorize | OpenID Connect authorization endpoint |
| hsp.platform.authorization.tokenUrl | http://localhost:8080/hspc-reference-authorization/token | OpenID Connect token endpoint |
| hsp.platform.authorization.tokenCheckUrl | http://localhost:8080/hspc-reference-authorization/introspect | OpenID Connect token introspection endpoint |
| hsp.platform.authorization.userinfoUrl | http://localhost:8080/hspc-reference-authorization/userinfo | OpenID Connect userinfo endpoint |
| hsp.platform.authorization.smart.launchUrl | http://localhost:8080/hspc-reference-authorization/Launch | http://docs.smarthealthit.org/authorization/scopes-and-launch-context/ |
| hsp.platform.authorization.smart.authorizeExtensionUrl | http://localhost:8080/hspc-reference-authorization/Launch | http://docs.smarthealthit.org/authorization/scopes-and-launch-context/ |
| hsp.platform.authorization.smart.registrationEndpointUrl | http://localhost:8080/hspc-reference-authorization/register | http://docs.smarthealthit.org/authorization/scopes-and-launch-context/ |
| hsp.platform.authorization.smart.urisEndpointExtensionUrl | http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris | URI for the conformance statement extension specifying the SMART endpoints |
| hsp.platform.messaging.url | http://localhost:8080/hspc-reference-messaging | HSPC Reference API server sends subscription messages to the HSPC Reference Messaging service for processing |
| hsp.platform.messaging.subscriptionSupport.enabled | true | Enables or disables subscription support.  Values: true, false |
| hsp.platform.messaging.subscriptionSupport.subscriptionEndpoint | http://localhost:8080/hspc-reference-messaging /subscription | Endpoint for sending and receiving Subscription resources |
| hsp.platform.messaging.subscriptionSupport.resourceEndpoint | http://localhost:8080/hspc-reference-messaging/resource | Endpoint for submitting FHIR resources to be matched using the subscription engine |

## Where to go from here ##
https://healthservices.atlassian.net/wiki/display/HSPC/Healthcare+Services+Platform+Consortium