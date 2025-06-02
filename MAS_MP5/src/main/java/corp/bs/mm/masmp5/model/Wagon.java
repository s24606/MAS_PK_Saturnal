package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.TypWagonu;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Wagon {
    @Getter(AccessLevel.NONE)
    private final double maksymalna_pojemnosc=1.3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long wagonId;

    @NotNull
    private int nrWagonu;

    @OneToMany(mappedBy = "wagon", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Miejsce> miejsca = new HashSet<>();

    @NotNull
    @Setter(AccessLevel.NONE)
    private TypWagonu numeracja;

    @ManyToOne
    @JoinColumn(name = "pociag_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Pociag pociag;

    public int getPojemnosc() throws Exception {
        if(numeracja == TypWagonu.ZNUMERACJA)
            return miejsca.size();
        if(numeracja == TypWagonu.BEZNUMERACJI)
            return (int)(miejsca.size()*maksymalna_pojemnosc);
        throw new Exception("nie mozna pobrac pojemnosci - niesprecyzowany typ wagonu");
    }

    public void changeTypWagonu(TypWagonu newTyp){
        if (this.pociag != null) {
            throw new IllegalStateException("Nie można zmienić typu wagonu, ponieważ jest on powiązany z pociągiem.");
        }
        if(newTyp!=numeracja){
            numeracja=newTyp;
        }
    }

    public void setNumeracja(TypWagonu newTyp) {
        if (this.pociag != null) {
            throw new IllegalStateException("Nie można zmienić typu wagonu, ponieważ jest on powiązany z pociągiem.");
        }
        this.numeracja = newTyp;
    }

    public double getMaksymalna_pojemnosc() throws Exception {
        if(numeracja == TypWagonu.BEZNUMERACJI)
            return maksymalna_pojemnosc;
        throw new Exception("nie mozna pobrac maksymalnej pojemnosci - zly typ wagonu");
    }

    public void setPociag(Pociag pociag) {
        this.pociag = pociag;
        if (pociag != null) {
            this.numeracja = pociag.isObowiazekRezerwacjiMiejsc() ? TypWagonu.ZNUMERACJA : TypWagonu.BEZNUMERACJI;
        }
    }
    public static class WagonBuilder {
        public Wagon build() {
            Wagon wagon = new Wagon(wagonId, nrWagonu, miejsca, numeracja, pociag);
            if (wagon.pociag != null) {
                wagon.numeracja = wagon.pociag.isObowiazekRezerwacjiMiejsc() ? TypWagonu.ZNUMERACJA : TypWagonu.BEZNUMERACJI;
            }
            return wagon;
        }
    }
}
