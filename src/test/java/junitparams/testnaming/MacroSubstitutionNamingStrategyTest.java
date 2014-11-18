package junitparams.testnaming;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.internal.TestMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Method;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class MacroSubstitutionNamingStrategyTest {

    @Parameters
    public Object parametersForTestNaming() {
        return $(
            $("withoutTestCaseAnnotation", "[0] value (withoutTestCaseAnnotation)"),
            $("withAnnotationWithoutTemplate", "[0] value (withAnnotationWithoutTemplate)"),
            $("withEmptyTemplate", "[0] value (withEmptyTemplate)"),
            $("withoutMacro", "plain name"),
            $("withIndexMacro", "0"),
            $("withParamsMacro", "value"),
            $("withMethodNameMacro", "withMethodNameMacro"),
            $("withCombinationOfMacros", "0: withCombinationOfMacros(value)"),
            $("withMacroNameWrittenInDifferentCase", "value value value")
        );
    }

    @Test
    @Parameters
    public void testNaming(String methodName, String expectedTestCaseName) throws NoSuchMethodException {
        TestCaseNamingStrategy strategy = createNamingStrategyForMethod(methodName, String.class);

        String name = strategy.getTestCaseName(0, $("value"));

        assertEquals(expectedTestCaseName, name);
    }

    public void withoutTestCaseAnnotation(String param1) { }

    @TestCaseName("plain name")
    public void withoutMacro(String param1) { }

    @TestCaseName("{index}")
    public void withIndexMacro(String param1) { }

    @TestCaseName("{params}")
    public void withParamsMacro(String param1) { }

    @TestCaseName("{method}")
    public void withMethodNameMacro(String param1) { }

    @TestCaseName
    public void withAnnotationWithoutTemplate(String param1) { }

    @TestCaseName("")
    public void withEmptyTemplate(String param1) { }

    @TestCaseName("{index}: {method}({params})")
    public void withCombinationOfMacros(String param1) { }

    @TestCaseName("{params} {PARAMS} {PaRams}")
    public void withMacroNameWrittenInDifferentCase(String param1) { }

    private TestCaseNamingStrategy createNamingStrategyForMethod(String name, Class... parameterTypes) throws NoSuchMethodException {
        TestMethod method = getCurrentClassMethod(name, parameterTypes);

        return new MacroSubstitutionNamingStrategy(method);
    }

    private TestMethod getCurrentClassMethod(String name, Class... parameterTypes) throws NoSuchMethodException {
        final Method method = MacroSubstitutionNamingStrategyTest.class.getMethod(name, parameterTypes);
        return new TestMethod(new FrameworkMethod(method),
                new TestClass(this.getClass()));
    }
}
