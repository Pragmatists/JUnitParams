package junitparams.internal;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import junitparams.internal.annotation.FrameworkMethodAnnotations;
import junitparams.internal.parameters.ParametersReader;
import junitparams.naming.MacroSubstitutionNamingStrategy;
import junitparams.naming.TestCaseNamingStrategy;

/**
 * A wrapper for a test method
 *
 * @author Pawel Lipinski
 */
public class TestMethod {
    private FrameworkMethod frameworkMethod;
    private FrameworkMethodAnnotations frameworkMethodAnnotations;
    private Class<?> testClass;
    private ParametersReader parametersReader;
    private Object[] cachedParameters;
    private TestCaseNamingStrategy namingStrategy;

    public TestMethod(FrameworkMethod method, TestClass testClass) {
        this.frameworkMethod = method;
        this.testClass = testClass.getJavaClass();
        frameworkMethodAnnotations = new FrameworkMethodAnnotations(method);
        parametersReader = new ParametersReader(testClass(), frameworkMethod);

        namingStrategy = new MacroSubstitutionNamingStrategy(this);
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

    private Class<?> testClass() {
        return testClass;
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

    Description describe() {
        if (isNotIgnored() && !describeFlat()) {
            Description parametrised = Description.createSuiteDescription(name());
            Object[] params = parametersSets();
            for (int i = 0; i < params.length; i++) {
                Object paramSet = params[i];
                String name = namingStrategy.getTestCaseName(i, paramSet);
                String uniqueMethodId = Utils.uniqueMethodId(i, paramSet, name());

                parametrised.addChild(
                        Description.createTestDescription(testClass().getName(), name, uniqueMethodId)
                );
            }
            return parametrised;
        } else {
            return Description.createTestDescription(testClass(), name(), frameworkMethodAnnotations.allAnnotations());
        }
    }

    private boolean describeFlat() {
        return System.getProperty("JUnitParams.flat") != null;
    }

    public Object[] parametersSets() {
        if (cachedParameters == null) {
            cachedParameters = parametersReader.read();
        }
        return cachedParameters;
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
}
