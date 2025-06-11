package corp.bs.mm.masmp5.constraints;

import corp.bs.mm.masmp5.enums.TypOsoby;
import corp.bs.mm.masmp5.model.Osoba;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RolesAttributesValidator implements ConstraintValidator<RolesAttributesValidation, Osoba> {

    @Override
    public boolean isValid(Osoba osoba, ConstraintValidatorContext context) {
        if (osoba == null) {
            return true;
        }

        boolean isValid = true;

        context.disableDefaultConstraintViolation();

        if (osoba.getRole().contains(TypOsoby.PASAZER) && osoba.getUlga() == null) {
            context.buildConstraintViolationWithTemplate("Rola PASAZER wymaga przypisanej ulgi.")
                    .addPropertyNode("ulga")
                    .addConstraintViolation();
            isValid = false;
        }

        if (osoba.getRole().contains(TypOsoby.PRACOWNIK) && osoba.getKodSluzbowy() == null) {
            context.buildConstraintViolationWithTemplate("Rola PRACOWNIK wymaga przypisanego kodu służbowego.")
                    .addPropertyNode("kodSluzbowy")
                    .addConstraintViolation();
            isValid = false;
        }

        if (!osoba.getRole().contains(TypOsoby.PASAZER) && osoba.getUlga() != null) {
            context.buildConstraintViolationWithTemplate("Nie można przypisać ulgi bez roli PASAZER.")
                    .addPropertyNode("ulga")
                    .addConstraintViolation();
            isValid = false;
        }

        if (!osoba.getRole().contains(TypOsoby.PRACOWNIK) && osoba.getKodSluzbowy() != null) {
            context.buildConstraintViolationWithTemplate("Nie można przypisać kodu służbowego bez roli PRACOWNIK.")
                    .addPropertyNode("kodSluzbowy")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
