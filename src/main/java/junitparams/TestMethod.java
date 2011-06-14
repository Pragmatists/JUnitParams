package junitparams;

import java.util.*;

import org.junit.runners.model.*;

public class TestMethod {
    protected FrameworkMethod frameworkMethod;

    public TestMethod(FrameworkMethod method) {
        this.frameworkMethod = method;
    }

    public String name() {
        return frameworkMethod.getName();
    }

    public static List<TestMethod> listFrom(List<FrameworkMethod> annotatedMethods) {
        List<TestMethod> methods = new ArrayList<TestMethod>();
        for (FrameworkMethod frameworkMethod : annotatedMethods) {
            methods.add(new TestMethod(frameworkMethod));
        }
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
}