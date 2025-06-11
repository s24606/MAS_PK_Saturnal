package corp.bs.mm.maspk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Setter(AccessLevel.NONE)
    @NotNull
    private boolean obowiazekRezerwacjiMiejsc;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "pociagKursujacy", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Polaczenie> polaczenia = new HashSet<>();

    @Setter(AccessLevel.NONE)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pociag")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Wagon> wagony = new HashSet<>();

    public static class PociagBuilder {
        public Pociag build() {
            Pociag pociag = new Pociag(pociagId, nazwa, przewoznik, obowiazekRezerwacjiMiejsc, polaczenia, wagony);
            if (pociag.wagony != null) {
                for (Wagon wagon : pociag.wagony) {
                    wagon.setPociag(pociag);
                }
            }
            return pociag;
        }
    }

    public void setObowiazekRezerwacjiMiejsc(boolean obowiazekRezerwacjiMiejsc) {
        this.obowiazekRezerwacjiMiejsc = obowiazekRezerwacjiMiejsc;
        if (wagony != null) {
            this.setWagony(wagony);
        }
    }

    public void setWagony(Set<Wagon> wagony) {
        this.wagony = wagony;
        if (wagony != null) {
            for (Wagon wagon : wagony) {
                wagon.setPociag(this);
            }
        }
    }

    public void addWagon(Wagon wagon) {
        wagony.add(wagon);
        wagon.setPociag(this);
    }


}
