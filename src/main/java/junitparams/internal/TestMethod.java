package junitparams.internal;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import junitparams.DatabaseParameters;
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
public class TestMethod {
    private FrameworkMethod frameworkMethod;
    private Class<?> testClass;
    private ParametersReader parametersReader;
    private Object[] cachedParameters;
    private TestCaseNamingStrategy namingStrategy;

    public TestMethod(FrameworkMethod method, TestClass testClass) {
        this.frameworkMethod = method;
        this.testClass = testClass.getJavaClass();
        parametersReader = new ParametersReader(testClass(), frameworkMethod);

		namingStrategy = new MacroSubstitutionNamingStrategy(this);
    }

    public String name() {
        return frameworkMethod.getName();
    }

    public static List<TestMethod> listFrom(List<FrameworkMethod> annotatedMethods, TestClass testClass) {
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
        if (!(obj instanceof TestMethod))
            return false;

        if (!frameworkMethod.getName().equals(((TestMethod) obj).frameworkMethod.getName()))
            return false;

        if (!frameworkMethod.getMethod().getParameterTypes().equals(((TestMethod) obj).frameworkMethod.getMethod().getParameterTypes()))
            return false;

        return true;
    }

    Class<?> testClass() {
        return testClass;
    }

    public boolean isIgnored() {
        if (frameworkMethod.getAnnotation(Ignore.class) != null)
            return true;

        if (isParameterised() && parametersSets().length == 0)
            return true;

        return false;
    }

    public boolean isNotIgnored() {
        return !isIgnored();
    }

    public Annotation[] annotations() {
        return frameworkMethod.getAnnotations();
    }

    public <T extends java.lang.annotation.Annotation> T getAnnotation(Class<? extends Annotation> annotationType) {
        return (T) frameworkMethod.getAnnotation(annotationType);
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
            return Description.createTestDescription(testClass(), name(), annotations());
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

    public boolean isParameterised() {
        return frameworkMethod.getMethod().isAnnotationPresent(Parameters.class)
            || frameworkMethod.getMethod().isAnnotationPresent(FileParameters.class)
            || frameworkMethod.getMethod().isAnnotationPresent(DatabaseParameters.class);
    }

    void warnIfNoParamsGiven() {
        if (isNotIgnored() && isParameterised() && parametersSets().length == 0)
            System.err.println("Method " + name() + " gets empty list of parameters, so it's being ignored!");
    }

    public FrameworkMethod frameworkMethod() {
        return frameworkMethod;
    }

}
