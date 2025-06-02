package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.model.constraints.CzasPostojuValidation;
import corp.bs.mm.masmp5.model.constraints.NrToruValidation;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@NrToruValidation
@CzasPostojuValidation
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Stacja stacja;

    @ManyToOne
    @JoinColumn(name = "polaczenie_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Polaczenie polaczenie;

    @Nullable
    private LocalDateTime planowanyCzasPrzyjazdu;
    @Nullable
    private LocalDateTime planowanyCzasOdjazdu;
    @Nullable
    private LocalDateTime FaktycznyCzasPrzyjazdu;
    @Nullable
    private LocalDateTime FaktycznyCzasOdjazdu;

    @NotNull
    @Min(1)
    private Integer nrToru;

    public int getNrPeronu(){
        return (nrToru+1)/2;
    }

}
