package junitparams;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class EnumsAsParamsTest {

    @Test
    @Parameters({ "SOME_VALUE", "OTHER_VALUE" })
    public void passEnumAsParam(PersonType person) {
    }
}
