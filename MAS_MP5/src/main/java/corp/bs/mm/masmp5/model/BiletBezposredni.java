package corp.bs.mm.masmp5.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BiletBezposredni extends Bilet{

    @Nullable
    private Integer nrMiejsca;

    @Setter(AccessLevel.NONE)
    @ManyToOne
    @JoinColumn(name = "polaczenie_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Polaczenie polaczenie;
}
