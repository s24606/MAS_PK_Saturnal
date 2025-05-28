package corp.bs.mm.masmp5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Pociag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pociagId;

    @Column(unique = true)
    @NotBlank
    private String nazwa;

    @NotBlank
    private String przewoznik;

    @NotNull
    private boolean obowiazekRezerwacjiMiejsc;

    @OneToMany(mappedBy = "pociagKursujacy", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Polaczenie> polaczenia = new HashSet<>();
}
