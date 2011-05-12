package pragmatists.parameterised;

import java.lang.annotation.*;

import javax.lang.model.type.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Parameters {
    String[] value() default {};

    Class source() default NullType.class;
}
