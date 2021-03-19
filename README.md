# The Task (Due: 2021-06-21 08:00:00)

Your task is to implement a simplified application that is able to store, update, delete, visualize (and so on) simple POJO's (Plain old Java objects). You task is to achieve this by using a neo4j graph database to model, store and visualize your POJO's. You have to build a REST-API on top of this database, which e.g. must enable users to upload new or update existing POJO's in the database. You only have to provide the REST-API calls to interact with your API and you don't need to add an additional user interface for communicating in a more user friendly way (and you won't get any extra points for that either ;)).

In order to keep the project simple, we will not use client authentication and authorization. In real life projects you would – of course – implement those features.
The stories are listed in the preferred order of implementation.

### User Stories (120 points max out of 100 points)
Each story gives you up to 5 to 25 points max (depending on other quality criteria)

#### Story 1 (25 pts)
As a developer I want to add new POJO's to my model I'm currently working on and add and visualize the dependencies and attributes of this class.

*acceptance criteria*
*  the POJO have to be uploaded in bytecode format - to be more precise the binary data of the classfile representing the class have to be uploaded
*  at least the following attributes of a class have to be stored in the neo4j database
   1. classname
   2. package name
   3. all attributes (incl. their visibility modifier) of the class and their corresponding data types
   4. parent classes
   5. implemented interfaces
*  parent classes and implemented interfaces shall be modelled as relationships in neo4j 
*  the corresponding data types of attributes shall also be modelled as relationships in neo4j
*  be aware, that you might have to create empty hulls for not yet imported classes that are parent classes or data types of attributes
*  if the class/POJO was already added to the model, don't add the class/POJO again and return an appropiate error to the caller. Be aware that empty hulls you created to model relationships may be overridden.
* for primitives like int, double, boolean, ... use the corresponding wrapper type in your model
* you may omit generic datatypes and arrays in this task

#### Story 2 (10 pts)
As a developer I want to remove a POJO from my model.

*acceptance criteria*
*  build a request for deleting a POJO
*  if the POJO isn't existing - return an appropiate error to the caller
*  if the POJO is existing and not referenced - delete the POJO from your model
*  if the POJO is existing and referenced - only delete the attributes of the modelled POJO (convert it to an empty hull)

#### Story 3 (10 pts)
As a developer I want to add multiple POJO's at once to my model I'm currently working on and add and visualize the dependencies and attributes of all these classes.

*acceptance criteria*
*  the POJO's have to be uploaded in a JAR file
*  all classes in the JAR file are loaded as POJO's into the database
*  for simplification purposes you don't need to take care of nested JAR files
*  if a class was already added to the model, don't add any of the classes in the uploaded JAR file and return an appropiate error to the caller
*  all attributes mentioned in Story 1 have to be stored in the database

#### Story 4 (10 pts)
As a developer I want to export the currently stored POJO's in order to have a snapshot of the work, that I can reimport in cases of data loss or working in parallel on different projects. 

*acceptance criteria*
*  output must be reimportable into the application 
*  export format must be JSON or XML
*  if I reimport the exported data, the graph must be identical to the one that I exported 
*  a JSON/XML schema is provided for documentation

#### Story 5 (10 pts)
As a developer I need to import preexisting POJO's in order ...
*  ... to inspect POJO's when I don't have the original class files of the POJO's of interest
*  ... to be able to quickly load a different set of POJO's when I work on multiple projects in parallel

*acceptance criteria*
*  the database gets cleared before importing the data
*  the model data of the imported file (JSON or XML) is stored in the database
*  the written import has at least square maturity regarding regarding the import time <!-- is square maturity explained? -->
*  JSON/XML files with incorrect syntax (e.g. missing parentheses, invalid character) are rejected with an apropriate error message 
*  JSON/XML files with incorrect schema (e.g. undefined field name) are rejected with an appropriate error message

#### Story 6 (5 pts)
As a developer I want to know which classes (POJO) reside in a package of my model.

*acceptance criteria*
* a list of all classes (classname and package name) directly located in the package or a subpackage shall be returned (recursive!)
* if the package is unknown or does not contain any classes return an empty list to the caller

#### Story 7 (5 pts)
As a developer I want to add a new POJO to my model, without providing a Java-class in binary format.

*acceptance criteria*
* essentially the task is to create a named "empty hull" by specifying a list of parameters
* mandatory parameters: class name, package name
* if a provided parameter is invalid/malformed, return an appropriate error message and don't change the model
* if POJO is already existing in model, return an appropriate error message and don't change the model
* the added POJO shall not have any attributes after the POJO was added to the model


#### Story 8 (5 pts)
As a developer I want to add a new attribute to an existing POJO of my model.

*acceptance criteria*
* the type of the new attribute must be provided 
* the name of the attribute must be provided
* the visibility of the attribute must be provided
* if a provided parameter is invalid/malformed, don't add the attribute and return an appropriate error message
* if the attribute is already existing in the POJO, don't add it and return an appropriate error message


#### Story 9 (5 pts)
As a developer I want to remove an existing attribute of a POJO.

*acceptance criteria*

* the classname, package name and name of the attribute to remove must be provided
* if class or attribute is not existing, return an appropriate error
* if all provided input parameters are valid, remove the attribute from the POJO

#### Story 9 (10 pts)
As a developer I want to get attributes and statistics for a single POJO.

*acceptance criteria*
*  these attributes have to be returned:
   1. classname
   2. package name
   3. number of attributes
   4. parent classname
   5. list of implemented interfaces
   6. number of direct subclasses
   7. number of attributes that have the corresponding data type
   8. number of classes in the same package
   9. number of classes with the same name
*  the response format must be human readable 
*  if the class/POJO couldn't be found - return an appropiate error to the caller


#### Story 11 (10 pts)
As a developer I want to generate simple Java code for the POJO's stored in the database.

*acceptance criteria*

*  the generated java file compiles
*  importing the generated class file generates an identical database entry as before
*  the generated could should be well formatted (e.g. good use of indentations (doesn't need to be perfect) and line breaks)
*  no code is generated for empty hulls  

#### Story 12 (5 pts)
As a developer I want to add a new attribute of type java.util.List to an existing POJO of my model. The generic type of the list is mandatory.

e.g.: private java.util.List<String> attr;

*acceptance criteria*
* the type of the new attribute must be provided 
* the name of the attribute must be provided
* the visibility of the attribute must be provided
* the generic type of the elements that may be stored in the list must be provided
* if a provided parameter is invalid/malformed, don't add the attribute and return an appropriate error message
* if the attribute is already existing in the POJO, don't add it and return an appropriate error message
* the generated code for these attributes contain the generic type information

#### Story 13 (10 pts)
As a developer I want to be sure that my application is secured against bytecode exploits in classes / JAR-Files that are getting uploaded to the application.

*acceptance criteria*
* malicous code get's not executed
* provide a unit test to prove it - at least for one attack scenario


# Rules / Restrictions
### Groups
We create user stories for all groups. Every group gets the same amount and the same set of stories. A group consists of 1 or 2 students.

### Grading
Your grade is composed as follows:
- We will grade the code and the program you submitted. (at least 80% of the final grade)
- You will have to give a final presentation (about 20-30 minutes, at most 20 % of the final grade), where you... 
... have to present your developed application.
... have to answer questions regarding your project and the submitted code.
**Code have to be submitted to your Gitlab project - neither on Github, through e-Mail, on a USB-device nor printed out.**

## Technology
As you implement the stories for fulfilling the requirements you have to use the following frameworks, tools & technologies - these are must haves:
* neo4j (as persistence layer)
* Spring Boot 
* Java 15 (you may not use any Java 15 features at all, but your code must compile using Java 15) 
* IntelliJ
* gradle as the only build tool (not the IntelliJ internal build)
* REST
* Git (using this GitLab-Repository)

### Quality
* **Submitted code must have a unit test line coverage of about 20 % which has a mutation test coverage of 80%.**
* Submitted code must be well formatted - therefore use the provided formatter settings for the IntelliJ IDEA IDE (Profile Default). Use the Save Actions Plugin for IntelliJ.
* Submitted code must compile, executing the tests must succeed and a runnable spring boot application must be the artifact of your submitted project. (for clarification: running gradle assemble & bootJar and gradle bootRun must succeed)
* You should use the SonarLint plugin for IntelliJ for checking your code for error, code smells and security issues. Your code shall have no issues or only irrelevant issues (be prepared for explaining the irrelevance in the presentation)
* Document your code using javadoc where needed (classes, complex methods, sometimes maybe inline comments)
* Handle errors and exceptions in an appropriate manner and avoid passing stacktraces or internal details to the caller of your REST API

### Naming
* Naming constants: Uppercase letters, numbers and underscore only 
* Naming Variables, member variables & method names: Starting with a lowercase letter then camelcase notation
* Naming Classes: Starting with a uppercase letter then camelcase notation
* Naming packages: only lowercase letters, avoid default package where possible

### Structure / Architecture
Submitted code must be structured, e.g. separate classes into different packages, build at least a service-, persistence- and a presentation (REST) layer.
Keep the principles DRY, KISS, POLS and YAGNI in mind when you develop your application.

**You must document the chosen architecture (package structure, layering, ...) and your submitted code must conform to the rule defined by this documentation**

# REST-API
**You must provide a documentation for the REST-API of your submitted application, where all endpoints are listed, documented and explained. You may use Swagger-Annotations to fulfill this task.**

# Configurations & Links
### Links
[Neo4j database download](https://neo4j.com/download-center/#releases)

[Cypher Manual](https://neo4j.com/docs/cypher-manual/current/)

[Spring Data and neo4j](https://spring.io/guides/gs/accessing-data-neo4j/)

[Git cheat sheet](https://ndpsoftware.com/git-cheatsheet.html)

[Consuming a REST service using Spring's RestTemplate (1)](https://howtodoinjava.com/spring-restful/spring-restful-client-resttemplate-example/)

[Consuming a REST service using Spring's RestTemplate (2)](https://www.baeldung.com/rest-template)

[ASM bytecode analysis framework](https://asm.ow2.io/index.html)




