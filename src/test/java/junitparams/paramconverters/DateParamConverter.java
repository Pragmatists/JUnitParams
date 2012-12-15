package junitparams.paramconverters;

import java.text.*;
import java.util.*;

import junitparams.*;

public class DateParamConverter implements ParamConverter<Date> {

    public Date convert(Object param) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(param.toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
