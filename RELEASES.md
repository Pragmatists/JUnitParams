## JUnitParams 1.0.5 release. Release date : ?

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
 
You can create your own annotations for parameter conversion. Just annotate it with `@Param` and pass it a reference to `Converter` implementation
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
