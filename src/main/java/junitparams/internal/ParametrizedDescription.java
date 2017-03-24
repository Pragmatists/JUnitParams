package junitparams.internal;

import junitparams.naming.TestCaseNamingStrategy;
import org.junit.runner.Description;

class ParametrizedDescription {

    private TestCaseNamingStrategy namingStrategy;
    private String testClassName;
    private String methodName;

    ParametrizedDescription(TestCaseNamingStrategy namingStrategy, String testClassName, String methodName) {
        this.namingStrategy = namingStrategy;
        this.testClassName = testClassName;
        this.methodName = methodName;
    }

    Description parametrizedDescription(Object[] params) {
        Description parametrised = Description.createSuiteDescription(methodName);
        for (int i = 0; i < params.length; i++) {
            Object paramSet = params[i];
            String name = namingStrategy.getTestCaseName(i, paramSet);
            String uniqueMethodId = Utils.uniqueMethodId(i, paramSet, methodName);
            parametrised.addChild(
                    Description.createTestDescription(testClassName, name, uniqueMethodId)
            );
        }
        return parametrised;
    }
}
