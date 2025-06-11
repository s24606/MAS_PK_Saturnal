package corp.bs.mm.masmp5.constraints;

import corp.bs.mm.masmp5.model.BiletBezposredni;
import corp.bs.mm.masmp5.model.Polaczenie;
import corp.bs.mm.masmp5.model.Postoj;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;

public class SkrajnePostojeValidator implements ConstraintValidator<SkrajnePostojeValidation, Polaczenie> {
    @Override
    public void initialize(SkrajnePostojeValidation constraintAnnotation) {
        // Inicjalizacja walidatora - nie jest wymagana w tym przypadku
    }

    @Override
    public boolean isValid(Polaczenie polaczenie, ConstraintValidatorContext context) {

        if (polaczenie.getPostoje() == null) {
            return true;
        } else {

            ArrayList<Postoj> postoje = new ArrayList<>(polaczenie.getPostoje());
            if (postoje.isEmpty())
                return true;
            else {
                int postojeS = 0, postojeE = 0;
                for (Postoj pos : postoje) {
                    if (pos.getPlanowanyCzasPrzyjazdu() == null)
                        postojeS++;
                    else {
                        if (pos.getPlanowanyCzasOdjazdu() == null)
                            postojeE++;
                    }
                }

                if (postojeS > 1 || postojeE > 1) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(
                                    "Więcej niż jeden Postoj z " +
                                            (postojeS > 1 ? "planowanyCzasOdjazdu" : "") +
                                            ((postojeS > 1 && postojeE > 1) ? " oraz " : "") +
                                            (postojeE > 1 ? "planowanyCzasPrzyjazdu" : "") +
                                            " = null w jednym Polaczenie.")
                            .addPropertyNode("postoje")
                            .addConstraintViolation();
                    return false;
                } else {
                    return true;
                }
            }
        }
    }
}