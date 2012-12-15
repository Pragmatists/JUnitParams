package junitparams.paramconverters;

import junitparams.*;

public class LetterToNumberConverter implements ParamConverter<Integer> {

    public Integer convert(Object param) {
        return param.toString().charAt(0) - 64;
    }

}
