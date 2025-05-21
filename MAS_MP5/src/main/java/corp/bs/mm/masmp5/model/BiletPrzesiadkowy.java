package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.model.constraints.CzasPrzyjazduPoOdjezdzie;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@CzasPrzyjazduPoOdjezdzie
public class BiletPrzesiadkowy extends Bilet{

    @NotNull
    private LocalDateTime czas_odjazdu;

    @NotNull
    private LocalDateTime czas_przyjazdu;

    @NotNull
    @Min(0)
    private Integer margines_bledu; //integer po to zeby NotNull wylapal, zamiast wstawic 0
}
