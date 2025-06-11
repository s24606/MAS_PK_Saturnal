package corp.bs.mm.maspk.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RolesAttributesValidator.class)
@Documented
public @interface RolesAttributesValidation {
    String message() default "Niespójne dane względem ról osoby";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
