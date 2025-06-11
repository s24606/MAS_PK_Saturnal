package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.StatusBiletu;
import corp.bs.mm.masmp5.constraints.CzasBiletValidation;
import jakarta.persistence.*;
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
//sprawdza czy czas przyjazdu jest po czasie odjazdu
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
    public StatusBiletu waliduj(Pociag pociag){
        //jesli zwrocony status jest jakkolwiek inny od WAZNY - wymaga blizszej inspekcji od biletera
        if(super.getStatus()==StatusBiletu.WAZNY){
            super.setStatus(StatusBiletu.SKASOWANY);
            return StatusBiletu.WAZNY;
        } else {
            return super.getStatus();
        }
    }
}
