package corp.bs.mm.maspk.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CzasPostojuValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CzasPostojuValidation {
    String message() default "Czas odjazdu musi być po czasie przyjazdu (zarowno planowany jak i faktyczny)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
