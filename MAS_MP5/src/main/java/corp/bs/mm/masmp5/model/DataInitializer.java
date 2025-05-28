package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.typMiejsca;
import corp.bs.mm.masmp5.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final BiletRepository biletRepository;
    private final BiletPrzesiadkowyRepository biletPrzesiadkowyRepository;
    private final BiletBezposredniRepository biletBezposredniRepository;

    private final StacjaRepository stacjaRepository;
    private final PostojRepository postojRepository;
    private final PolaczenieRepository polaczenieRepository;

    private final PociagRepository pociagRepository;
    private final WagonRepository wagonRepository;
    private final MiejsceRepository miejsceRepository;

    private final PrzesiadkowyPolaczenieRepository przesiadkowyPolaczenieRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(biletRepository.count()==0&&stacjaRepository.count()==0&&pociagRepository.count()==0)
            initialData();
    }

    private void initialData(){

        BiletPrzesiadkowy bp = BiletPrzesiadkowy.builder()
                .cena(14.45)
                .czasOdjazdu(LocalDateTime.of(2025, 5, 21, 14, 30))
                .czasPrzyjazdu(LocalDateTime.of(2025, 5, 21, 15, 40))
                .marginesBledu(60)
                .build();

        Pociag poc = Pociag.builder()
                .przewoznik("Koleje Mazowieckie")
                .obowiazekRezerwacjiMiejsc(false)
                .nazwa("Torpeda")
                .build();

        Stacja st1 = Stacja.builder()
                .nazwa("Warszawa Centralna")
                .tory(8)
                .build();
        Stacja st2 = Stacja.builder()
                .nazwa("Warszawa Wschodnia")
                .tory(12)
                .build();
        Polaczenie pol = Polaczenie.builder()
                .oznaczeniePolaczenia("R2345")
                .pociagKursujacy(poc)
                .build();
        Postoj pos1 = Postoj.builder()
                .planowanyCzasPrzyjazdu(LocalDateTime.of(2025, 5, 21, 14, 30))
                .planowanyCzasOdjazdu(LocalDateTime.of(2025, 5, 21, 14, 35))
                .stacja(st1)
                .nrToru(2)
                .polaczenie(pol)
                .build();
        Postoj pos2 = Postoj.builder()
                .planowanyCzasPrzyjazdu(LocalDateTime.of(2025, 5, 21, 14, 40))
                .planowanyCzasOdjazdu(LocalDateTime.of(2025, 5, 21, 14, 55))
                .stacja(st2)
                .nrToru(2)
                .polaczenie(pol)
                .build();
        PrzesiadkowyPolaczenie pp1 = PrzesiadkowyPolaczenie.builder()
                .biletPrzesiadkowy(bp)
                .polaczenie(pol)
                .build();
        //PrzesiadkowyPolaczenie pp2 = PrzesiadkowyPolaczenie.builder()
        //        .biletPrzesiadkowy(bp)
        //        .polaczenie(pol)
        //        .build();


        BiletBezposredni bb1 = BiletBezposredni.builder()
                .cena(12.55)
                .stacjaOdjazd(st1)
                .stacjaPrzyjazd(st2)
                .polaczenie(pol)
                .build();
        BiletBezposredni bb2 = BiletBezposredni.builder()
                .cena(7.34)
                .stacjaOdjazd(st1)
                .stacjaPrzyjazd(st2)
                .polaczenie(pol)
                .nrMiejsca(177)
                .build();

        ArrayList<Wagon> wagony = new ArrayList<>();
        ArrayList<Miejsce> allMiejsca = new ArrayList<>();

        for(int i=1; i<=6; i++) {
            Wagon w = Wagon.builder()
                    .nrWagonu(i)
                    .build();

            for (int j = 1; j <= 10; j++) {
                double a = Math.random();
                double b = Math.random();
                double c = Math.random();
                List<typMiejsca> typ = new ArrayList<>();
                if (a > 0.5) typ.add(typMiejsca.STOLIK);
                if (b > 0.6) typ.add(typMiejsca.ROWEROWE);
                if (c > 0.4) typ.add(typMiejsca.INWALIDA);
                Miejsce m = Miejsce.builder()
                        .typ(typ)
                        .nrMiejsca(j)
                        .wagon(w)
                        .build();
                allMiejsca.add(m);
            }
            wagony.add(w);
        }


        stacjaRepository.saveAll(Arrays.asList(st1, st2));
        pociagRepository.saveAll(Arrays.asList(poc));
        wagonRepository.saveAll(wagony);
        miejsceRepository.saveAll(allMiejsca);
        polaczenieRepository.saveAll(Arrays.asList(pol));
        postojRepository.saveAll(Arrays.asList(pos1, pos2));
        biletRepository.saveAll(Arrays.asList(bp,bb1,bb2));
        biletBezposredniRepository.saveAll(Arrays.asList(bb1,bb2));
        biletPrzesiadkowyRepository.saveAll(Arrays.asList(bp));
        przesiadkowyPolaczenieRepository.saveAll(Arrays.asList(pp1));

        logger.info("ok");

    }
}
