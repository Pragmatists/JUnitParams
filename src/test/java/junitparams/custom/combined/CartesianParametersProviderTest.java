package junitparams.custom.combined;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class CartesianParametersProviderTest {

    private static Verifier verifier = new Verifier();

    private Iterable<String> suppliesForCalledWithCartesianProduct() {
        return Arrays.asList("1", "2");
    }

    private Iterable<String> parametersForCalledWithCartesianProductAndTestMethod() {
        return Arrays.asList("1", "2");
    }

    private static class AnotherParameterSource {
        private Iterable<String> suppliesForCalledWithCartesianProduct() {
            return Arrays.asList("1", "2");
        }
    }

    @Test
    @CartesianParameters({
        @Parameters({"a", "b"}),
        @Parameters({"1", "2"})
    })
    public void calledWithCartesianProduct(String character, String number) {
        verifier.called(character, number);
    }

    @Test
    @CartesianParameters({
            @Parameters({"a", "b"}),
            @Parameters(method = "suppliesForCalledWithCartesianProduct")
    })
    public void calledWithCartesianProductAndMethod(String character, String number) {
        verifier.called(character, number);
    }

    @Test
    @CartesianParameters({
            @Parameters({"a", "b"}),
            @Parameters(source = AnotherParameterSource.class, method = "suppliesForCalledWithCartesianProduct")
    })
    public void calledWithCartesianProductAndExternalMethod(String character, String number) {
        verifier.called(character, number);
    }

    @Test
    @CartesianParameters({
            @Parameters({"a", "b"}),
            @Parameters
    })
    public void calledWithCartesianProductAndTestMethod(String character, String number) {
        verifier.called(character, number);
    }

    @AfterClass
    public static void verify() {
        assertThat(verifier.getCalls()).containsOnly(
                new Verifier.Call("a", "1"),
                new Verifier.Call("b", "1"),
                new Verifier.Call("a", "2"),
                new Verifier.Call("b", "2")
        );
    }

    private static class Verifier {

        private List<Call> calls = new LinkedList<Call>();

        void called(String firstParam, String anotherParam){
            calls.add(new Call(firstParam, anotherParam));
        }

        List<Call> getCalls() {
            return calls;
        }

        private static class Call {

            private final String firstParam;
            private final String anotherParam;

            Call(String firstParam, String anotherParam) {
                this.firstParam = firstParam;
                this.anotherParam = anotherParam;
            }

            @Override
            public String toString() {
                return "Call{" +
                        "'" + firstParam + '\'' +
                        ",'" + anotherParam + '\'' +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }

                Call call = (Call) o;

                return firstParam.equals(call.firstParam) && anotherParam.equals(call.anotherParam);

            }

            @Override
            public int hashCode() {
                int result = firstParam.hashCode();
                result = 31 * result + anotherParam.hashCode();
                return result;
            }
        }
    }
}
