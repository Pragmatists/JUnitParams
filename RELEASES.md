## JUnitParams 1.1.0 release. Release date : 2017-04-17

### [Breaking change] Enhance ParametersProvider with FrameworkMethod

`ParametersProvider#initialize` method was enhanced with new parameter - FrameworkMethod object. It gives access to all information connected with method in testing e.g. annotations. 

```java
    public static class MethodNameReader implements ParametersProvider<CustomParameters> {
        private FrameworkMethod frameworkMethod;
    
        @Override
        public void initialize(CustomParameters parametersAnnotation, FrameworkMethod frameworkMethod) {
            this.frameworkMethod = frameworkMethod;
        }
    
        @Override
        public Object[] getParameters() {
            return new Object[]{frameworkMethod.getName()};
        }
    }
```

**How to migrate?** Add new parameter to `ParametersProvider#initialize` method if you are using any custom parameters provider.

Thanks [bohrqiu](https://github.com/bohrqiu) for contribution.

### Possibility to link parameters to test case by `@Named` annotation

In some cases we can don't want to link parameters to test case by method name or putting everything into the test case annotation. We introduce possibility to define custom name for the connection by using `@NamedParameters` annotation.

```java
    @Test
    @Parameters(named = "return 1")
    public void testSingleNamedMethod(int number) {
        assertThat(number).isEqualTo(1);
    }

    @NamedParameters("return 1")
    private Integer[] returnNumberOne() {
        return new Integer[] { 1 };
    }
```

Thanks [tobyt42](https://github.com/tobyt42) for idea and contribution.

### Speedups

New version is noticeably faster for big as well as small set of parameters.
 
 Thanks for [ukcrpb6](https://github.com/ukcrpb6) for contribution.
 
## JUnitParams 1.0.6 release. Release date : 2017-01-23

### Change default testcase naming template
Due to problems with test results presentation e.g. in Jenkins where test cases
will be not ordered by execution but by naming, default test case naming template was changed to:
```
{method}({params}) [{index}]
```
from
```
[{index}] {params} ({method})
```
Thanks to [ealgell](https://github.com/ealgell) for contribution.

## Support multiple custom annotations for parameter conversion.
Now you can apply multiple your own annotations for parameter conversion.
```java
    @Test
    @Parameters("2")
    public void useMultipleConvertersFromLeftToRight(@TimesTwo @PlusTwo Integer param) {
        assertThat(param).isEqualTo(2 * 2 + 2);
    }
```

Thanks to [bbobcik](https://github.com/bbobcik) for inspiration.

## Test will fail if provided parameters cannot be resolved

As [matthopkins](https://github.com/matthopkins) suggested it was a problem that tests were ignored
when could not resolve provided parameters. Now tests will fail in such a situation.

```java
java.lang.IllegalStateException: Method package.className#methodName is annotated with @Parameters but there were no parameters provided.
```


## Improvements
Thanks to the rest of contributors for lots of improvements: 
* [bencampion](https://github.com/bencampion)
* [jtbeckha](https://github.com/jtbeckha)

## JUnitParams 1.0.5 release. Release date : 2016-04-14

### Deprecated $ method
Utility method `$` was deprecated. It was causing too much problems and we decided not to support it any more. If you wish to keep using it, implement it in your own codebase.

### Automatic class name to class object conversion

```java

    @Test
    @Parameters({"java.lang.Object", "java.lang.String"})
    public void passClassAsString(Class<?> clazz) {
        assertThat(clazz).isIn(java.lang.Object.class, java.lang.String.class);
    }
```

Thanks to [adammichalik](https://github.com/adammichalik) for contribution

### Support custom annotations for parameter conversion
 
You can create your own annotations for parameter conversion. Just annotate it with `@Param` and pass it a reference to `Converter` implementation.

Example:


```java

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @Param(converter = FormattedDateConverter.class)
    public @interface DateParam {

        String format() default "dd.MM.yyyy";
    }

    public static class FormattedDateConverter implements Converter<DateParam, Date> {

        private String format;

        @Override
        public void initialize(DateParam annotation) {
            this.format = annotation.format();
        }

        @Override
        public Date convert(Object param) throws ConversionFailedException {
            try {
                return new SimpleDateFormat(format).parse(param.toString());
            } catch (ParseException e) {
                throw new ConversionFailedException("failed");
            }
        }
    }
```

Usage example:

```java

    @Test
    @Parameters({"2012-12-01"})
    public void testWithConvertedDate(@DateParam Date date) {
        assertThat(...);
    }
```

Thanks to [bbobcik](https://github.com/bbobcik) for inspiration

### CustomParameters

You can create custom annotations for parameter providers. `@FileParameters` have been refactored to use this mechanism and should serve as a perfect usage example.

```java

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @CustomParameters(provider = FileParametersProvider.class)
    public @interface FileParameters {

        String fileLocation();
    
    }

    public class FileParametersProvider implements ParametersProvider<FileParameters> {

        private String fileLocation;
    
        @Override
        public void initialize(FileParameters fileParameters) {
            this.fileLocation = fileParameters.fileLocation();
        }
    
        @Override
        public Object[] getParameters() {
            return paramsFromFile(fileLocation);
        }
        
        ...
    }

```

### @CombinedParameters

Thanks to [piekarskim](https://github.com/piekarskim) The issue #1 is fixed. 
Using this annotation will result in creating a n-fold cartesian product of parameter values effectively testing each possible combination.
Since an example is worth a thousand words:

Such annotated test method:

```java

    @Test
    @CombinedParameters({"a,b", "1,2"})
    public void calledWithCartesianProduct(String character, Integer number) {
    ...
    }
```

Will be called 4 times with parameters:

```
 a  1 
 a  2 
 b  1 
 b  2 
```


### Bug fixes and improvements

Thanks to the rest of contributors for lots of bug fixes and improvements:

* [jtbeckha](https://github.com/jtbeckha)
* [szpak](https://github.com/szpak)
* [mkordas](https://github.com/mkordas)
* [davidwiking](https://github.com/davidwiking)
* [bennetelli](https://github.com/bennetelli)


## JUnitParams 1.0.4 release. Release date : 2015-01-23

### Configurable test case name
New annotation `@TestCaseName` that can be used for test case name configuration:

```java
  @Test
  @Parameters({ "1,1", "2,2" })
  @TestCaseName("factorial({0}) = {1}")
  public void custom_names_for_test_case(int argument, int result) { }
```

will produce tests with names:

```
factorial(1) = 1
factorial(2) = 2
```

Thanks to [Menliat](https://github.com/Menliat) for contribution.


### Allow usage of enums as a data source

Parameters annotation now allows passing Enum values as parameters

```
@Parameters(source = Fruit.class)
```

Thanks to [ukcrpb6](https://github.com/ukcrpb6) for contribution.


### Test results filtering fixed

When starting a single test method from within an IDE, the tests results were not shown up properly in the results tab.
Its fixed now thanks to [jerzykrlk](https://github.com/jerzykrlk)

### Bug fixes and improvements

Thanks to the rest of contributors for lots of bug fixes and improvements:

* [wojtek-szymanski](https://github.com/wojtek-szymanski)
* [Jacobus2k](https://github.com/Jacobus2k)
* [justhalf](https://github.com/justhalf)
* [szpak](https://github.com/szpak)
* [v-dev](https://github.com/v-dev)
