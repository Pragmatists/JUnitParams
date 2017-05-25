package junitparams.naming;

import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.assertj.core.api.Assertions.*;

@RunWith(JUnitParamsRunner.class)
public class NamingStrategyIsUsedByRunnerTest {

    @AfterClass
    public static void checkTestCaseNames() {
        Description rootDescription = getTestClassDescription();
        String className = "(" + NamingStrategyIsUsedByRunnerTest.class.getCanonicalName() + ")";

        Description sampleMethodDescription = getChildDescriptionByName(rootDescription, "sampleMethod");

        assertThat(sampleMethodDescription.getChildren()).extracting("displayName").containsExactly(
                "[0] Well formed name of sampleMethod with param1" + className,
                "[1] Well formed name of sampleMethod with param2" + className);
    }

    // Android-changed: CTS and AndroidJUnitRunner rely on specific format to test names, changing
    // them will prevent CTS and AndroidJUnitRunner from working properly; see b/36541809
    @Ignore
    @Test
    @Parameters({"param1", "param2"})
    @TestCaseName("[{index}] Well formed name of {method} with {params}")
    public void sampleMethod(String parameter) {
    }

    private static Description getTestClassDescription() {
        return Request.aClass(NamingStrategyIsUsedByRunnerTest.class).getRunner().getDescription();
    }

    private static Description getChildDescriptionByName(Description parent, String expectedName) {
        for (Description childDescription : parent.getChildren()) {
            if (expectedName.equals(childDescription.getDisplayName())) {
                return childDescription;
            }
        }

        return null;
    }
}
