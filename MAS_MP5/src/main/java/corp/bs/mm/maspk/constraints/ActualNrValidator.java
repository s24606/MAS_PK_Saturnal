package corp.bs.mm.maspk.constraints;

import corp.bs.mm.maspk.model.BiletBezposredni;
import corp.bs.mm.maspk.model.Miejsce;
import corp.bs.mm.maspk.model.Wagon;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;

public class ActualNrValidator  implements ConstraintValidator<ActualNrValidation, BiletBezposredni>  {
    @Override
    public void initialize(ActualNrValidation constraintAnnotation) {
        // Inicjalizacja walidatora - nie jest wymagana w tym przypadku
    }

    @Override
    public boolean isValid(BiletBezposredni bilet, ConstraintValidatorContext context) {
        Integer nrMiejsca = bilet.getNrMiejsca();
        Integer nrWagonu = bilet.getNrWagonu();

        if(nrMiejsca==null && nrWagonu==null) {
            return true;
        }

        if(nrMiejsca==null || nrWagonu==null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Tylko jeden z atrybutów określających miejsce biletu ma przypisaną wartość -  nrWagonu = "
                                    + (nrWagonu == null ? "null" : nrWagonu) + ", nrMiejsca = "
                                    + (nrMiejsca == null ? "null" : nrMiejsca)+". ")
                    .addPropertyNode("nrMiejsca, nrWagonu")
                    .addConstraintViolation();
            return false;
        }

        boolean isNrWagonuValid = false;
        boolean isNrMiejscaValid = false;
        ArrayList <Wagon> wagony = new ArrayList<>( bilet.getPolaczenie().getPociagKursujacy().getWagony() );
        for(Wagon w : wagony) {
            if (w.getNrWagonu() == nrWagonu) {
                isNrWagonuValid = true;
            }
            for(Miejsce m : w.getMiejsca()){
                if (m.getNrMiejsca() == nrMiejsca) {
                    isNrMiejscaValid = true;
                }
            }
        }

        if (isNrWagonuValid && isNrMiejscaValid)
        {
            return true;
        }
        else{
            return false;
        }


    }
}
