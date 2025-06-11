package corp.bs.mm.maspk.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NrToruValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NrToruValidation {
    String message() default "Numer toru nie może być większy niż liczba torów na stacji";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
