package corp.bs.mm.masmp5.constraints;

import corp.bs.mm.masmp5.enums.TypOsoby;
import corp.bs.mm.masmp5.model.Osoba;
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
