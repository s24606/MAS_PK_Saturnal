package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.StatusBiletu;
import corp.bs.mm.masmp5.enums.TypUlgi;
import corp.bs.mm.masmp5.constraints.KupujacyPasazerValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Bilet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long biletId;

    @NotNull(message = "Price is mandatory")
    @Min(0)
    private double cena;

    @NotNull
    @Builder.Default
    private StatusBiletu status = StatusBiletu.ZAREZERWOWANY;;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "stacjaOdjazd_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stacja stacjaOdjazd;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "stacjaPrzyjazd_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stacja stacjaPrzyjazd;

    @AssertTrue(message = "Stacja odjazdu i przyjazdu muszą być różne")
    private boolean isStacjeRozne() {
        if (stacjaOdjazd == null || stacjaPrzyjazd == null) {
            return true;
        }
        return !stacjaOdjazd.equals(stacjaPrzyjazd);
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "kupujacy_id", nullable = false, updatable = false)
    @KupujacyPasazerValidation
    //pilnuje zeby z biletem powiązane były tylko osoby z rolą Pasażer
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Osoba kupujacy;

    public static double obliczCeneBiletu(Osoba pasazer, Stacja stacjaS, Stacja stacjaE, List<Postoj> postoje) throws Exception {
        double znizka = getZnizka(pasazer.getUlga());
        if (postoje == null) {
            throw new Exception("Nie mozna obliczyc ceny biletu - podane polaczenie nie ma przypisanych postojow");
        }
        Postoj postojS=null, postojE=null;
        for(Postoj p :postoje){
            if(p.getStacja().equals(stacjaS))
                postojS=p;
            if(p.getStacja().equals(stacjaE))
                postojE=p;
        }
        if(postojS==null||postojE==null)
            throw new Exception("Nie mozna obliczyc ceny biletu - podano stacje spoza polaczenia");
        else
            return Math.round((1-znizka)*10/60*Duration.between(postojS.getPlanowanyCzasOdjazdu(),postojE.getPlanowanyCzasPrzyjazdu()).toMinutes()*100.0)/100.0;
        //stawka - dycha za godzine jazdy
    }

    public void anuluj(){
        setStatus(StatusBiletu.ANULOWANY);
    }

    public abstract StatusBiletu waliduj(Pociag pociag);

    public static double obliczCeneBiletu(Osoba pasazer,LocalDateTime czasOdjazdu, LocalDateTime czasPrzyjazdu){
       return Math.round((1-getZnizka(pasazer.getUlga()))*10/60*Duration.between(czasOdjazdu,czasPrzyjazdu).toMinutes()*100.0)/100.0;
    }

    public static double getZnizka(TypUlgi ulga){
        double znizka = 0.0;
        if(ulga == TypUlgi.DZIECKO)
            znizka = 0.9;
        if(ulga == TypUlgi.STUDENT)
            znizka = 0.49;
        if(ulga == TypUlgi.EMERYT)
            znizka = 0.37;
        return znizka;
    }

}


