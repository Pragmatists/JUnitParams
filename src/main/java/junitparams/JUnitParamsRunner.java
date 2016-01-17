package junitparams;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import junitparams.internal.ParameterisedTestClassRunner;
import junitparams.internal.ParametrizedTestMethodsFilter;
import junitparams.internal.TestMethod;

/**
 * <h1>JUnitParams</h1><br>
 * <p>
 * This is a JUnit runner for parameterised tests that don't suck. Annotate your test class with
 * <code>&#064;RunWith(JUnitParamsRunner.class)</code> and place
 * <code>&#064;Parameters</code> annotation on each test method which requires
 * parameters. Nothing more needed - no special structure, no dirty tricks.
 * </p>
 * <br>
 * <h2>Contents</h2> <b> <a href="#p1">1. Parameterising tests</a><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#a">a. Parameterising tests via values
 * in annotation</a><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#b">b. Parameterising tests via a
 * method that returns parameter values</a><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#c">c. Parameterising tests via
 * external classes</a><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#d">d. Loading parameters from files</a><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#d">e. Converting parameter values</a><br>
 * <a href="#p2">2. Usage with Spring</a><br>
 * <a href="#p3">3. Other options</a><br>
 * </b><br>
 * <h3 id="p1">1. Parameterising tests</h3> Parameterised tests are a great way
 * to limit the amount of test code when you need to test the same code under
 * different conditions. Ever tried to do it with standard JUnit tools like
 * Parameterized runner or Theories? I always thought they're so awkward to use,
 * that I've written this library to help all those out there who'd like to have
 * a handy tool.
 *
 * So here we go. There are a few different ways to use JUnitParams, I will try
 * to show you all of them here.
 *
 * <h4 id="a">a. Parameterising tests via values in annotation</h4>
 * <p>
 * You can parameterise your test with values defined in annotations. Just pass
 * sets of test method argument values as an array of Strings, where each string
 * contains the argument values separated by a comma or a pipe "|".
 *
 * <pre>
 *   &#064;Test
 *   &#064;Parameters({ "20, Tarzan", "0, Jane" })
 *   public void cartoonCharacters(int yearsInJungle, String person) {
 *       ...
 *   }
 * </pre>
 *
 * Sometimes you may be interested in passing enum values as parameters, then
 * you can just write them as Strings like this:
 *
 * <pre>
 * &#064;Test
 * &#064;Parameters({ &quot;FROM_JUNGLE&quot;, &quot;FROM_CITY&quot; })
 * public void passEnumAsParam(PersonType person) {
 * }
 * </pre>
 *
 * <h4 id="b">b. Parameterising tests via a method that returns parameter values
 * </h4>
 * <p>
 * Obviously passing parameters as strings is handy only for trivial situations,
 * that's why for normal cases you have a method that gives you a collection of
 * parameters:
 *
 * <pre>
 *   &#064;Test
 *   &#064;Parameters(method = "cartoonCharacters")
 *   public void cartoonCharacters(int yearsInJungle, String person) {
 *       ...
 *   }
 *   private Object[] cartoonCharacters() {
 *      return $(
 *          $(0, "Tarzan"),
 *          $(20, "Jane")
 *      );
 *   }
 * </pre>
 *
 * Where <code>$(...)</code> is a static method defined in
 * <code>JUnitParamsRunner</code> class, which returns its parameters as a
 * <code>Object[]</code> array. Just a shortcut, so that you don't need to write the ugly <code>new Object[] {}</code> kind of stuff.
 *
 * <p>
 * <code>method</code> can take more than one method name - you can pass as many
 * of them as you want, separated by commas. This enables you to divide your
 * test cases e.g. into categories.
 * <pre>
 *   &#064;Test
 *   &#064;Parameters(method = "menCharactes, womenCharacters")
 *   public void cartoonCharacters(int yearsInJungle, String person) {
 *       ...
 *   }
 *   private Object[] menCharacters() {
 *      return $(
 *          $(20, "Tarzan"),
 *          $(2, "Chip"),
 *          $(2, "Dale")
 *      );
 *   }
 *   private Object[] womenCharacters() {
 *      return $(
 *          $(0, "Jane"),
 *          $(18, "Pocahontas")
 *      );
 *   }
 * </pre>
 * <p>
 * The <code>method</code> argument of a <code>@Parameters</code> annotation can
 * be ommited if the method that provides parameters has a the same name as the
 * test, but prefixed by <code>parametersFor</code>. So our example would look
 * like this:
 *
 * <pre>
 *   &#064;Test
 *   &#064;Parameters
 *   public void cartoonCharacters(int yearsInJungle, String person) {
 *       ...
 *   }
 *   private Object[] parametersForCartoonCharacters() {
 *      return $(
 *          $(0, "Tarzan"),
 *          $(20, "Jane")
 *      );
 *   }
 * </pre>
 *
 * <p>
 * If you don't like returning untyped values and arrays, you can equally well
 * return any Iterable of concrete objects:
 *
 * <pre>
 *   &#064;Test
 *   &#064;Parameters
 *   public void cartoonCharacters(Person character) {
 *       ...
 *   }
 *   private List&lt;Person&gt; parametersForCartoonCharacters() {
 *      return Arrays.asList(
 *          new Person(0, "Tarzan"),
 *          new Person(20, "Jane")
 *      );
 *   }
 * </pre>
 *
 * If we had more than just two Person's to make, we would get redundant,
 * so JUnitParams gives you a simplified way of creating objects to be passed as
 * params. You can omit the creation of the objects and just return their constructor
 * argument values like this:
 *
 * <pre>
 *   &#064;Test
 *   &#064;Parameters
 *   public void cartoonCharacters(Person character) {
 *       ...
 *   }
 *   private List&lt;?&gt; parametersForCartoonCharacters() {
 *      return Arrays.asList(
 *          $(0, "Tarzan"),
 *          $(20, "Jane")
 *      );
 *   }
 * </pre>
 * And JUnitParams will invoke the appropriate constructor (<code>new Person(int age, String name)</code> in this case.)
 * <b>If you want to use it, watch out! Automatic refactoring of constructor
 * arguments won't be working here!</b>
 *
 * <p>
 * You can also define methods that provide parameters in subclasses and use
 * them in test methods defined in superclasses, as well as redefine data
 * providing methods in subclasses to be used by test method defined in a
 * superclass. That you can doesn't mean you should. Inheritance in tests is
 * usually a code smell (readability hurts), so make sure you know what you're
 * doing.
 *
 * <h4 id="c">c. Parameterising tests via external classes</h4>
 * <p>
 * For more complex cases you may want to externalise the method that provides
 * parameters or use more than one method to provide parameters to a single test
 * method. You can easily do that like this:
 *
 * <pre>
 *   &#064;Test
 *   &#064;Parameters(source = CartoonCharactersProvider.class)
 *   public void testReadyToLiveInJungle(int yearsInJungle, String person) {
 *       ...
 *   }
 *   ...
 *   class CartoonCharactersProvider {
 *      public static Object[] provideCartoonCharactersManually() {
 *          return $(
 *              $(0, "Tarzan"),
 *              $(20, "Jane")
 *          );
 *      }
 *      public static Object[] provideCartoonCharactersFromDB() {
 *          return cartoonsRepository.loadCharacters();
 *      }
 *   }
 * </pre>
 *
 * All methods starting with <code>provide</code> are used as parameter
 * providers.
 *
 * <p>
 * Sometimes though you may want to use just one or few methods of some class to
 * provide you parameters. This can be done as well like this:
 *
 * <pre>
 *   &#064;Test
 *   &#064;Parameters(source = CartoonCharactersProvider.class, method = "cinderellaCharacters,snowwhiteCharacters")
 *   public void testPrincesses(boolean isAPrincess, String characterName) {
 *       ...
 *   }
 * </pre>
 *
 *
 * <h4 id="d">d. Loading parameters from files</h4> You may be interested in
 * loading parameters from a file. This is very easy if it's a CSV file with
 * columns in the same order as test method parameters:
 *
 * <pre>
 *   &#064;Test
 *   &#064;FileParameters("cartoon-characters.csv")
 *   public void shouldSurviveInJungle(int yearsInJungle, String person) {
 *       ...
 *   }
 * </pre>
 *
 * But if you want to process the data from the CSV file a bit to use it in the
 * test method arguments, you
 * need to use an <code>IdentityMapper</code>. Look:
 *
 * <pre>
 *   &#064;Test
 *   &#064;FileParameters(value = "cartoon-characters.csv", mapper = CartoonMapper.class)
 *   public void shouldSurviveInJungle(Person person) {
 *       ...
 *   }
 *
 *   public class CartoonMapper extends IdentityMapper {
 *     &#064;Override
 *     public Object[] map(Reader reader) {
 *         Object[] map = super.map(reader);
 *         List&lt;Object[]&gt; result = new LinkedList&lt;Object[]&gt;();
 *         for (Object lineObj : map) {
 *             String line = (String) lineObj; // line in a format just like in the file
 *             result.add(new Object[] { ..... }); // some format edible by the test method
 *         }
 *         return result.toArray();
 *     }
 *
 * }
 * </pre>
 *
 * A CSV files with a header are also supported with the use of <code>CsvWithHeaderMapper</code> class.
 *
 * You may also want to use a completely different file format, like excel or
 * something. Then just parse it yourself:
 *
 * <pre>
 *   &#064;Test
 *   &#064;FileParameters(value = "cartoon-characters.xsl", mapper = ExcelCartoonMapper.class)
 *   public void shouldSurviveInJungle(Person person) {
 *       ...
 *   }
 *
 *   public class CartoonMapper implements DataMapper {
 *     &#064;Override
 *     public Object[] map(Reader fileReader) {
 *         ...
 *     }
 * }
 * </pre>
 *
 * As you see, you don't need to open or close the file. Just read it from the
 * reader and parse it the way you wish.
 *
 * By default the file is loaded from the file system, relatively to where you start the tests from. But you can also use a resource from
 * the classpath by prefixing the file name with <code>classpath:</code>
 *
 * <h4 id="e">e. Converting parameter values</h4>
 * Sometimes you want to pass some parameter in one form, but use it in the test in another. Dates are a good example. It's handy to
 * specify them in the parameters as a String like "2013.01.01", but you'd like to use a Jodatime's LocalDate or JDKs Date in the test
 * without manually converting the value in the test. This is where the converters become handy. It's enough to annotate a parameter with
 * a <code>&#064;ConvertParam</code> annotation, give it a converter class and possibly some options (like date format in this case) and
 * you're done. Here's an example:
 * <pre>
 *     &#064;Test
 *     &#064;Parameters({ "01.12.2012, A" })
 *     public void convertMultipleParams(
 *                  &#064;ConvertParam(value = StringToDateConverter.class, options = "dd.MM.yyyy") Date date,
 *                  &#064;ConvertParam(LetterToASCIIConverter.class) int num) {
 *
 *         Calendar calendar = Calendar.getInstance();
 *         calendar.setTime(date);
 *
 *         assertEquals(2012, calendar.get(Calendar.YEAR));
 *         assertEquals(11, calendar.get(Calendar.MONTH));
 *         assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
 *
 *         assertEquals(65, num);
 *     }
 * </pre>
 *
 * <h3 id="p2">2. Usage with Spring</h3>
 * <p>
 * You can easily use JUnitParams together with Spring. The only problem is that
 * Spring's test framework is based on JUnit runners, and JUnit allows only one
 * runner to be run at once. Which would normally mean that you could use only
 * one of Spring or JUnitParams. Luckily we can cheat Spring a little by adding
 * this to your test class:
 *
 * <pre>
 * private TestContextManager testContextManager;
 *
 * &#064;Before
 * public void init() throws Exception {
 *     this.testContextManager = new TestContextManager(getClass());
 *     this.testContextManager.prepareTestInstance(this);
 * }
 * </pre>
 *
 * This lets you use in your tests anything that Spring provides in its test
 * framework.
 *
 * <h3 id="p3">3. Other options</h3>
 * <h4> Enhancing test case description</h4>
 * You can use <code>TestCaseName</code> annotation to provide template of the individual test case name:
 * <pre>
 *     &#064;TestCaseName("factorial({0}) = {1}")
 *     &#064;Parameters({ "1,1"})
 *     public void fractional_test(int argument, int result) { }
 * </pre>
 * Will be displayed as 'fractional(1)=1'
 * <h4>Customizing how parameter objects are shown in IDE</h4>
 * <p>
 * Tests show up in your IDE as a tree with test class name being the root, test
 * methods being nodes, and parameter sets being the leaves. If you want to
 * customize the way an parameter object is shown, create a <b>toString</b>
 * method for it.
 * <h4>Empty parameter sets</h4>
 * <p>
 * If you create a parameterised test, but won't give it any parameter sets, it
 * will be ignored and you'll be warned about it.
 * <h4>Parameterised test with no parameters</h4>
 * <p>
 * If for some reason you want to have a normal non-parameterised method to be
 * annotated with @Parameters, then fine, you can do it. But it will be ignored
 * then, since there won't be any params for it, and parameterised tests need
 * parameters to execute properly (parameters are a part of test setup, right?)
 * <h4>JUnit Rules</h4>
 * <p>
 * The runner for parameterised test is trying to keep all the @Rule's running,
 * but if something doesn't work - let me know. It's pretty tricky, since the
 * rules in JUnit are chained, but the chain is kind of... unstructured, so
 * sometimes I need to guess how to call the next element in chain. If you have
 * your own rule, make sure it has a field of type Statement which is the next
 * statement in chain to call.
 * <h4>Test inheritance</h4>
 * <p>
 * Although usually a bad idea, since it makes tests less readable, sometimes
 * inheritance is the best way to remove repetitions from tests. JUnitParams is
 * fine with inheritance - you can define a common test in the superclass, and
 * have separate parameters provider methods in the subclasses. Also the other
 * way around is ok, you can define parameter providers in superclass and have
 * tests in subclasses uses them as their input.
 *
 * @author Pawel Lipinski (lipinski.pawel@gmail.com)
 */
