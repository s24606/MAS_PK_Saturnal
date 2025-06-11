package corp.bs.mm.maspk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class Stacja {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long stacjaId;

    @NotBlank
    private String nazwa;

    @NotNull
    @Min(1)
    private int tory;

    @Setter(AccessLevel.NONE)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "stacja", cascade = CascadeType.REMOVE)
    @OrderBy("planowanyCzasOdjazdu ASC")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Postoj> postoje = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "stacja", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Postoj> biletyZ = new HashSet<>();

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "stacja", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Postoj> biletyDo = new HashSet<>();
}
