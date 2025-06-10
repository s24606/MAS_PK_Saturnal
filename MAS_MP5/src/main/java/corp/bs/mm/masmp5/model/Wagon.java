package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.TypMiejsca;
import corp.bs.mm.masmp5.enums.TypWagonu;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nrWagonu", "pociag_id"})
        }
)
public class Wagon {
    @Getter(AccessLevel.NONE)
    private final double maksymalnaPojemnosc =1.3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long wagonId;

    @NotNull
    @Min(1)
    private int nrWagonu;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "wagon", cascade = CascadeType.REMOVE, orphanRemoval = true)
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
            return (int)(miejsca.size()* maksymalnaPojemnosc);
        throw new Exception("nie mozna pobrac pojemnosci - niesprecyzowany typ wagonu");
    }

    public void setNumeracja(TypWagonu newTyp) {
        if (this.pociag != null) {
            throw new IllegalStateException("Nie można zmienić typu wagonu, ponieważ jest on powiązany z pociągiem.");
        }
        this.numeracja = newTyp;
    }

    public double getMaksymalnaPojemnosc() throws Exception {
        if(numeracja == TypWagonu.BEZNUMERACJI)
            return maksymalnaPojemnosc;
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

    public ArrayList<TypMiejsca> getDostepneTypyMiejsc(){
        ArrayList<TypMiejsca> typy = new ArrayList<>();
        for(Miejsce m: miejsca)
            for(TypMiejscaEntity typ:m.getTypMiejsca())
                if(!typy.contains(typ.getTyp()))
                    typy.add(typ.getTyp());
        return typy;
    }
}
