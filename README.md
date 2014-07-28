# JUnitParams - *Parameterised tests that don't suck*
---

**Note**: We are currently moving the project from Google Code to Github. Some information may still be accessible only at https://code
.google.com/p/junitparams/

## About
JUnitParams project adds a new runner to JUnit and provides much easier and readable parametrised tests for JUnit >=4.6.

Main differences to standard JUnit Parametrised runner:

more explicit - params are in test method params, not class fields
less code - you don't need a constructor to set up parameters
you can mix parametrised with non-parametrised methods in one class
params can be passed as a CSV string or from a parameters provider class
parameters provider class can have as many parameters providing methods as you want, so that you can group different cases
you can have a test method that provides parameters (no external classes or statics anymore)
you can see actual parameter values in your IDE (in JUnit's Parametrised it's only consecutive numbers of parameters):

## Quickstart

If you want to see just one simple test class with all main ways to use JUnitParams see here: https://code.google.com/p/junitparams/source/browse/src/test/java/junitparams/usage/Samples_of_Usage_Test.java

## News
* Jul 28, 2014: Moved to Github!
* Jun 23, 2013: 1.0.2 release, with a fix to handling primitive wrapper classes (like Integer) for parameters passed in the annotation and
 the Usage Tutorial test (line below).
* Jun 10, 2013: Usage tutorial in one simple test class: https://code.google.com/p/junitparams/source/browse/src/test/java/junitparams/usage/Samples_of_Usage_Test.java
* Apr 04, 2013: Release 1.0.1 Converters can now be used for parameters provided in methods; carriage return (\r) can be used in parameters' values
* Mar 13, 2013: Release 1.0.0 JavaDocs updated, you can see how to use all the new features there. We're also compliant with the recent JUnit 4.11.
* Jan 13, 2013: Release 0.9.0 - Preparing for 1.0 release. If you'd like something to be included before that, raise an issue please. New features: param converters, now you can have any parameter value automatically converted to a form expected by your tests. See ParamsConvertersTest in tests for examples. File parameters - now you can have your params read directly from external resource, CSV files supported by default. Enums - now param types can also be enums. Plus some issues fixed, to keep the bug count level as close to zero as my spare time allows.
* Jul 18, 2012: There's an article by Vladimir Belorusets on JUnitParams in the recent STQA magazine - see http://www.softwaretestpro.com/Download/Publication/356
* May 11, 2012: Release 0.5.0 - separators in param values properly handled now (thanks for impl to Vitaliy Oliyjyk); can now use data providing methods from external classes (thanks for idea to Vladimir Belorusets); simplified creation of objects in data providing methods
* Jan 09, 2012: Release 0.4.0 - method for providing params can now return Iterables; few issues with ignored tests fixed (counting tests, failing ignored tests, etc.); parameter values are computed only once now; more detailed description in JUnitParamsRunner javadoc.
* Dec 20, 2011: Release 0.3.7 - params for a single test method can be provided by multiple methods, so you can now categorize your parameters (e.g. positive/negative cases)
* Nov 17, 2011: Release 0.3.6 - fixed issues 6,8-11 + handles properly situation when there's an abstract test superclass with test methods and params providers. Thanks to David Karlsen, Artur Gajowy, Marcin Zajączkowski for finding and proposing fixes for the issues.
* Oct 28, 2011: Release 0.3.5 - Adapted to work with JUnit 4.10.
* Jun 14, 2011: Release 0.3.2 - fixed issues with @Ignore'ed non-parametrised tests and some @Rule's. JUnitParams can also be easily 
used by other Runners - look at javadocs for a description.
* May 25, 2011: Release 0.3.0 - param providing methods can be in test superclasses, fixed some problems with showing params without toString, containing newline and parentheses
* May 16, 2011: Release 0.2.0 - some javadocs added, now test methods can also provide parameters
* May 15, 2011: Release 0.1.0, now available also in Maven
* May 12, 2011: Project created.
