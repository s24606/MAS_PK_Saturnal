package corp.bs.mm.masmp5.model;

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
public class BiletPrzesiadkowy extends Bilet{

    @NotNull
    private LocalDateTime czas_odjazdu;

    @NotNull
    private LocalDateTime czas_przyjazdu;

    @NotNull
    @Min(0)
    private int margines_bledu;
}
