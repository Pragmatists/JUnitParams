package junitparams;

import org.junit.runners.model.*;

public class ParameterisedTestMethodRunner {

    private int count;
    private final FrameworkMethod method;

    public ParameterisedTestMethodRunner(FrameworkMethod method) {
        this.method = method;
    }

    public int nextCount() {
        return count++;
    }

    Object[] paramsFromAnnotation(ParameterisedTestClassRunner parameterisedTestClassRunner) {
        Parameters parametersAnnotation = method.getAnnotation(Parameters.class);
        Object[] params = parameterisedTestClassRunner.paramsFromValue(parametersAnnotation);

        if (params.length == 0)
            params = parameterisedTestClassRunner.paramsFromSource(parametersAnnotation);

        if (params.length == 0)
            params = parameterisedTestClassRunner.paramsFromMethod(parametersAnnotation, method);

        if (params.length == 0)
            throw new RuntimeException(
                    "No parameters found, even though the method is defined as Prameterised. "
                            + "There aren't any params in the annotation, there's no test class method providing the params and no external provider...");

        return params;
    }

    public Object currentParamsFromAnnotation(ParameterisedTestClassRunner parameterisedTestClassRunner) {
        return paramsFromAnnotation(parameterisedTestClassRunner)[nextCount()];
    }

}
