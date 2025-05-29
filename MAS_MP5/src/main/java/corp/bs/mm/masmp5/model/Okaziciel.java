package corp.bs.mm.masmp5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"osobaId", "biletId"})
        }
)
public class Okaziciel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long okazicielId;

    @NotBlank
    private String imie;

    @NotBlank
    private String nazwisko;

    @ManyToOne
    @JoinColumn(name = "osobaId", nullable = false)
    @NotNull
    private Osoba kupujacy;

    @ManyToOne
    @JoinColumn(name = "biletId", nullable = false)
    @NotNull
    private Bilet bilet;

    public void uzupelnijNaKupujacego(){
        imie = kupujacy.getImie();
        nazwisko = kupujacy.getNazwisko();
    }
}
