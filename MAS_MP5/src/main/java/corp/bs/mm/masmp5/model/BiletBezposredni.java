package corp.bs.mm.masmp5.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BiletBezposredni extends Bilet{

    @Nullable
    private Integer nrMiejsca;

    @ManyToOne
    @JoinColumn(name = "polaczenie_id")//zeby zrobic z tego kompozycje: , nullable = false, updatable = false
    // oraz dodac w polaczeniu: , cascade = CascadeType.REMOVE
    private Polaczenie polaczenie;
}
