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
