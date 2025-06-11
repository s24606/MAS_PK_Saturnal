package corp.bs.mm.maspk.constraints;

import corp.bs.mm.maspk.model.Postoj;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CzasPostojuValidator implements ConstraintValidator<CzasPostojuValidation, Postoj> {

    @Override
    public boolean isValid(Postoj postoj, ConstraintValidatorContext context) {
        if (postoj == null) return true;

        boolean planowanyOK = postoj.getPlanowanyCzasPrzyjazdu() == null
                || postoj.getPlanowanyCzasOdjazdu() == null
                || postoj.getPlanowanyCzasOdjazdu().isAfter(postoj.getPlanowanyCzasPrzyjazdu());

        boolean faktycznyOK = postoj.getFaktycznyCzasPrzyjazdu() == null
                || postoj.getFaktycznyCzasOdjazdu() == null
                || postoj.getFaktycznyCzasOdjazdu().isAfter(postoj.getFaktycznyCzasPrzyjazdu());

        return planowanyOK && faktycznyOK;
    }
}
