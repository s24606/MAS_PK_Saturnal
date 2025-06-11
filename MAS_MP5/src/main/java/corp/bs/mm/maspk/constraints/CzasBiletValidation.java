package corp.bs.mm.maspk.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CzasBiletValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CzasBiletValidation {
    String message() default "Czas przyjazdu musi być po czasie odjazdu";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
