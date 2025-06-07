package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.TypMiejsca;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "typ_miejsca",
        uniqueConstraints = @UniqueConstraint(columnNames = {"miejsce_id", "typ"})
)
public class TypMiejscaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TypMiejsca typ;

    @ManyToOne(optional = false)
    @JoinColumn(name = "miejsce_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Miejsce miejsce;
}

