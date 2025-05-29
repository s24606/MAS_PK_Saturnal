package corp.bs.mm.masmp5.model.constraints;

import corp.bs.mm.masmp5.enums.TypOsoby;
import corp.bs.mm.masmp5.model.Osoba;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class KupujacyPasazerValidator implements ConstraintValidator<KupujacyPasazerValidation, Osoba> {

    @Override
    public boolean isValid(Osoba kupujacy, ConstraintValidatorContext context) {
        // Sprawdzanie, czy kupujący ma rolę PASAZER
        if (kupujacy == null || kupujacy.getRole() == null) {
            return false;
        }

        // Sprawdzenie, czy lista ról zawiera PASAZER
        return kupujacy.getRole().contains(TypOsoby.PASAZER);
    }
}
