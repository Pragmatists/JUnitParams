package junitparams;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.*;
import org.junit.runners.model.*;

/**
 * <h1>JUnitParams</h1><br/>
 * <p>
 * JUnit runner for parameterised tests. Annotate your test class with
 * <code>&#064;RunWith(JUnitParamsRunner.class)</code> and place
 * <code>&#064;Parameters</code> annotation on each test method which requires
 * parameters.
 * </p>
 * <br/>
 * <h2>Contents</h2> <b> <a href="#1">1. Parameterising tests</a><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#a">a. Parameterising tests via values
 * in annotation</a><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#b">b. Parameterising tests via a
 * method that returns parameter values</a><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#c">c. Parameterising tests via
 * external classes</a><br/>
 * <a href="#2">2. Using JUnitParams together with Spring</a><br/>
 * </b><br/>
 * <h3 id="1">1. Parameterising tests</h3> <h4 id="a">a. Parameterising tests
 * via values in annotation</h4>
 * <p>
 * Parameterising tests with values defined in annotations:
 * 
 * <pre>
 *   &#064;Test
 *   &#064;Parameters({ "20, Tarzan", "0, Jane" })
 *   public void testReadyToLiveInJungle(int yearsInJungle, String person) {
 *       ...
 *   }
 * </pre>
 * 
 * </p>
 * <h4 id="b">b. Parameterising tests via a method that returns parameter values
 * </h4>
 * <p>
 * Tests can also be parameterised using a method that provides values:
 * 
 * <pre>
 *   &#064;Test
 *   &#064;Parameters(method = "cartoonCharacters")
 *   public void testReadyToLiveInJungle(int yearsInJungle, String person) {
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
 * <code>Object[]</code> array.
 * </p>
 * <p>
 * The <code>method</code> argument of a <code>@Parameters</code> annotation can
 * be ommited if the method that provides parameters has a the same name as the
 * test, but prefixed by <code>parametersFor</code>. So our example would look
 * like this:
 * 
 * <pre>
 *   &#064;Test
 *   &#064;Parameters
 *   public void testReadyToLiveInJungle(int yearsInJungle, String person) {
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
 * </p>
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
 * </p>
 * 
 * <h3 id="2">2. Usage with Spring</h3>
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
 * </p>
 * 
 * @author Pawel Lipinski
 */
public class JUnitParamsRunner extends BlockJUnit4ClassRunner {

    private ParameterisedTestClassRunner parameterisedRunner;

    public JUnitParamsRunner(Class<?> klass) throws InitializationError {
        super(klass);
        parameterisedRunner = new ParameterisedTestClassRunner();
        computeTestMethods();
    }

    protected void collectInitializationErrors(List<Throwable> errors) {
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        if (handleIgnored(method, notifier))
            return;

        TestMethod testMethod = new TestMethod(method, getTestClass());
        if (parameterisedRunner.shouldRun(testMethod))
            parameterisedRunner.runParameterisedTest(testMethod, methodBlock(method), notifier);
        else
            super.runChild(method, notifier);
    }

    private boolean handleIgnored(FrameworkMethod method, RunNotifier notifier) {
        TestMethod testMethod = new TestMethod(method, getTestClass());
        boolean ignored = false;
        if (method.getAnnotation(Ignore.class) != null) {
            if (parameterisedRunner.isParameterised(testMethod)) {
                Description ignoredMethod = parameterisedRunner
                        .describeParameterisedMethod(testMethod);
                for (Description child : ignoredMethod.getChildren()) {
                    notifier.fireTestIgnored(child);
                    ignored = true;
                }
            } else {
                notifier.fireTestIgnored(describeMethod(method));
                ignored = true;
            }
        }
        return ignored;
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return parameterisedRunner.computeTestMethods(
                TestMethod.listFrom(getTestClass().getAnnotatedMethods(
                        Test.class), getTestClass()), false);
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        Statement methodInvoker = parameterisedRunner
                .parameterisedMethodInvoker(new TestMethod(method, getTestClass()), test);
        if (methodInvoker == null)
            methodInvoker = super.methodInvoker(method, test);

        return methodInvoker;
    }

    @Override
    public Description getDescription() {
        Description description = Description.createSuiteDescription(getName(), getTestClass().getAnnotations());
        List<FrameworkMethod> resultMethods = parameterisedRunner.computeTestMethods(
                TestMethod.listFrom(getTestClass().getAnnotatedMethods(
                        Test.class), getTestClass()), true);

        for (FrameworkMethod method : resultMethods)
            description.addChild(describeMethod(method));

        return description;
    }

    private Description describeMethod(FrameworkMethod method) {
        Description child = parameterisedRunner
                .describeParameterisedMethod(new TestMethod(method, getTestClass()));

        if (child == null)
            child = describeChild(method);

        return child;
    }

    /**
     * Shortcut for returning an array of objects. All parameters passed to this
     * method are returned in an <code>Object[]</code> array.
     * 
     * @param params
     *            Values to be returned in an <code>Object[]</code> array.
     * @return Values passed to this method.
     */
    public static Object[] $(Object... params) {
        return params;
    }
}
