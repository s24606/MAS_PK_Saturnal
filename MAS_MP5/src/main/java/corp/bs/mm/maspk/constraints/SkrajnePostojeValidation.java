package corp.bs.mm.maspk.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SkrajnePostojeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SkrajnePostojeValidation {
    String message() default "Jedno Polaczenie moze miec po jednym przypisanym Postoju z PlanowanyCzasOdjazdu i PlanowanyCzasPrzyjazdu równym null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
