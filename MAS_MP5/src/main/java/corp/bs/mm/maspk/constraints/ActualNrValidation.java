package corp.bs.mm.maspk.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ActualNrValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActualNrValidation {
    String message() default "Numer miejsca i wagonu musi wskazywać na istniejace miejsce w pociagu kursujacym w powaizanym polaczeniu";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
