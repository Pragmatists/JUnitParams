package junitparams.internal;

import java.lang.reflect.Method;
import junitparams.internal.annotation.FrameworkMethodAnnotations;
import junitparams.internal.parameters.ParametersReader;
import junitparams.naming.MacroSubstitutionNamingStrategy;
import junitparams.naming.TestCaseName;
import junitparams.naming.TestCaseNamingStrategy;
import org.junit.Ignore;
import org.junit.Test;
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
    private Memoizer<DescribableFrameworkMethod> describableFrameworkMethod;

    public TestMethod(final FrameworkMethod method, final TestClass testClass) {
        this.frameworkMethod = method;
        frameworkMethodAnnotations = new FrameworkMethodAnnotations(method);

        final Class<?> javaClass = testClass.getJavaClass();
        describableFrameworkMethod = new DescribableFrameworkMethodMemoizer(javaClass);
        parameters = new Memoizer<Object[]>() {
            @Override
            protected Object[] computeValue() {
                return new ParametersReader(javaClass, frameworkMethod).read();
            }
        };
    }

    public String name() {
        return frameworkMethod.getName();
    }

    public static List<FrameworkMethod> listFrom(TestClass testClass) {
        List<FrameworkMethod> methods = new ArrayList<FrameworkMethod>();

        for (FrameworkMethod frameworkMethod : testClass.getAnnotatedMethods(Test.class)) {
            TestMethod testMethod = new TestMethod(frameworkMethod, testClass);
            methods.add(testMethod.describableFrameworkMethod());
        }

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

    private boolean isIgnored() {
        return hasIgnoredAnnotation() || hasNoParameters();
    }

    private boolean hasIgnoredAnnotation() {
        return frameworkMethodAnnotations.hasAnnotation(Ignore.class);
    }

    private boolean hasNoParameters() {
        return isParameterised() && parametersSets().length == 0;
    }

    private boolean isNotIgnored() {
        return !isIgnored();
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return frameworkMethodAnnotations.getAnnotation(annotationType);
    }

    public Object[] parametersSets() {
        return parameters.get();
    }

    private void warnIfNoParamsGiven() {
        if (isNotIgnored() && isParameterised() && parametersSets().length == 0)
            System.err.println("Method " + name() + " gets empty list of parameters, so it's being ignored!");
    }

    public FrameworkMethod frameworkMethod() {
        return frameworkMethod;
    }

    DescribableFrameworkMethod describableFrameworkMethod() {
        return describableFrameworkMethod.get();
    }

    boolean isParameterised() {
        return frameworkMethodAnnotations.isParametrised();
    }

    private class DescribableFrameworkMethodMemoizer extends Memoizer<DescribableFrameworkMethod> {

        private final Class<?> testClass;

        public DescribableFrameworkMethodMemoizer(Class<?> testClass) {
            this.testClass = testClass;
        }

        @Override
        protected DescribableFrameworkMethod computeValue() {
            Description baseDescription = Description.createTestDescription(
                    testClass, name(), frameworkMethodAnnotations.allAnnotations());
            Method method = frameworkMethod.getMethod();
            try {
                return createDescribableFrameworkMethod(method, baseDescription);
            } catch (IllegalStateException e) {
                // Defer error until running.
                return new DeferredErrorFrameworkMethod(method, baseDescription, e);
            }
        }

        private DescribableFrameworkMethod createDescribableFrameworkMethod(Method method, Description baseDescription) {
            if (isParameterised()) {
                if (isNotIgnored()) {
                    TestCaseNamingStrategy
                            namingStrategy = new MacroSubstitutionNamingStrategy(getAnnotation(TestCaseName.class), name());
                    Object[] parametersSets = parametersSets();
                    List<InstanceFrameworkMethod> methods
                            = new ArrayList<InstanceFrameworkMethod>();
                    for (int i = 0; i < parametersSets.length; i++) {
                        Object paramSet = parametersSets[i];
                        String name = namingStrategy.getTestCaseName(i, paramSet);
                        String uniqueMethodId = Utils.uniqueMethodId(i, paramSet, name());

                        Description description = Description
                                .createTestDescription(testClass.getName(), name, uniqueMethodId);
                        methods.add(new InstanceFrameworkMethod(
                                method, baseDescription.childlessCopy(),
                                description, paramSet));
                    }

                    return new ParameterisedFrameworkMethod(method, baseDescription, methods);
                }

                warnIfNoParamsGiven();
            } else {
                verifyMethodCanBeRunByStandardRunner(frameworkMethod);
            }

            // The method to use if it was ignored or was parameterized but had no parameters.
            return new NonParameterisedFrameworkMethod(method, baseDescription, isIgnored());
        }

        private void verifyMethodCanBeRunByStandardRunner(FrameworkMethod method) {
            List<Throwable> errors = new ArrayList<Throwable>();
            method.validatePublicVoidNoArg(false, errors);
            if (!errors.isEmpty()) {
                throw new RuntimeException(errors.get(0));
            }
        }

        private boolean describeFlat() {
            return System.getProperty("JUnitParams.flat") != null;
        }

    }
}
