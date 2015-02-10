package junitparams.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import junitparams.FileParameters;
import junitparams.Parameters;
import junitparams.internal.parameters.ParametersReader;
import junitparams.naming.MacroSubstitutionNamingStrategy;
import junitparams.naming.TestCaseNamingStrategy;

/**
 * A wrapper for a test method
 *
 * @author Pawel Lipinski
 */
public class TestMethod extends FrameworkMethod {
    private final int index;
    private final Object params;

    private Class<?> testClass;
    private TestCaseNamingStrategy namingStrategy;

    private static final Map<Method, Description> descriptions = new HashMap<Method, Description>();

    public TestMethod(FrameworkMethod method, TestClass testClass, Object params, int index) {
        super(method.getMethod());
        this.index = index;
        this.params = params;
        this.testClass = testClass.getJavaClass();

        namingStrategy = new MacroSubstitutionNamingStrategy(this);
    }

    public static List<TestMethod> listFrom(List<FrameworkMethod> annotatedMethods, TestClass testClass) {
        List<TestMethod> methods = new ArrayList<TestMethod>();

        for (FrameworkMethod frameworkMethod : annotatedMethods) {

            ParametersReader parametersReader = new ParametersReader(testClass.getJavaClass(), frameworkMethod);
            boolean notIgnored = !frameworkMethod.getMethod().isAnnotationPresent(Ignore.class);

            if (notIgnored && parametersReader.isParametrized()) {
                Object[] paramsSet = parametersReader.read();
                if (paramsSet.length == 0) {
                    methods.add(singleTestMethod(testClass, frameworkMethod));
                } else {
                    for (int i = 0; i < paramsSet.length; i++) {
                        Object params = paramsSet[i];
                        methods.add(new TestMethod(frameworkMethod, testClass, params, i));
                    }
                }
            } else {
                methods.add(singleTestMethod(testClass, frameworkMethod));
            }
        }

        return methods;
    }

    private static TestMethod singleTestMethod(TestClass testClass, FrameworkMethod frameworkMethod) {
        return new TestMethod(frameworkMethod, testClass, new Object[]{}, 0);
    }

    Class<?> testClass() {
        return testClass;
    }

    public boolean isIgnored() {
        return getMethod().isAnnotationPresent(Ignore.class);
    }

    public boolean isNotIgnored() {
        return !isIgnored();
    }

    Description describe() {
        if (isNotIgnored() && !describeFlat()) {
            Description parentDescription = createParentIfNotExists();
            addToParentIfNotExists(parentDescription);

            return parentDescription;
        } else {
            return Description.createTestDescription(testClass(), getName(), getAnnotations());
        }
    }

    private Description createParentIfNotExists() {
        Description parentDescription = descriptions.get(getMethod());
        if (parentDescription == null) {
            parentDescription = Description.createSuiteDescription(getName());
            descriptions.put(getMethod(), parentDescription);
        }
        return parentDescription;
    }

    private void addToParentIfNotExists(Description parentDescription) {
        String name = namingStrategy.getTestCaseName(index);
        String uniqueMethodId = Utils.uniqueMethodId(index, params, getName());
        Description description = Description.createTestDescription(testClass().getName(), name, uniqueMethodId);
        if (!parentDescription.getChildren().contains(description)) {
            parentDescription.addChild(description);
        }
    }

    private boolean describeFlat() {
        return System.getProperty("JUnitParams.flat") != null;
    }

    public boolean isParameterised() {
        return getMethod().isAnnotationPresent(Parameters.class)
            || getMethod().isAnnotationPresent(FileParameters.class);
    }

    void warnIfNoParamsGiven() {
        if (isNotIgnored() && isParameterised() && params == null)
            System.err.println("Method " + getName() + " gets empty list of parameters, so it's being ignored!");
    }

    public Object getParameters() {
        return params;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        TestMethod that = (TestMethod) o;

        if (index != that.index) {
            return false;
        }
        if (params != null ? !params.equals(that.params) : that.params != null) {
            return false;
        }
        if (testClass != null ? !testClass.equals(that.testClass) : that.testClass != null) {
            return false;
        }
        if (getMethod() != null ? !getMethod().equals(that.getMethod()) : that.getMethod() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + index;
        result = 31 * result + (params != null ? params.hashCode() : 0);
        result = 31 * result + (testClass != null ? testClass.hashCode() : 0);
        result = 31 * result + (getMethod() != null ? getMethod().hashCode() : 0);
        return result;
    }
}
