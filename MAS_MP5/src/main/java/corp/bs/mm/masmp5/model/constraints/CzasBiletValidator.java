package corp.bs.mm.masmp5.model.constraints;

import corp.bs.mm.masmp5.model.BiletPrzesiadkowy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CzasBiletValidator implements ConstraintValidator<CzasBiletValidation, BiletPrzesiadkowy> {

    @Override
    public boolean isValid(BiletPrzesiadkowy bilet, ConstraintValidatorContext context) {
        if (bilet == null || bilet.getCzasOdjazdu() == null || bilet.getCzasPrzyjazdu() == null) {
            return false;
        }
        return bilet.getCzasPrzyjazdu().isAfter(bilet.getCzasOdjazdu());
    }
}
