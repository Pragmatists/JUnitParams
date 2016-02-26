package junitparams.custom;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(JUnitParamsRunner.class)
public class CustomParametersProviderTest {

    @Test
    @CustomParameters(provider = SimpleHelloProvider.class)
    public void runWithParametersFromCustomProvider(String param) {
        assertThat(param).isEqualTo("hello");
    }

    @Test
    @HelloParameters(hello = "Hi")
    public void runWithParametersFromCustomAnnotation(String param) {
        assertThat(param).isEqualTo("Hi");
    }


    @Retention(RetentionPolicy.RUNTIME)
    @CustomParameters(provider = CustomHelloProvider.class)
    public @interface HelloParameters {
        String hello();
    }

    public static class SimpleHelloProvider implements ParametersProvider<CustomParameters> {
        @Override
        public void initialize(CustomParameters parametersAnnotation) {
        }

        @Override
        public Object[] getParameters() {
            return new Object[]{"hello", "hello"};
        }
    }

    public static class CustomHelloProvider implements ParametersProvider<HelloParameters> {

        private String hello;

        @Override
        public void initialize(HelloParameters parametersAnnotation) {
            hello = parametersAnnotation.hello();
        }

        @Override
        public Object[] getParameters() {
            return new Object[]{hello, hello};
        }
    }
}

