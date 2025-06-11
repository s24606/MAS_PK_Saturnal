package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.StatusBiletu;
import corp.bs.mm.masmp5.model.constraints.StacjeOfPolaczenieValidation;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@StacjeOfPolaczenieValidation
// Pilnuje, żeby wprzypisanych stacjach były postoje w ramach powiązanego połaczenia
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
    @ManyToOne(optional=false)
    @JoinColumn(name = "polaczenie_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Polaczenie polaczenie;

    public StatusBiletu waliduj(Pociag pociag){
        //jesli zwrocony status jest jakkolwiek inny od WAZNY - wymaga blizszej inspekcji od biletera
        if(polaczenie.getPociagKursujacy().equals(pociag) && super.getStatus()==StatusBiletu.WAZNY){
            super.setStatus(StatusBiletu.SKASOWANY);
            return StatusBiletu.WAZNY;
        }else{
            return super.getStatus();
        }
    }
}
