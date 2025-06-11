package corp.bs.mm.masmp5.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Adnotacja niestandardowa
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = KupujacyPasazerValidator.class)
public @interface KupujacyPasazerValidation {
    String message() default "Kupujący musi mieć rolę PASAZER";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
