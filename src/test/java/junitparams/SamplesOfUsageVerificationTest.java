package junitparams;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import junitparams.usage.SamplesOfUsageTest;

public class SamplesOfUsageVerificationTest {

    @Test
    public void verifyNoTestsIgnoredInSamplesOfUsageTest() {
        Result result = JUnitCore.runClasses(SamplesOfUsageTest.class);

        assertEquals(0, result.getFailureCount());
        assertEquals(0, result.getIgnoreCount());
    }

}