public class JUnitParamsRunner extends BlockJUnit4ClassRunner {

    private ParametrizedTestMethodsFilter parametrizedTestMethodsFilter = new ParametrizedTestMethodsFilter(this);
    private ParameterisedTestClassRunner parameterisedRunner;
    private Description description;

    public JUnitParamsRunner(Class<?> klass) throws InitializationError {
        super(klass);
        parameterisedRunner = new ParameterisedTestClassRunner(getTestClass());
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        super.filter(filter);
        this.parametrizedTestMethodsFilter = new ParametrizedTestMethodsFilter(this,filter);
    }

    protected void collectInitializationErrors(List<Throwable> errors) {
        for (Throwable throwable : errors)
            throwable.printStackTrace();
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        if (handleIgnored(method, notifier))
            return;

        TestMethod testMethod = parameterisedRunner.testMethodFor(method);
        if (parameterisedRunner.shouldRun(testMethod)){
            parameterisedRunner.runParameterisedTest(testMethod, methodBlock(method), notifier);
        }
        else{
            verifyMethodCanBeRunByStandardRunner(testMethod);
            super.runChild(method, notifier);
        }
    }

    private void verifyMethodCanBeRunByStandardRunner(TestMethod testMethod) {
        List<Throwable> errors = new ArrayList<Throwable>();
        testMethod.frameworkMethod().validatePublicVoidNoArg(false, errors);
        if (!errors.isEmpty()) {
            throw new RuntimeException(errors.get(0));
        }
    }

