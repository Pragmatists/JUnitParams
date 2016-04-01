package junitparams.custom;

import junitparams.internal.Utils;
import junitparams.internal.util.Cartesian;

import java.util.ArrayList;
import java.util.List;

public class CombinedParametersProvider implements ParametersProvider<CombinedParameters>{

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

        Object[] resultOne = Cartesian.getCartesianProductOf(list);
        return resultOne;
    }
}
