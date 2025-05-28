package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.typMiejsca;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Miejsce {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long miejsceId;

    @NotNull
    private int nrMiejsca;

    @ElementCollection(targetClass = typMiejsca.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "miejsce_typy", joinColumns = @JoinColumn(name = "miejsce_id"))
    @Column(name = "typ")
    private List<typMiejsca> typ = new ArrayList<>();
}