    private boolean handleIgnored(FrameworkMethod method, RunNotifier notifier) {
        TestMethod testMethod = parameterisedRunner.testMethodFor(method);
        if (testMethod.isIgnored())
            notifier.fireTestIgnored(describeMethod(method));

        return testMethod.isIgnored();
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return parameterisedRunner.computeFrameworkMethods();
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        Statement methodInvoker = parameterisedRunner.parameterisedMethodInvoker(method, test);
        if (methodInvoker == null)
            methodInvoker = super.methodInvoker(method, test);

        return methodInvoker;
    }

    @Override
    public Description getDescription() {
        if (description == null) {
            description = Description.createSuiteDescription(getName(), getTestClass().getAnnotations());
            List<FrameworkMethod> resultMethods = getListOfMethods();

            for (FrameworkMethod method : resultMethods)
                description.addChild(describeMethod(method));
        }

        return description;
    }

    private List<FrameworkMethod> getListOfMethods() {
        List<FrameworkMethod> frameworkMethods = parameterisedRunner.returnListOfMethods();
        return parametrizedTestMethodsFilter.filteredMethods(frameworkMethods);
    }

    public Description describeMethod(FrameworkMethod method) {
        Description child = parameterisedRunner.describeParameterisedMethod(method);

        if (child == null)
            child = describeChild(method);

        return child;
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        Object test;
        try {
            test = new ReflectiveCallable() {
                @Override
                protected Object runReflectiveCall() throws Throwable {
                    return createTest();
                }
            }.run();
        } catch (Throwable e) {
            return new Fail(e);
        }

        Statement statement = methodInvoker(method, test);
        statement = possiblyExpectingExceptions(method, test, statement);
        statement = withPotentialTimeout(method, test, statement);
        statement = withBefores(method, test, statement);
        statement = withAfters(method, test, statement);
        statement = withRules(method, test, statement);
        return statement;
    }

