package corp.bs.mm.masmp5.constraints;

import corp.bs.mm.masmp5.model.BiletBezposredni;
import corp.bs.mm.masmp5.model.Postoj;
import corp.bs.mm.masmp5.model.Stacja;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class StacjeOfPolaczenieValidator implements ConstraintValidator<StacjeOfPolaczenieValidation, BiletBezposredni> {

    @Override
    public void initialize(StacjeOfPolaczenieValidation constraintAnnotation) {
        // Inicjalizacja walidatora - nie jest wymagana w tym przypadku
    }

    @Override
    public boolean isValid(BiletBezposredni bilet, ConstraintValidatorContext context) {
        if (bilet == null) {
            return true;
        }
        if (bilet.getPolaczenie() == null ||
                bilet.getStacjaOdjazd() == null ||
                bilet.getStacjaPrzyjazd() == null) {
            return true;
        }
        List<Postoj> postoje = bilet.getPolaczenie().getPostoje();
        if (postoje == null || postoje.isEmpty()) {
            return false;
        }

        // Pobranie wszystkich stacji z postojów połączenia
        Set<Stacja> stacjePolaczenia = postoje.stream()
                .map(Postoj::getStacja)
                .collect(Collectors.toSet());

        StringBuilder stacjeText = new StringBuilder("( ");
        boolean stacjaOdjazdValid = false;
        boolean stacjaPrzyjazdValid = false;
        Long stacjaOdjazdId = bilet.getStacjaOdjazd().getStacjaId();
        Long stacjaPrzyjazdId = bilet.getStacjaPrzyjazd().getStacjaId();

        // Sprawdzenie czy stacje naleza do połaczenia - po Id, ponieważ same obiekty mogą być w różnych instancjach
        for(Stacja st : stacjePolaczenia){
            stacjeText.append(st.getNazwa()).append(" ");
            if(Objects.equals(st.getStacjaId(), stacjaOdjazdId))
                stacjaOdjazdValid=true;
            if(Objects.equals(st.getStacjaId(), stacjaPrzyjazdId))
                stacjaPrzyjazdValid=true;
        }
        stacjeText.append(")");

        if (!stacjaOdjazdValid || !stacjaPrzyjazdValid) {
            context.disableDefaultConstraintViolation();
            if (!stacjaOdjazdValid) {
                context.buildConstraintViolationWithTemplate(
                                "Stacja odjazdu '" + bilet.getStacjaOdjazd().getNazwa() +
                                        "' nie należy do postojów połączenia '" + bilet.getPolaczenie().getPolaczenieId() + "' " + stacjeText)
                        .addPropertyNode("stacjaOdjazd")
                        .addConstraintViolation();
            }

            if (!stacjaPrzyjazdValid) {
                context.buildConstraintViolationWithTemplate(
                                "Stacja przyjazdu '" + bilet.getStacjaPrzyjazd().getNazwa() +
                                        "' nie należy do postojów połączenia '" + bilet.getPolaczenie().getPolaczenieId() + "' " + stacjeText)
                        .addPropertyNode("stacjaPrzyjazd")
                        .addConstraintViolation();
            }
            return false;
        }
        return true;
    }
}
