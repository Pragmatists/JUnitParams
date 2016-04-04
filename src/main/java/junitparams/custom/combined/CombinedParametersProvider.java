package junitparams.custom.combined;

import java.util.ArrayList;
import java.util.List;

import junitparams.custom.ParametersProvider;
import junitparams.internal.Utils;

public class CombinedParametersProvider implements ParametersProvider<CombinedParameters> {

    private CombinedParameters combinedParameters;

    @Override
    public void initialize(CombinedParameters parametersAnnotation) {
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
