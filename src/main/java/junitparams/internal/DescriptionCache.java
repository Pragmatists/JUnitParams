package junitparams.internal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.Description;

enum DescriptionCache {
    INSTANCE;

    final Map<Method, Description> descriptions = new HashMap<Method, Description>();

    Description putIfAbsent(Method method, Description defaultDescription) {
        Description parentDescription = descriptions.get(method);
        if (parentDescription == null) {
            parentDescription = defaultDescription;
            descriptions.put(method, parentDescription);
        }
        return parentDescription;
    }
}
