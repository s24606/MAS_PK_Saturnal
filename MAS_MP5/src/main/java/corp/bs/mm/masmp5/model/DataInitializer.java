package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final BiletRepository biletRepository;
    private final BiletPrzesiadkowyRepository biletPrzesiadkowyRepository;
    private final BiletBezposredniRepository biletBezposredniRepository;

    private final StacjaRepository stacjaRepository;

    private final PociagRepository pociagRepository;
    private final PolaczenieRepository polaczenieRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initialData();
    }

    private void initialData(){

        BiletPrzesiadkowy bp = BiletPrzesiadkowy.builder()
                .cena(14.45)
                .czasOdjazdu(LocalDateTime.of(2025, 5, 21, 14, 30))
                .czasPrzyjazdu(LocalDateTime.of(2025, 5, 21, 15, 40))
                .marginesBledu(60)
                .build();
        BiletBezposredni bb1 = BiletBezposredni.builder()
                        .cena(12.55).build();
        BiletBezposredni bb2 = BiletBezposredni.builder()
                .cena(7.34).nrMiejsca(177).build();

        Stacja st = Stacja.builder()
                .nazwa("Warszawa Centralna")
                .tory(8)
                .build();

        Pociag poc = Pociag.builder()
                .przewoznik("Koleje Mazowieckie")
                .obowiazekRezerwacjiMiejsc(false)
                .build();
        Polaczenie pol = Polaczenie.builder()
                .oznaczeniePolaczenia("R2345")
                .pociagKursujacy(poc)
                .build();


        biletRepository.saveAll(Arrays.asList(bp,bb1,bb2));
        biletBezposredniRepository.saveAll(Arrays.asList(bb1,bb2));
        biletPrzesiadkowyRepository.saveAll(Arrays.asList(bp));
        stacjaRepository.saveAll(Arrays.asList(st));
        pociagRepository.saveAll(Arrays.asList(poc));
        polaczenieRepository.saveAll(Arrays.asList(pol));

        logger.info("ok");

    }
}
