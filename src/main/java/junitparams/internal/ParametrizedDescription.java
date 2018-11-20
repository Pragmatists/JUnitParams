package junitparams.internal;

import junitparams.naming.TestCaseNamingStrategy;
import org.junit.runner.Description;

import java.lang.annotation.Annotation;


class ParametrizedDescription {

    private TestCaseNamingStrategy namingStrategy;
    private String testClassName;
    private String methodName;
    private Annotation[] annotations;

    ParametrizedDescription(TestCaseNamingStrategy namingStrategy, String testClassName, String methodName,
            final Annotation[] annotations) {
        this.namingStrategy = namingStrategy;
        this.testClassName = testClassName;
        this.methodName = methodName;
        this.annotations = annotations;
    }

    Description parametrizedDescription(Object[] params) {
        Description parametrised = Description.createSuiteDescription(methodName);
        for (int i = 0; i < params.length; i++) {
            Object paramSet = params[i];
            String name = namingStrategy.getTestCaseName(i, paramSet);
            String uniqueMethodId = Utils.uniqueMethodId(i, paramSet, methodName);
            parametrised.addChild(
                    Description.createSuiteDescription(formatDisplayName(testClassName, name), uniqueMethodId, annotations)
            );
        }
        return parametrised;
    }

    private String formatDisplayName(String className, String name) {
        return String.format("%s(%s)", name, className);
    }
}
