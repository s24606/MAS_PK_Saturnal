package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.model.constraints.CzasBiletValidation;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
}
