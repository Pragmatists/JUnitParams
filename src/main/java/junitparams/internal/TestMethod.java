package junitparams.internal;

import junitparams.internal.annotation.FrameworkMethodAnnotations;
import junitparams.internal.parameters.ParametersReader;
import junitparams.naming.MacroSubstitutionNamingStrategy;
import junitparams.naming.TestCaseName;
import junitparams.naming.TestCaseNamingStrategy;
import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A wrapper for a test method
 *
 * @author Pawel Lipinski
 */
public class TestMethod {

    private FrameworkMethod frameworkMethod;
    private FrameworkMethodAnnotations frameworkMethodAnnotations;

    private Memoizer<Object[]> parameters;
    private Memoizer<Description> description;

    public TestMethod(final FrameworkMethod method, final TestClass testClass) {
        this.frameworkMethod = method;
        frameworkMethodAnnotations = new FrameworkMethodAnnotations(method);

        description = new Memoizer<Description>() {
            @Override
            protected Description computeValue() {

                if (isNotIgnored() && !describeFlat()) {
                    TestCaseNamingStrategy namingStrategy = new MacroSubstitutionNamingStrategy(getAnnotation(TestCaseName.class), name());
                    return new ParametrizedDescription(namingStrategy, testClass.getJavaClass().getName(), name()).parametrizedDescription(parametersSets());
                } else {
                    return Description.createTestDescription(testClass.getJavaClass(), name(), frameworkMethodAnnotations.allAnnotations());
                }
            }

            private boolean describeFlat() {
                return System.getProperty("JUnitParams.flat") != null;
            }

        };
        parameters = new Memoizer<Object[]>() {
            @Override
            protected Object[] computeValue() {
                return new ParametersReader(testClass.getJavaClass(), frameworkMethod).read();
            }
        };
    }

    public String name() {
        return frameworkMethod.getName();
    }

    static List<TestMethod> listFrom(List<FrameworkMethod> annotatedMethods, TestClass testClass) {
        List<TestMethod> methods = new ArrayList<TestMethod>();

        for (FrameworkMethod frameworkMethod : annotatedMethods)
            methods.add(new TestMethod(frameworkMethod, testClass));

        return methods;
    }

    @Override
    public int hashCode() {
        return frameworkMethod.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TestMethod)
                && hasTheSameNameAsFrameworkMethod((TestMethod) obj)
                && hasTheSameParameterTypesAsFrameworkMethod((TestMethod) obj);
    }

    private boolean hasTheSameNameAsFrameworkMethod(TestMethod testMethod) {
        return frameworkMethod.getName().equals(testMethod.frameworkMethod.getName());
    }

    private boolean hasTheSameParameterTypesAsFrameworkMethod(TestMethod testMethod) {
        Class<?>[] frameworkMethodParameterTypes = frameworkMethod.getMethod().getParameterTypes();
        Class<?>[] testMethodParameterTypes = testMethod.frameworkMethod.getMethod().getParameterTypes();
        return Arrays.equals(frameworkMethodParameterTypes, testMethodParameterTypes);
    }

    public boolean isIgnored() {
        return hasIgnoredAnnotation() || hasNoParameters();
    }

    private boolean hasIgnoredAnnotation() {
        return frameworkMethodAnnotations.hasAnnotation(Ignore.class);
    }

    private boolean hasNoParameters() {
        return isParameterised() && parametersSets().length == 0;
    }

    public boolean isNotIgnored() {
        return !isIgnored();
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return frameworkMethodAnnotations.getAnnotation(annotationType);
    }

    public Object[] parametersSets() {
        return parameters.get();
    }

    void warnIfNoParamsGiven() {
        if (isNotIgnored() && isParameterised() && parametersSets().length == 0)
            System.err.println("Method " + name() + " gets empty list of parameters, so it's being ignored!");
    }

    public FrameworkMethod frameworkMethod() {
        return frameworkMethod;
    }

    boolean isParameterised() {
        return frameworkMethodAnnotations.isParametrised();
    }

    Description description() {
        return description.get();
    }
}