    private Statement withRules(FrameworkMethod method, Object target,
            Statement statement) {
        List<TestRule> testRules = getTestRules(target);
        Statement result = statement;
        result = withMethodRules(method, testRules, target, result);
        result = withTestRules(method, testRules, result);

        return result;
    }

    private Statement withMethodRules(FrameworkMethod method, List<TestRule> testRules,
        Object target, Statement result) {
        for (org.junit.rules.MethodRule each : getMethodRules(target)) {
            if (!testRules.contains(each)) {
                result = each.apply(result, method, target);
            }
        }
        return result;
    }

    private List<org.junit.rules.MethodRule> getMethodRules(Object target) {
        return rules(target);
    }

    private Statement withTestRules(FrameworkMethod method, List<TestRule> testRules,
            Statement statement) {
        return testRules.isEmpty() ? statement :
            new RunRules(statement, testRules, internalDescribeChild(method, statement));
    }

    private Description internalDescribeChild(FrameworkMethod method,
        Statement methodInvoker) {
        Description description =
            parameterisedRunner.getParameterisedTestDescription(method, methodInvoker);
        if (description == null) {
            return describeChild(method);
        }

        return description;
    }

    /**
     * Shortcut for returning an array of objects. All parameters passed to this
     * method are returned in an <code>Object[]</code> array.
     *
     * Should not be used to create var-args arrays, because of the way Java resolves
     * var-args for objects and primitives.
     *
     * @deprecated This method is no longer supported. It might be removed in future
     * as it does not support all cases (especially var-args). Create arrays using
     * <code>new Object[]{}</code> instead.
     *
     * @param params
     *            Values to be returned in an <code>Object[]</code> array.
     * @return Values passed to this method.
     */
    @Deprecated
    public static Object[] $(Object... params) {
        return params;
    }
}
