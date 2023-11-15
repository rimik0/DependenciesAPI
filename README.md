# Mini DependenciesAPI

This is a small api for your plugin that sets up hiccups via a direct link to a file.

## Exempe

Example code for adding envs without config file

```java
new DependenciesAPI()
        .addDependence("https://github.com/ScarletRedMan/FormAPI/releases/download/2.2/FormAPI.jar", "2.1-SNAPSHOT")
        .addDependence("https://cloudburstmc.org//resources/simplescoreboards.195/version/2273/download", "2.6.0")
        .build;
```

Example of code for adding envs from config file

```java
new DependenciesAPI()
        .loadDependenciesFromFile(getResource("dependencies.yml"))
        .build;
```

## How to fill in the config.
The config should be created in resources. And then fill it in like this example.

```yaml
"FormAPI": 
  version: "1.0.0-SNAPSHOT"
  url: "https://github.com/ScarletRedMan/FormAPI/releases/download/2.2/FormAPI.jar"
"SimpleScoreboards-2.6.0":
  version: "1.0.1"
  url: "https://cloudburstmc.org//resources/simplescoreboards.195/version/2273/download"
```

## Maven example

```xml
<dependency>
    <groupId>ru.dependencies.api</groupId>
    <artifactId>DependenciesAPI</artifactId>
    <version>dev</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/libs/DependenciesAPI-dev.jar</systemPath>
</dependency>
```

## IMPORTANT

If you already have a dependency installed, but the plugin needs a different version of the dependency, it will not overwrite it, but will only display message.

