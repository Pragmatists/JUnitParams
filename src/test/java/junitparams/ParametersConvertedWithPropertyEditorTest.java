package junitparams;

import static org.assertj.core.api.Assertions.*;

import java.beans.PropertyEditorManager;
import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class ParametersConvertedWithPropertyEditorTest {

	@BeforeClass
	public static void registerEditors() {
		PropertyEditorManager.registerEditor(BigDecimal.class, BigDecimalPropertyEditor.class);
	}

	@Test
	@Parameters({
			"123456789.9876543212 , 123456789.9876543212",
			"-99887766.5544332211 , -99887766.5544332211000",
			"0.000000000000000001 , 0.000000000000000001",
			"0, 0"
	})
	public void convertsToBigDecimal(BigDecimal num, String numAsText) {
		assertThat(num).isEqualByComparingTo(numAsText);
	}

	public static class BigDecimalPropertyEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
			setValue(new BigDecimal(text));
        }
    }
}
