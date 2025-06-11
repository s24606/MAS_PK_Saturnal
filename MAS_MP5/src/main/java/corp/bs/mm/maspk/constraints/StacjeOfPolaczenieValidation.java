package corp.bs.mm.maspk.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StacjeOfPolaczenieValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StacjeOfPolaczenieValidation {
    String message() default "Stacje odjazdu i przyjazdu muszą należeć do postojów powiązanych z połączeniem";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

