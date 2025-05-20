package corp.bs.mm.mas_mp5;

import corp.bs.mm.mas_mp5.model.Bilet;
import corp.bs.mm.mas_mp5.repository.BiletRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(Data.class);

    private final BiletRepository biletRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initialData();
    }

    private void initialData(){

        Bilet bilet = Bilet.builder().cena(14.44).build();


        biletRepository.saveAll(Arrays.asList(bilet));
        logger.info("ok");

    }
}
