package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.model.constraints.CzasBiletValidation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@CzasBiletValidation
public class BiletPrzesiadkowy extends Bilet{

    @NotNull
    private LocalDateTime czasOdjazdu;

    @NotNull
    private LocalDateTime czasPrzyjazdu;

    @NotNull
    @Min(0)
    @Builder.Default
    private int marginesBledu=60;

    @OneToMany(mappedBy = "biletPrzesiadkowy", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PrzesiadkowyPolaczenie> przesiadkowyPolaczenia = new HashSet<>();
}
