package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.TypMiejsca;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nr_miejsca", "wagon_id"})
        }
)
public class Miejsce {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long miejsceId;

    @NotNull
    private int nrMiejsca;

    @ElementCollection(targetClass = TypMiejsca.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "miejsce_typy", joinColumns = @JoinColumn(name = "miejsce_id"))
    @Column(name = "typ")
    @UniqueElements(message = "Typy miejsca nie mogą się powtarzać")

    private List<TypMiejsca> typ = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "wagon_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Wagon wagon;
}
