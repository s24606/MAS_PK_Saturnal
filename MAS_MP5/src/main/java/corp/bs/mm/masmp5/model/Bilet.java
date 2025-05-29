package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.statusBiletu;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

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
    private statusBiletu status = statusBiletu.ZAREZERWOWANY;;

    @ManyToOne
    @JoinColumn(name = "stacjaOdjazd_id")
    private Stacja stacjaOdjazd;

    @ManyToOne
    @JoinColumn(name = "stacjaPrzyjazd_id")
    private Stacja stacjaPrzyjazd;

    @OneToMany(mappedBy = "bilet", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Okaziciel> okaziciele = new HashSet<>();


    @AssertTrue(message = "Stacja odjazdu i przyjazdu muszą być różne")
    private boolean isStacjeRozne() {
        if (stacjaOdjazd == null || stacjaPrzyjazd == null) {
            return true;
        }
        return !stacjaOdjazd.equals(stacjaPrzyjazd);
    }
}


