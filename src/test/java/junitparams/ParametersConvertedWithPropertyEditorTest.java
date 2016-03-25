package junitparams;

import java.beans.PropertyEditorManager;
import java.beans.PropertyEditorSupport;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.*;

@RunWith(JUnitParamsRunner.class)
public class ParametersConvertedWithPropertyEditorTest {

    @BeforeClass
    public static void registerEditors() {
        PropertyEditorManager.registerEditor(StringWrapper.class, StringWrapperPropertyEditor.class);
    }

    @Test
    @Parameters({"wrapped , wrapped"})
    public void convertsToCustomType(StringWrapper wrapper, String text) {
        assertThat(wrapper.getText()).isEqualTo(text);
    }

    public static class StringWrapperPropertyEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            setValue(new StringWrapper(text));
        }
    }

    private static class StringWrapper {
        private String text;

        StringWrapper(String text) {
            this.text = text;
        }

        String getText() {
            return text;
        }
    }
}
