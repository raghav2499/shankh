# Darzee Boutique Management System

## Setup Guidelines

Run in development mode in Intellij idea etc by adding the following line
-Dproperty.environment=dev

#### Build
mvn clean package

#### Run
java  -jar target/shankh-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

#### Documentation
Api documentation can be found at : [TBA] 

### Branching and Commit Guidelines

Syntax to be followed for forking out new branches -

- {JIRAId}/{Parentbranch}/{Your name or Feature or any custom tag}
- Ex Situation      : New branch from master for fixing a bug with jira id ORG-123  
  Suggestive Branch : ORG-123/master/customTag

### Commit Syntax
- All commit messages should have a jira id in it.
- Also it is adviced to put the reason of making the change in the message
- Adding your name in the message will help you to identify your commit (ex: in jenkins changes page)
- Preferred separator in the message is '|'
- Ex: git commit -m "ORG-123|Raghav|Making this commit to provide an example for the syntax that can be followed"

#### Deployment guideline
https://docs.google.com/document/d/1fe4pe_cLiMvzcJV8vsrADO2_GnyjP263xD6eIgN6DMs/edit
