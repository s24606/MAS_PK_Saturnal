package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.StatusBiletu;
import corp.bs.mm.masmp5.model.constraints.KupujacyPasazerValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Bilet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long biletId;

    @NotNull(message = "Price is mandatory")
    @Min(0)
    private double cena;

    @NotNull
    @Builder.Default
    private StatusBiletu status = StatusBiletu.ZAREZERWOWANY;;

    @ManyToOne
    @JoinColumn(name = "stacjaOdjazd_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stacja stacjaOdjazd;

    @ManyToOne
    @JoinColumn(name = "stacjaPrzyjazd_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stacja stacjaPrzyjazd;

    @AssertTrue(message = "Stacja odjazdu i przyjazdu muszą być różne")
    private boolean isStacjeRozne() {
        if (stacjaOdjazd == null || stacjaPrzyjazd == null) {
            return true;
        }
        return !stacjaOdjazd.equals(stacjaPrzyjazd);
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "kupujacy_id", nullable = false, updatable = false)
    @KupujacyPasazerValidation
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Osoba kupujacy;
}


