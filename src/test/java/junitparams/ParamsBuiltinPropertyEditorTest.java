package junitparams;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class ParamsBuiltinPropertyEditorTest {

	@Test
	@Parameters({
			"123456789.9876543212 , 123456789.9876543212",
			"-99887766.5544332211 , -99887766.5544332211000",
			"0.000000000000000001 , 0.000000000000000001",
			"0, 0"
	})
	public void shouldParseNormalBigDecimals(BigDecimal num, String numAsText) {
		assertThat(num).isEqualByComparingTo(numAsText);
	}

	@Test
	@Parameters({ "", " ", "\t" })
	public void shouldParseBlankAsNullBigDecimal(BigDecimal num) {
		assertThat(num).isNull();
	}

	@Test
	@Parameters({
			"123456789 , 123456789",
			"-99887766 , -99887766",
			"0         , 0"
	})
	public void shouldParseNormalBigIntegers(BigInteger num, long referenceNum) {
		assertThat(num).isEqualTo(BigInteger.valueOf(referenceNum));
	}

	@Test
	@Parameters({ "", " ", "\t" })
	public void shouldParseBlankAsNullBigInteger(BigInteger num) {
		assertThat(num).isNull();
	}

}
