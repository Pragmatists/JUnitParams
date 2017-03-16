package junitparams.internal.parameters;

import junitparams.NullType;
import junitparams.Parameters;
import org.junit.runners.model.FrameworkMethod;

import static com.google.common.base.Preconditions.checkNotNull;

class ParametersFromExternalClassMethod extends ParamsFromMethodCommon {

    private Parameters annotation;

    ParametersFromExternalClassMethod(FrameworkMethod frameworkMethod, Parameters annotation,
                                      ResultAdapter resultAdapter) {
        super(frameworkMethod, annotation.source(), annotation, resultAdapter);
        this.annotation = annotation;
    }

    @Override
    public boolean isApplicable() {
        return annotation != null
                && !annotation.source().isAssignableFrom(NullType.class)
                && !annotation.method().isEmpty();
    }
}