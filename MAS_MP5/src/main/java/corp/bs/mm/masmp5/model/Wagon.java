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
    private typWagonu numeracja;

    public int getPojemnosc() throws Exception {
        if(numeracja == typWagonu.ZNUMERACJA)
            return miejsca.size();
        if(numeracja == typWagonu.BEZNUMERACJI)
            return (int)(miejsca.size()*maksymalna_pojemnosc);
        throw new Exception("nie mozna pobrac pojemnosci - niesprecyzowany typ wagonu");
    }

    public void changeTypWagonu(typWagonu newTyp){
        if(newTyp!=numeracja){
            numeracja=newTyp;
        }
    }

    public double getMaksymalna_pojemnosc() throws Exception {
        if(numeracja == typWagonu.BEZNUMERACJI)
            return maksymalna_pojemnosc;
        throw new Exception("nie mozna pobrac maksymalnej pojemnosci - zly typ wagonu");
    }
}
