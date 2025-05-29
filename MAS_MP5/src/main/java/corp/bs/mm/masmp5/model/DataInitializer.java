package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.typMiejsca;
import corp.bs.mm.masmp5.enums.typOsoby;
import corp.bs.mm.masmp5.enums.typWagonu;
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
import java.util.Random;

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

    private final OsobaRepository osobaRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(biletRepository.count()==0&&stacjaRepository.count()==0&&pociagRepository.count()==0) {
            try {
                initialData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initialData() throws Exception {

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
        pociagRepository.saveAll(Arrays.asList(poc));

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


        ArrayList<Miejsce> allMiejsca = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            double aw = Math.random();
            Wagon w = Wagon.builder()
                    .numeracja(aw < 0.5 ? typWagonu.BEZNUMERACJI : typWagonu.ZNUMERACJA)
                    .pociag(poc)
                    .nrWagonu(i)
                    .build();
            w = wagonRepository.save(w);

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
        }


        for (int i = 0; i < 10; i++) {
            osobaRepository.save(generateOsoba());
        }




        stacjaRepository.saveAll(Arrays.asList(st1, st2));
        miejsceRepository.saveAll(allMiejsca);
        polaczenieRepository.saveAll(Arrays.asList(pol));
        postojRepository.saveAll(Arrays.asList(pos1, pos2));
        biletRepository.saveAll(Arrays.asList(bp,bb1,bb2));
        biletBezposredniRepository.saveAll(Arrays.asList(bb1,bb2));
        biletPrzesiadkowyRepository.saveAll(Arrays.asList(bp));
        przesiadkowyPolaczenieRepository.saveAll(Arrays.asList(pp1));


        logger.info("ok");

    }

    private Osoba generateOsoba(){
        List<String> imiona = Arrays.asList(
                "Jan", "Piotr", "Marek", "Tomasz", "Andrzej",
                "Krzysztof", "Paweł", "Michał", "Marcin", "Adam",
                "Łukasz", "Grzegorz", "Mateusz", "Rafał", "Szymon",
                "Sebastian", "Damian", "Kamil", "Jakub", "Robert",
                "Anna", "Maria", "Katarzyna", "Magdalena", "Agnieszka",
                "Joanna", "Ewa", "Aleksandra", "Natalia", "Zuzanna",
                "Julia", "Wiktoria", "Małgorzata", "Paulina", "Karolina",
                "Martyna", "Emilia", "Patrycja", "Dominika", "Barbara"
        );
        List<String> nazwiska = Arrays.asList(
                "Kowalsk", "Nowakowsk", "Wiśniewsk", "Wójcick", "Kowalewsk",
                "Kamińsk", "Lewandowsk", "Zielińsk", "Szymańsk",
                "Dąbrowsk", "Kozłowsk", "Jankowsk", "Mazursk", "Wojciechowsk",
                "Kwiatkowsk", "Kaczmarsk", "Piotrowsk", "Grabowsk",
                "Zajączkowsk", "Pawłowsk", "Michalsk", "Królikowsk",
                "Jabłońsk", "Wróblewsk", "Nowick", "Olszewsk",
                "Olszańsk", "Jaworsk", "Malinowsk", "Pawłowsk", "Górsk",
                "Witkowsk", "Markowsk", "Urbańsk", "Sikorsk", "Rutkowsk",
                "Michalsk", "Ostrowsk", "Tomaszewsk", "Zalewsk"
        );
        Random random = new Random();
        String imie = imiona.get(random.nextInt(imiona.size()));
        String nazwisko = nazwiska.get(random.nextInt(nazwiska.size()));
        nazwisko+= imie.endsWith("a")?'a':'i';
        List<String> dom1 = Arrays.asList("onet", "gmail", "wp", "yahoo");
        List<String> dom2 = Arrays.asList("pl", "com", "gov", "net", "de");
        double a = Math.random();
        double b = Math.random();
        List<typOsoby> role = new ArrayList<>();
        if (a < 1.0/2.0) role.add(typOsoby.PASAZER);
        else {
            role.add(typOsoby.PRACOWNIK);
            if (b < 1.0/2.0) role.add(typOsoby.PASAZER);
        }
        Osoba os = Osoba.builder()
                .imie(imie)
                .nazwisko(nazwisko)
                .email(imie+"_"+nazwisko+
                        random.nextInt(1000)+"@"+
                        dom1.get(random.nextInt(dom1.size()))+"."+
                        dom2.get(random.nextInt(dom2.size())))
                .telefon(String.valueOf(random.nextInt(500000000)+500000000))
                .role(role)
                .build();

        return os;
    }
}
