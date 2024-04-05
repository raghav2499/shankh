

---

# Darzee Boutique Management System

## Setup Guidelines

Run in development mode in IntelliJ IDEA, etc. by adding the following line:
```
-Dproperty.environment=dev
```

#### Clean Previous Build
```bash
mvn clean package
```

### Build (New Build)
```bash
mvn package
```

#### Run
If you're using IntelliJ IDEA, run the following command:
```bash
java -jar target/shankh-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```
Otherwise, use this command:
```bash
java -jar target/shankh-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev -Dproperty.environment=dev
```

#### Documentation
API documentation can be found at: [TBA]

### Branching and Commit Guidelines

Syntax to be followed for forking out new branches:
- `{JIRAId}/{Parentbranch}/{Your name or Feature or any custom tag}`
- Example situation: New branch from master for fixing a bug with JIRA ID ORG-123
  Suggestive branch: `ORG-123/master/customTag`

### Commit Syntax
- All commit messages should have a JIRA ID in them.
- It's also advised to put the reason for making the change in the message.
- Adding your name in the message will help you identify your commit (e.g., in Jenkins changes page).
- Preferred separator in the message is `|`
- Example: `git commit -m "ORG-123|Raghav|Making this commit to provide an example for the syntax that can be followed"`

#### Deployment Guidelines
For detailed deployment instructions, refer to the [deployment guideline document](https://docs.google.com/document/d/1fe4pe_cLiMvzcJV8vsrADO2_GnyjP263xD6eIgN6DMs/edit). Make sure your README.md is well-organized, structured, and clearly written. 🚀

---
