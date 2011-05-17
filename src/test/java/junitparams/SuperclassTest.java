package junitparams;

import static junitparams.JUnitParamsRunner.*;

public abstract class SuperclassTest {

    protected Object[] paramsForIsAdult() {
        return $($(11, false),
                $(17, false),
                $(18, true),
                $(22, true));
    }
}