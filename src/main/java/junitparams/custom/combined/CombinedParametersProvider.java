package junitparams.custom.combined;

import java.util.ArrayList;
import java.util.List;

import junitparams.custom.ParametersProvider;
import junitparams.internal.Utils;
import org.junit.runners.model.FrameworkMethod;

public class CombinedParametersProvider implements ParametersProvider<CombinedParameters> {

    private CombinedParameters combinedParameters;

    @Override
    public void initialize(CombinedParameters parametersAnnotation, FrameworkMethod frameworkMethod) {
        this.combinedParameters = parametersAnnotation;
    }

    @Override
    public Object[] getParameters() {
        List<Object[]> list = new ArrayList<Object[]>();
        for(String parameterArray : combinedParameters.value()) {
            list.add(Utils.splitAtCommaOrPipe(parameterArray));
        }

        return Cartesian.getCartesianProductOf(list);
    }
}
