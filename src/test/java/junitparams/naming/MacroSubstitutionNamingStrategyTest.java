package junitparams.naming;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.internal.TestMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class MacroSubstitutionNamingStrategyTest {

    public Object parametersForTestNaming() {
        return new Object[]{new Object[]{"withoutTestCaseAnnotation", "[0] value (withoutTestCaseAnnotation)"}, new Object[]{"withAnnotationWithoutTemplate", "[0] value (withAnnotationWithoutTemplate)"}, new Object[]{"withEmptyTemplate", "[0] value (withEmptyTemplate)"}, new Object[]{"whenTemplateResultedToEmptyName", "[0] value (whenTemplateResultedToEmptyName)"}, new Object[]{"withoutMacro", "plain name"}, new Object[]{"withIndexMacro", "0"}, new Object[]{"withParamsMacro", "value"}, new Object[]{"withMethodNameMacro", "withMethodNameMacro"}, new Object[]{"withCombinationOfMacros", "0: withCombinationOfMacros(value)"}, new Object[]{"withMacroNameWrittenInDifferentCase", "value value value"}, new Object[]{"withMethodParameterIndexInsideArgumentsArray", "value"}, new Object[]{"withMethodParameterIndexOutsideArgumentsArray", "Here is 100 argument:"}, new Object[]{"whenGivenMacroNotExists", "{not_existing_macro}"}};
    }

    @Test
    @Parameters
    public void testNaming(String methodName, String expectedTestCaseName) throws NoSuchMethodException {
        TestCaseNamingStrategy strategy = createNamingStrategyForMethod(methodName, String.class);

        String name = strategy.getTestCaseName(0, new Object[]{"value"});

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

    @TestCaseName("{0}")
    public void withMethodParameterIndexInsideArgumentsArray(String param1) { }

    @TestCaseName("Here is 100 argument:{100}")
    public void withMethodParameterIndexOutsideArgumentsArray(String param1) { }

    @TestCaseName("{100}")
    public void whenTemplateResultedToEmptyName(String param1) { }

    @TestCaseName("{not_existing_macro}")
    public void whenGivenMacroNotExists(String param1) { }

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
