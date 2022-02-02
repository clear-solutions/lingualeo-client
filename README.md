[![Java CI with Gradle](https://github.com/clear-solutions/lingualeo-client/actions/workflows/gradle.yml/badge.svg)](https://github.com/clear-solutions/lingualeo-client/actions/workflows/gradle.yml)

# LingualeoClient

## _Client for adding words to the dictionary https://lingualeo.com_

[<img src="http://clear-solutions.ltd/wp-content/uploads/2020/09/logolidone-300x61.png" width="200"/>](http://clear-solutions.ltd)

> Client for interacting with the API of https://lingualeo.com site, which allows you to add
> to the dictionary on the site words with translation.

## REQUIREMENTS

#### [Java 17 Oracle Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

## HOW TO USE

### NECESSARILY

1. Create an instance of `LingualeoClient` class and pass in the constructor the login data to Lingualeo.com such as
   email and password;

```sh
LingualeoClient lingualeoClient = new LingualeoClient("email", "password");
```

2. Log in to the site using the `auth` method;

```sh
lingualeoClient.auth();
```

### AFTER

3. You can use the `getTranslate` method to get the translation of the word;

```sh
lingualeoClient.getTranstale("word");
```

4. To add a word to the dictionary, you must enter a word and its translation in the `addWord` method;

```sh
lingualeoClient.addWord("word", "translate");
```


