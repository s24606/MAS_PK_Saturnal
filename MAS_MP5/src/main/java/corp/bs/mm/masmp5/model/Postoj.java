package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.StatusBiletu;
import corp.bs.mm.masmp5.model.constraints.CzasPostojuValidation;
import corp.bs.mm.masmp5.model.constraints.NrToruValidation;
import corp.bs.mm.masmp5.repository.BiletBezposredniRepository;
import corp.bs.mm.masmp5.repository.BiletRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.StatelessSession;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

//plinuje czy przypisany nrToru jest zgodny z ilością torów na przypisanej stacji
@NrToruValidation
//pilnuje czy czas odjazdu jest po czasie przyjazdu (zarowno planowany jak i faktyczny)
@CzasPostojuValidation
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"stacja_id","polaczenie_id"})
})
public class Postoj {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long postojId;

    @ManyToOne
    @JoinColumn(name = "stacja_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Stacja stacja;

    @ManyToOne
    @JoinColumn(name = "polaczenie_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Polaczenie polaczenie;

    @Nullable
    private LocalDateTime planowanyCzasPrzyjazdu;
    @Nullable
    private LocalDateTime planowanyCzasOdjazdu;
    @Nullable
    private LocalDateTime faktycznyCzasPrzyjazdu;
    @Nullable
    private LocalDateTime faktycznyCzasOdjazdu;

    @NotNull
    @Min(1)
    private Integer nrToru;

    public int getNrPeronu(){
        return (nrToru+1)/2;
    }

    public void updateRelatedBiletStatus(BiletBezposredniRepository biletBezposredniRepository){
        ArrayList<BiletBezposredni> bilety = new ArrayList<>(polaczenie.getBiletBezposrednie());
        if(faktycznyCzasOdjazdu!=null){
            for(BiletBezposredni bb: bilety){
                if(bb.getStacjaOdjazd().equals(stacja) && bb.getStatus().equals(StatusBiletu.ZAREZERWOWANY))
                    bb.setStatus(StatusBiletu.WAZNY);
            }
        }
        if(faktycznyCzasPrzyjazdu!=null){
            for(BiletBezposredni bb: bilety){
                if(bb.getStacjaOdjazd().equals(stacja) && !bb.getStatus().equals(StatusBiletu.ANULOWANY))
                    bb.setStatus(StatusBiletu.ZARCHIWIZOWANY);
            }
        }
        biletBezposredniRepository.saveAll(bilety);
    }

}
