package corp.bs.mm.masmp5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Polaczenie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long polaczenieId;

    @NotBlank
    private String oznaczeniePolaczenia;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kursujacy_id", nullable = false, updatable = false)
    private Pociag pociagKursujacy;

    @OneToMany(mappedBy = "polaczenie", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Postoj> postoje = new HashSet<>();
}
