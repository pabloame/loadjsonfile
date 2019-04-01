[![Build Status](https://api.travis-ci.org/pabloame/loadjsonfile.svg?branch=master)](https://travis-ci.org/pabloame/loadjsonfile)

# LoadJsonFile

Load Json content into a POJO class.

This library is meant to be used as a dependency for a project built on 
a Java EE Application Server. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 
See package for notes on how to package the project and use it as dependency.

## Dev Prerequisites

* Maven
* Java 9 JDK
* Git

## Installing

A step by step series of examples that tell you how to get a development env running

```
git clone https://github.com/pabloame/loadjsonfile.git
```

## Running the tests

You can run unit test with the following command:

```
mvn test
```


## Package

To package the library just need to run:

```
mvn package
```

A jar file will be generated in target folder, you can add it as dependency to your project.

In case you want to install it into you local maven repository use:

```
mvn install
```

and then add it as:

```
<dependency>
 <groupId>com.pam.loadjsonfile</groupId>
 <artifactId>loadjsonfile</artifactId>
 <version>1.0-SNAPSHOT</version>
</dependency>
```


## Use cases

### Loading a json file

It loads a java class ```MyDummyClass``` annotated with ```@LoadJsonFile``` with content from a json file with same class name. 

#### Example

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

### Loading a json file with different name

In case your json file is named differently to your java class you can specify the name in the annotation.

#### Example

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


## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details

