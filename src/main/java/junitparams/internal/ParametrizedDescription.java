package junitparams.internal;

import org.junit.runner.Description;

import java.lang.annotation.Annotation;
import junitparams.naming.TestCaseNamingStrategy;

class ParametrizedDescription {

    private TestCaseNamingStrategy namingStrategy;
    private String testClassName;
    private String methodName;
    private Annotation[] annotations;

    ParametrizedDescription(TestCaseNamingStrategy namingStrategy, String testClassName, String methodName,
                            Annotation[] annotations) {
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

            // Creating a suite description for a test to maintain the annotations and still use a uniqueMethodId.
            // Manually formatting the name to match the format used for test descriptions with uniqueMethodId.
            Description description = Description.createSuiteDescription(String.format("%s(%s)", name, testClassName),
                    uniqueMethodId, annotations);
            parametrised.addChild(description);
        }
        return parametrised;
    }
}
