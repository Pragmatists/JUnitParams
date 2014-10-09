# JUnitParams

[![Build Status](https://secure.travis-ci.org/Pragmatists/JUnitParams.png)](http://travis-ci.org/Pragmatists/JUnitParams)

*Parameterised tests that don't suck*

## Example

``` java
  @Test
  @Parameters({"17, false", 
               "22, true" })
  public void personIsAdult(int age, boolean valid) throws Exception {
    assertThat(new Person(age).isAdult(), is(valid));
  }
```

## About
JUnitParams project adds a new runner to JUnit and provides much easier and readable parametrised tests for JUnit >=4.6.

Main differences to standard JUnit Parametrised runner:

* more explicit - params are in test method params, not class fields
* less code - you don't need a constructor to set up parameters
* you can mix parametrised with non-parametrised methods in one class
* params can be passed as a CSV string or from a parameters provider class
* parameters provider class can have as many parameters providing methods as you want, so that you can group different cases
* you can have a test method that provides parameters (no external classes or statics anymore)
* you can see actual parameter values in your IDE (in JUnit's Parametrised it's only consecutive numbers of parameters):

## Quickstart

JUnitParams is available as Maven artifact:
```
<dependency>
  <groupId>pl.pragmatists</groupId>
  <artifactId>JUnitParams</artifactId>
  <version>1.0.3</version>
</dependency>
```

If you want to see just one simple test class with all main ways to use JUnitParams see here:
https://github.com/Pragmatists/junitparams/tree/master/src/test/java/junitparams/usage

You can also have a look at [Wiki:Quickstart](https://github.com/Pragmatists/junitparams/wiki/Quickstart)

## News

Latest news are available in our [Wiki](https://github.com/Pragmatists/junitparams/wiki/)

**Note**: We are currently moving the project from Google Code to Github. Some information may still be accessible only at https://code.google.com/p/junitparams/
