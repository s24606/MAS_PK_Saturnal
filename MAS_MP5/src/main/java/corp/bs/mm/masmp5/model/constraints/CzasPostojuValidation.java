package corp.bs.mm.masmp5.model.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CzasPostojuValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CzasPostojuValidation {
    String message() default "Czas odjazdu musi być po czasie przyjazdu (planowany lub faktyczny)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
