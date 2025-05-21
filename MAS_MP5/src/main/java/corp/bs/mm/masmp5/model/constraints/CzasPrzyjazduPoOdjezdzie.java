package corp.bs.mm.masmp5.model.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CzasPrzyjazduPoOdjezdzieValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CzasPrzyjazduPoOdjezdzie {
    String message() default "Czas przyjazdu musi być po czasie odjazdu";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
