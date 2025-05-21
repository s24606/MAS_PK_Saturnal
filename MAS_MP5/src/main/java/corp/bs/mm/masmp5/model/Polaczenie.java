package corp.bs.mm.masmp5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
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
    @OrderBy("planowanyCzasOdjazdu ASC")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Postoj> postoje = new ArrayList<>();

    @OneToMany(mappedBy = "polaczenie", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PrzesiadkowyPolaczenie> przesiadkowyPolaczenia = new HashSet<>();

    @OneToMany(mappedBy = "polaczenie")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<BiletBezposredni> biletBezposrednie = new HashSet<>();
}
