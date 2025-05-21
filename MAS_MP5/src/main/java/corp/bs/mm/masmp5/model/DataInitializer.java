package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.repository.BiletBezposredniRepository;
import corp.bs.mm.masmp5.repository.BiletPrzesiadkowyRepository;
import corp.bs.mm.masmp5.repository.BiletRepository;
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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initialData();
    }

    private void initialData(){

        BiletPrzesiadkowy bp = BiletPrzesiadkowy.builder()
                .cena(14.45)
                .czas_odjazdu(LocalDateTime.of(2025, 5, 21, 14, 30))
                .czas_przyjazdu(LocalDateTime.of(2025, 5, 21, 15, 40))
                .margines_bledu(60)
                .build();

        BiletBezposredni bb1 = BiletBezposredni.builder()
                        .cena(12.55).build();
        BiletBezposredni bb2 = BiletBezposredni.builder()
                .cena(7.34).nr_miejsca(177).build();


        biletRepository.saveAll(Arrays.asList(bp,bb1,bb2));
        biletBezposredniRepository.saveAll(Arrays.asList(bb1,bb2));
        biletPrzesiadkowyRepository.saveAll(Arrays.asList(bp));
        logger.info("ok");

    }
}
