package corp.bs.mm.masmp5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.swing.*;
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
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Pociag pociagKursujacy;

    @OneToMany(/*fetch = FetchType.EAGER, */mappedBy = "polaczenie", cascade = CascadeType.REMOVE)
    @OrderBy("planowanyCzasOdjazdu ASC")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Postoj> postoje = new ArrayList<>();

    @OneToMany(mappedBy = "polaczenie", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PrzesiadkowyPolaczenie> przesiadkowyPolaczenia = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "polaczenie", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<BiletBezposredni> biletBezposrednie = new HashSet<>();
}
