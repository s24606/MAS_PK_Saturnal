package corp.bs.mm.masmp5.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nrWagonu", "nrMiejsca", "polaczenie_id"})
        }
)
public class BiletBezposredni extends Bilet{

    @Nullable
    private Integer nrMiejsca;

    @Nullable
    private Integer nrWagonu;

    @Setter(AccessLevel.NONE)
    @ManyToOne
    @JoinColumn(name = "polaczenie_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Polaczenie polaczenie;
}
