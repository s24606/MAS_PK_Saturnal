package corp.bs.mm.masmp5.model;

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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long wagonId;

    @NotNull
    private int nrWagonu;

    @OneToMany(mappedBy = "wagon", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Miejsce> miejsca = new HashSet<>();
}
