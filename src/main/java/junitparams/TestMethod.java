package junitparams;

import java.util.*;

import org.junit.runners.model.*;

public class TestMethod {
    protected FrameworkMethod frameworkMethod;
	private Class<?> testClass;

    public TestMethod(FrameworkMethod method, TestClass testClass) {
        this.frameworkMethod = method;
        this.testClass = testClass.getJavaClass();
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

        return frameworkMethod.equals(((TestMethod) obj).frameworkMethod);
    }

	Class<?> testClass() {
		return testClass;
	}
}