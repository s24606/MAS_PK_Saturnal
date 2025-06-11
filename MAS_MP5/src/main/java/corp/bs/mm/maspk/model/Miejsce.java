package corp.bs.mm.maspk.model;

import corp.bs.mm.maspk.enums.TypMiejsca;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nr_miejsca", "wagon_id"})
        }
)
public class Miejsce {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long miejsceId;

    @NotNull
    @Min(1)
    private int nrMiejsca;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "miejsce", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<TypMiejscaEntity> typMiejsca = new HashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "wagon_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Wagon wagon;

    public ArrayList<TypMiejsca> getTypy(){
        ArrayList<TypMiejsca> typy= new ArrayList<>();
        for(TypMiejscaEntity tme:typMiejsca)
            typy.add(tme.getTyp());
        return typy;
    }
}
