package corp.bs.mm.masmp5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"biletPrzesiadkowy_id", "polaczenie_id"})
        }
)
public class PrzesiadkowyPolaczenie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long PrzesiadkowyPolaczenieId;

    @ManyToOne
    @JoinColumn(name = "biletPrzesiadkowy_id", nullable = false)
    @NotNull
    private BiletPrzesiadkowy biletPrzesiadkowy;

    @ManyToOne
    @JoinColumn(name = "polaczenie_id", nullable = false)
    @NotNull
    private Polaczenie polaczenie;
}
