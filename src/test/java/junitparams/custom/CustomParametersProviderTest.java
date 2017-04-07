package junitparams.custom;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import org.junit.runners.model.FrameworkMethod;

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

    @Test
    @CustomParameters(provider = MethodNameReader.class)
    public void getDataFromFrameworkMethod(String name) throws Exception {
        assertThat(name).isEqualTo("getDataFromFrameworkMethod");
    }

    @Retention(RetentionPolicy.RUNTIME)
    @CustomParameters(provider = CustomHelloProvider.class)
    public @interface HelloParameters {
        String hello();
    }

    public static class SimpleHelloProvider implements ParametersProvider<CustomParameters> {
        @Override
        public void initialize(CustomParameters parametersAnnotation, FrameworkMethod frameworkMethod) {
        }

        @Override
        public Object[] getParameters() {
            return new Object[]{"hello", "hello"};
        }
    }

    public static class CustomHelloProvider implements ParametersProvider<HelloParameters> {

        private String hello;

        @Override
        public void initialize(HelloParameters parametersAnnotation, FrameworkMethod frameworkMethod) {
            hello = parametersAnnotation.hello();
        }

        @Override
        public Object[] getParameters() {
            return new Object[]{hello, hello};
        }
    }

    public static class MethodNameReader implements ParametersProvider<CustomParameters> {
        private FrameworkMethod frameworkMethod;

        @Override
        public void initialize(CustomParameters parametersAnnotation, FrameworkMethod frameworkMethod) {
            this.frameworkMethod = frameworkMethod;
        }

        @Override
        public Object[] getParameters() {
            return new Object[]{frameworkMethod.getName()};
        }
    }
}

