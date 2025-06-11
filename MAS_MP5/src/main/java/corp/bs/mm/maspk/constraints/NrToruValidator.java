package corp.bs.mm.maspk.constraints;

import corp.bs.mm.maspk.model.Postoj;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NrToruValidator implements ConstraintValidator<NrToruValidation, Postoj> {

    @Override
    public boolean isValid(Postoj postoj, ConstraintValidatorContext context) {
        if (postoj == null || postoj.getStacja() == null || postoj.getNrToru() == null) {
            return false;
        }

        return postoj.getNrToru() <= postoj.getStacja().getTory();
    }
}
