package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.TypMiejsca;
import corp.bs.mm.masmp5.enums.TypOsoby;
import corp.bs.mm.masmp5.enums.TypUlgi;
import corp.bs.mm.masmp5.enums.TypWagonu;
import corp.bs.mm.masmp5.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

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
    private final TypMiejscaEntityRepository typMiejscaEntityRepository;

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

        Random rand = new Random();

        //generowanie osob
        ArrayList<Osoba> osoby = new ArrayList<>();
        ArrayList<Osoba> pasazerowie = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Osoba osoba = generateOsoba();
            osobaRepository.save(osoba);
            osoby.add(osoba);
            if(osoba.getRole().contains(TypOsoby.PASAZER))
                pasazerowie.add(osoba);
        }

        //generowanie pociagow
        ArrayList<String> nazwyPociagow = new ArrayList<>(Arrays.asList(
                "Pendolino", "Lukowianka", "Chopin",
                "Mickiewicz", "Orzeszkowa", "Szyndzielnia",
                "Słowacki", "Giewont", "Tatry",
                "Kopernik", "Kormoran", "Norwid",
                "Warta", "Bombardier", "Sloneczny",
                "Rysy", "Sobieski", "Żubr",
                "Łokietek", "Moniuszko", "Bieszczady",
                "Mazury", "Reymont", "Kaszub"
        ));
        ArrayList<String> przewoznicy = new ArrayList<>(Arrays.asList(
                "KM","IC","TLK","REGIO"
        ));
        ArrayList<Pociag> pociagi = new ArrayList<>();
        for(String n: nazwyPociagow){
            Pociag poc = Pociag.builder()
                    .przewoznik(przewoznicy.get(rand.nextInt(przewoznicy.size())))
                    .obowiazekRezerwacjiMiejsc(Math.random() > 0.5)
                    .nazwa(n)
                    .build();
            pociagRepository.save(poc);
            pociagi.add(poc);
        }

        //generowanie wagonow i miejsc
        ArrayList<Miejsce> allMiejsca = new ArrayList<>();
        ArrayList<Wagon> allWagon = new ArrayList<>();
        for(Pociag poc: pociagi) {
            int iloscWagonow = (int)(Math.random()*5+5);
            for (int i = 1; i <= iloscWagonow; i++) {
                Wagon w = Wagon.builder()
                        .pociag(poc)
                        .nrWagonu(i)
                        .build();
                w = wagonRepository.save(w);
                allWagon.add(w);

                int iloscMiejsc = (int)(Math.random()*50+50);
                for (int j = 1; j <= iloscMiejsc; j++) {
                    double a = Math.random();
                    double b = Math.random();
                    double c = Math.random();
                    //List<TypMiejsca> typ = new ArrayList<>();
                    //if (a > 0.5) typ.add(TypMiejsca.STOLIK);
                    //if (b > 0.05) typ.add(TypMiejsca.ROWEROWE);
                    //if (c > 0.15) typ.add(TypMiejsca.INWALIDA);

                    Miejsce m = Miejsce.builder()
                            //.typyMiejsca(typ)
                            .nrMiejsca(j)
                            .wagon(w)
                            .build();
                    miejsceRepository.save(m);
                    if (a > 0.5) typMiejscaEntityRepository.save(TypMiejscaEntity.builder().typ(TypMiejsca.STOLIK).miejsce(m).build());
                    if (b > 0.05) typMiejscaEntityRepository.save(TypMiejscaEntity.builder().typ(TypMiejsca.ROWEROWE).miejsce(m).build());
                    if (c > 0.15) typMiejscaEntityRepository.save(TypMiejscaEntity.builder().typ(TypMiejsca.INWALIDA).miejsce(m).build());
                    allMiejsca.add(m);
                }
            }
        }

        //generowanie stacji
        ArrayList<Stacja> stacje = new ArrayList<>();
        ArrayList<String> miasta = new ArrayList<>(Arrays.asList(
                "Warszawa", "Kraków", "Łódź", "Gorzów Wielkopolski",
                "Wrocław", "Poznań", "Gdańsk", "Szczecin",
                "Katowice", "Lublin", "Białystok", "Rzeszów",
                "Opole", "Olsztyn", "Kielce", "Zielona Góra" ,
                "Bydgoszcz", "Toruń"
                ));
        for(String miasto : miasta){
            Stacja st = Stacja.builder()
                    .nazwa(miasto)
                    .tory((int)(Math.random()*8)+6)
                    .build();
            stacje.add(st);
            stacjaRepository.save(st);
        }


        //generowanie polaczen i postojów
        ArrayList<ArrayList<String>> linie = new ArrayList<>();
        linie.add(new ArrayList<>(Arrays.asList("Gdańsk", "Olsztyn", "Warszawa", "Kielce", "Kraków")));
        linie.add(new ArrayList<>(Arrays.asList("Szczecin", "Gorzów Wielkopolski", "Poznań", "Wrocław", "Opole", "Katowice")));
        linie.add(new ArrayList<>(Arrays.asList("Opole", "Katowice", "Kraków", "Rzeszów", "Lublin")));
        linie.add(new ArrayList<>(Arrays.asList("Gdańsk", "Bydgoszcz", "Toruń", "Łódź", "Katowice")));
        linie.add(new ArrayList<>(Arrays.asList("Szczecin", "Gdańsk", "Olsztyn", "Białystok")));
        linie.add(new ArrayList<>(Arrays.asList("Toruń", "Warszawa", "Lublin", "Rzeszów")));
        linie.add(new ArrayList<>(Arrays.asList("Poznań", "Łódź", "Kielce", "Rzeszów")));
        linie.add(new ArrayList<>(Arrays.asList("Opole", "Łódź", "Warszawa", "Białystok")));

        ArrayList<Polaczenie> polaczenia = new ArrayList<>();
        ArrayList<Postoj> postoje = new ArrayList<>();
        for(Pociag p : pociagi){
            int nrLinii=rand.nextInt(linie.size());
            LocalDateTime termin = LocalDateTime.now().minusDays(7).withHour(0).withMinute(0).withNano(0);
            LocalDateTime zakresRozkladu = termin.plusDays(30);

            ArrayList<String> linia = linie.get(nrLinii);

            while(termin.isBefore(zakresRozkladu)) {
                Polaczenie pol = Polaczenie.builder()
                        .oznaczeniePolaczenia(generujOznaczeniePolaczenia())
                        .pociagKursujacy(p)
                        .build();
                polaczenieRepository.save(pol);
                polaczenia.add(pol);

                for (String miasto : linia) {
                    Stacja st = stacje.get(miasta.indexOf(miasto));
                    int czasPostoju = rand.nextInt(14) + 1;
                    LocalDateTime termin1 = termin;
                    LocalDateTime termin2 = termin.plusMinutes(czasPostoju);
                    //czy to stacja poczatkowa
                    if(miasto.compareTo(linia.get(0))==0){
                        termin1=null;
                    }
                    //czy to stacja koncowa
                    if(miasto.compareTo(linia.get(linia.size()-1))==0){
                        termin2=null;
                    }
                    Postoj post = Postoj.builder()
                            .polaczenie(pol)
                            .stacja(st)
                            .nrToru(rand.nextInt(st.getTory()) + 1)
                            .planowanyCzasPrzyjazdu(termin1)
                            .planowanyCzasOdjazdu(termin2)
                            .build();
                    postojRepository.save(post);
                    postoje.add(post);
                    termin = termin.plusMinutes(czasPostoju)
                            .plusMinutes(rand.nextInt(90) + 60);
                }
                Collections.reverse(linia);
            }
        }

        //generowanie biletow
        ArrayList<Bilet> bilety =new ArrayList<>();
        //generowanie biletow bezposrednich
        ArrayList<BiletBezposredni> biletybezposrednie =new ArrayList<>();
        double PRZELICZNIK_BB_GODZINOWY = 10.0;

        for(int i=0;i<1000;i++) {
            Polaczenie pol = polaczenia.get(rand.nextInt(polaczenia.size()));
            List<Postoj> postojeZPolaczenia = postojRepository.findByPolaczenie(pol);
            int st1 = rand.nextInt(postojeZPolaczenia.size()-1);
            int st2 = st1 + 1 + rand.nextInt(postojeZPolaczenia.size() - st1 - 1);
            double cenaBB = ((double) Duration.between(
                    postojeZPolaczenia.get(st1).getPlanowanyCzasOdjazdu(),
                    postojeZPolaczenia.get(st2).getPlanowanyCzasPrzyjazdu()
            ).toMinutes()) / 60 * PRZELICZNIK_BB_GODZINOWY;
            cenaBB = Math.round(cenaBB * 100.0) / 100.0;
            BiletBezposredni bb = BiletBezposredni.builder()
                    .cena(cenaBB)
                    .stacjaOdjazd(postojeZPolaczenia.get(st1).getStacja())
                    .stacjaPrzyjazd(postojeZPolaczenia.get(st2).getStacja())
                    .polaczenie(pol)
                    .kupujacy(pasazerowie.get(rand.nextInt(pasazerowie.size())))
                    .build();
            biletRepository.save(bb);
            bilety.add(bb);
            biletBezposredniRepository.save(bb);
            biletybezposrednie.add(bb);
        }

        //generowanie biletow przesiadkowych
        BiletPrzesiadkowy bp = BiletPrzesiadkowy.builder()
                .cena(14.45)
                .czasOdjazdu(LocalDateTime.of(2025, 5, 21, 14, 30))
                .czasPrzyjazdu(LocalDateTime.of(2025, 5, 21, 15, 40))
                .marginesBledu(60)
                .kupujacy(pasazerowie.get(rand.nextInt(pasazerowie.size())))
                .build();

        PrzesiadkowyPolaczenie pp1 = PrzesiadkowyPolaczenie.builder()
                .biletPrzesiadkowy(bp)
                .polaczenie(polaczenia.get(rand.nextInt(polaczenia.size())))
                .build();
        PrzesiadkowyPolaczenie pp2 = PrzesiadkowyPolaczenie.builder()
                .biletPrzesiadkowy(bp)
                .polaczenie(polaczenia.get(rand.nextInt(polaczenia.size())))
                .build();









        biletPrzesiadkowyRepository.saveAll(Arrays.asList(bp));
        przesiadkowyPolaczenieRepository.saveAll(Arrays.asList(pp1));

        //wagonRepository.delete(allWagon.get(0));


        logger.info("ok");

    }

    private String generujOznaczeniePolaczenia() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append("R");
        sb.append(random.nextInt(10));
        sb.append(random.nextInt(10));
        sb.append("Z");
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
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
        List<TypOsoby> role = new ArrayList<>();
        TypUlgi ulga=null;
        String kod = null;
        if (a < 1.0/2.0)
        {
            role.add(TypOsoby.PASAZER);
        }
        else {
            role.add(TypOsoby.PRACOWNIK);
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // A-Z i 0-9
            StringBuilder result = new StringBuilder(10);
            for (int i = 0; i < 10; i++) {
                int randomIndex = random.nextInt(characters.length());
                result.append(characters.charAt(randomIndex));
            }
            kod = String.valueOf(result);

            if (b < 1.0/2.0) {
                role.add(TypOsoby.PASAZER);
            }
        }

        if(role.contains(TypOsoby.PASAZER)){
            TypUlgi[] values = TypUlgi.values();
            ulga = values[random.nextInt(values.length)];
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
                .kodSluzbowy(kod)
                .ulga(ulga)
                .build();

        return os;
    }
}
