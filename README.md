# CDI LoadJsonFile - Interceptor

A simple CDI interceptor for Java EE 7. It loads json content file into a Java class.


### Examples

#### Loading a json file

A java class ```MyDummyClass``` annotated with ```@LoadJsonFile``` 

```java
@Singleton
@LoadJsonFile
public static class MyDummyClass {
    private String name;
    private int age;
    private boolean active;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isActive() {
        return active;
    }
}
```
it will be created with the information stored in ```MyDummyClass.json``` file

```json
{
  "name": "Name",
  "age": "29",
  "active": false
}
```

#### Loading a json file with different name

In case your json file is named differently to your java class you can specify the name in the annotation.

```java
@Singleton
@LoadJsonFile(fileName = "config.json")
public static class MyDummyClass {
    private String name;
    private int age;
    private boolean active;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isActive() {
        return active;
    }
}
```

and it will be created with the information stored in ```config.json```

```json
{
  "name": "Name",
  "age": "29",
  "active": false
}
```

### How do I get set up? ###

Fork this repository and build it. Once is done, add the dependency to your pom.xml project.

```xml
<dependency>
 <groupId>com.pam.loadjsonfile</groupId>
 <artifactId>loadjsonfile</artifactId>
 <version>1.0-SNAPSHOT</version>
</dependency>
```