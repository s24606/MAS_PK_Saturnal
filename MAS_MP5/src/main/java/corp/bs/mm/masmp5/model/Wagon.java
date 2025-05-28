package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.typWagonu;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Wagon {
    private final double maksymalna_pojemnosc=1.3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long wagonId;

    @NotNull
    private int nrWagonu;

    @OneToMany(mappedBy = "wagon", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Miejsce> miejsca = new HashSet<>();

    @NotNull
    @Setter(AccessLevel.NONE)
    private typWagonu numeracja;

    @ManyToOne
    @JoinColumn(name = "pociag_id")
    private Pociag pociag;

    public int getPojemnosc() throws Exception {
        if(numeracja == typWagonu.ZNUMERACJA)
            return miejsca.size();
        if(numeracja == typWagonu.BEZNUMERACJI)
            return (int)(miejsca.size()*maksymalna_pojemnosc);
        throw new Exception("nie mozna pobrac pojemnosci - niesprecyzowany typ wagonu");
    }

    public void changeTypWagonu(typWagonu newTyp){
        if (this.pociag != null) {
            throw new IllegalStateException("Nie można zmienić typu wagonu, ponieważ jest on powiązany z pociągiem.");
        }
        if(newTyp!=numeracja){
            numeracja=newTyp;
        }
    }

    public void setNumeracja(typWagonu newTyp) {
        if (this.pociag != null) {
            throw new IllegalStateException("Nie można zmienić typu wagonu, ponieważ jest on powiązany z pociągiem.");
        }
        this.numeracja = newTyp;
    }

    public double getMaksymalna_pojemnosc() throws Exception {
        if(numeracja == typWagonu.BEZNUMERACJI)
            return maksymalna_pojemnosc;
        throw new Exception("nie mozna pobrac maksymalnej pojemnosci - zly typ wagonu");
    }

    public void setPociag(Pociag pociag) {
        this.pociag = pociag;
        if (pociag != null) {
            this.numeracja = pociag.isObowiazekRezerwacjiMiejsc() ? typWagonu.ZNUMERACJA : typWagonu.BEZNUMERACJI;
        }
    }
    public static class WagonBuilder {
        public Wagon build() {
            Wagon wagon = new Wagon(wagonId, nrWagonu, miejsca, numeracja, pociag);
            if (wagon.pociag != null) {
                wagon.numeracja = wagon.pociag.isObowiazekRezerwacjiMiejsc() ? typWagonu.ZNUMERACJA : typWagonu.BEZNUMERACJI;
            }
            return wagon;
        }
    }
}
