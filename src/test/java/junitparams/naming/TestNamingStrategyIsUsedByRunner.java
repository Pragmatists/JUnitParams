package junitparams.naming;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnitParamsRunner.class)
public class TestNamingStrategyIsUsedByRunner {
    
    @AfterClass
    public static void checkTestCaseNames() {
        Description rootDescription = getTestClassDescription();
        Description sampleMethodDescription = getChildDescriptionByName(rootDescription, "sampleMethod");
        String className = "(" + TestNamingStrategyIsUsedByRunner.class.getCanonicalName() + ")";

        assertHasChildWithName(sampleMethodDescription, "[0] Well formed name of sampleMethod with param1" + className);
        assertHasChildWithName(sampleMethodDescription, "[1] Well formed name of sampleMethod with param2" + className);
    }

    @Test
    @Parameters({"param1", "param2"})
    @TestCaseName("[{index}] Well formed name of {method} with {params}")
    public void sampleMethod(String parameter) { }

    private static Description getTestClassDescription() {
        return Request.aClass(TestNamingStrategyIsUsedByRunner.class).getRunner().getDescription();
    }

    private static Description getChildDescriptionByName(Description parent, String expectedName) {
        for (Description childDescription : parent.getChildren()) {
            if (expectedName.equals(childDescription.getDisplayName())) {
                return childDescription;
            }
        }

        return null;
    }

    private static void assertHasChildWithName(Description testingMethodDescription, String expectedName) {
        assertNotNull(getChildDescriptionByName(testingMethodDescription, expectedName));
    }
}