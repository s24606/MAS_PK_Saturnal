package corp.bs.mm.masmp5.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"stacja_id","polaczenie_id"})
})
public class Postoj {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long postojId;

    @ManyToOne
    @JoinColumn(name = "stacja_id", nullable = false)
    @NotNull
    private Stacja stacja;

    @ManyToOne
    @JoinColumn(name = "polaczenie_id", nullable = false)
    @NotNull
    private Polaczenie polaczenie;

    @NotNull
    private LocalDateTime planowanyCzasPrzyjazdu;
    @NotNull
    private LocalDateTime planowanyCzasOdjazdu;
    @Nullable
    private LocalDateTime FaktycznyCzasPrzyjazdu;
    @Nullable
    private LocalDateTime FaktycznyCzasOdjazdu;

    @NotNull
    private Integer nrToru;

}
