package corp.bs.mm.masmp5.model.constraints;

import corp.bs.mm.masmp5.model.BiletPrzesiadkowy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CzasPrzyjazduPoOdjezdzieValidator implements ConstraintValidator<CzasPrzyjazduPoOdjezdzie, BiletPrzesiadkowy> {

    @Override
    public boolean isValid(BiletPrzesiadkowy bilet, ConstraintValidatorContext context) {
        if (bilet == null || bilet.getCzasOdjazdu() == null || bilet.getCzasPrzyjazdu() == null) {
            return true;
        }
        return bilet.getCzasPrzyjazdu().isAfter(bilet.getCzasOdjazdu());
    }
}
