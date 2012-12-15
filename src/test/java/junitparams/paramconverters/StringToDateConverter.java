package junitparams.paramconverters;

import java.text.*;
import java.util.*;

import junitparams.*;
import junitparams.converters.*;

public class StringToDateConverter implements ParamConverter<Date> {

    public Date convert(Object param, String options) {
        try {
            return new SimpleDateFormat(options).parse(param.toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
