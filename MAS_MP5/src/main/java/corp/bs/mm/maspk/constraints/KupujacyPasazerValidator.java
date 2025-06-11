package corp.bs.mm.maspk.constraints;

import corp.bs.mm.maspk.enums.TypOsoby;
import corp.bs.mm.maspk.model.Osoba;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class KupujacyPasazerValidator implements ConstraintValidator<KupujacyPasazerValidation, Osoba> {

    @Override
    public boolean isValid(Osoba kupujacy, ConstraintValidatorContext context) {
        if (kupujacy == null || kupujacy.getRole() == null) {
            return false;
        }

        return kupujacy.getRole().contains(TypOsoby.PASAZER);
    }
}
