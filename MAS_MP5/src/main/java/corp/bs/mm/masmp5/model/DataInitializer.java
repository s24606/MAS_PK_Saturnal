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
import java.time.temporal.ChronoUnit;
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
        for (int i = 0; i < 100; i++) {
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
            //czy dany pociag ma udogodnienie
            double a = Math.random();
            double b = Math.random();
            double c = Math.random();
            int iloscWagonow = (int)(Math.random()*3+2);
            for (int i = 1; i <= iloscWagonow; i++) {
                Wagon w = Wagon.builder()
                        .pociag(poc)
                        .nrWagonu(i)
                        .build();
                w = wagonRepository.save(w);
                allWagon.add(w);

                int iloscMiejsc = (int)(Math.random()*20+10);

                for (int j = 1; j <= iloscMiejsc; j++) {
                    //List<TypMiejsca> typ = new ArrayList<>();
                    //if (a > 0.5) typ.add(TypMiejsca.STOLIK);
                    //if (b > 0.05) typ.add(TypMiejsca.ROWEROWE);
                    //if (c > 0.15) typ.add(TypMiejsca.INWALIDA);

                    //czy miejsce ma udogodnienie
                    double a_w = Math.random();
                    double b_w = Math.random();
                    double c_w = Math.random();
                    Miejsce m = Miejsce.builder()
                            //.typyMiejsca(typ)
                            .nrMiejsca(j)
                            .wagon(w)
                            .build();
                    miejsceRepository.save(m);
                    if (a > 0.5 && a_w > 0.5) typMiejscaEntityRepository.save(TypMiejscaEntity.builder().typ(TypMiejsca.STOLIK).miejsce(m).build());
                    if (b > 0.05 && b_w > 0.5) typMiejscaEntityRepository.save(TypMiejscaEntity.builder().typ(TypMiejsca.ROWEROWE).miejsce(m).build());
                    if (c > 0.15 && c_w > 0.5) typMiejscaEntityRepository.save(TypMiejscaEntity.builder().typ(TypMiejsca.INWALIDA).miejsce(m).build());
                    allMiejsca.add(m);
                }
            }
        }

        //generowanie stacji
        ArrayList<Stacja> stacje = new ArrayList<>();
        ArrayList<String> miasta = new ArrayList<>(Arrays.asList(
                "Białystok", "Bydgoszcz", "Gdańsk", "Gorzów Wielkopolski",
                "Katowice", "Kielce", "Kraków", "Łódź", "Lublin",
                "Olsztyn", "Opole", "Poznań", "Rzeszów", "Szczecin",
                "Toruń", "Warszawa", "Wrocław", "Zielona Góra"
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
            LocalDateTime termin = LocalDateTime.now().minusDays(2).withHour(0).withMinute(0).withNano(0);
            LocalDateTime zakresRozkladu = termin.plusDays(5);

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
                    LocalDateTime faktycznyPrzyjazd = null;
                    if(termin1!=null)
                        if(termin1.isBefore(LocalDateTime.now()))
                            faktycznyPrzyjazd=termin1;
                    LocalDateTime faktycznyOdjazd = null;
                    if(termin2!=null)
                        if(termin2.isBefore(LocalDateTime.now()))
                            faktycznyOdjazd=termin2;

                    Postoj post = Postoj.builder()
                            .polaczenie(pol)
                            .stacja(st)
                            .nrToru(rand.nextInt(st.getTory()) + 1)
                            .planowanyCzasPrzyjazdu(termin1)
                            .planowanyCzasOdjazdu(termin2)
                            .faktycznyCzasPrzyjazdu(faktycznyPrzyjazd)
                            .faktycznyCzasOdjazdu(faktycznyOdjazd)
                            .build();
                    postojRepository.save(post);
                    postoje.add(post);
                    termin = termin.plusMinutes(czasPostoju)
                            .plusMinutes(rand.nextInt(600) + 60);
                }
                Collections.reverse(linia);
            }
        }

        //generowanie biletow
        ArrayList<Bilet> bilety =new ArrayList<>();
        //generowanie biletow bezposrednich
        ArrayList<BiletBezposredni> biletybezposrednie =new ArrayList<>();


        //generuje po jednym bilecie na kazdy wagon kazdego połaczenia, ale nie wszystkie mają przypisane nrMiejsc
        polaczenia = (ArrayList<Polaczenie>) polaczenieRepository.findAll();
        for(Polaczenie pol: polaczenia) {
            Pociag kursujacy = pol.getPociagKursujacy();
            for (int i=1;i<=kursujacy.getWagony().size();i++) {
                Wagon wagon = new ArrayList<>(kursujacy.getWagony()).get(i-1);
                List<Postoj> postojeZPolaczenia = postojRepository.findByPolaczenie(pol);
                int st1 = rand.nextInt(postojeZPolaczenia.size() - 1);
                int st2 = st1 + 1 + rand.nextInt(postojeZPolaczenia.size() - st1 - 1);
                Osoba pasazer = pasazerowie.get(rand.nextInt(pasazerowie.size()));
                Integer nrWagonu;
                Integer nrMiejsca;
                if (kursujacy.isObowiazekRezerwacjiMiejsc() || Math.random() > 0.1) {
                    nrWagonu = i;
                    nrMiejsca = (int) (Math.random() * wagon.getMiejsca().size()) + 1;
                } else {
                    nrWagonu = null;
                    nrMiejsca = null;
                }

                BiletBezposredni bb = BiletBezposredni.builder()
                        .cena(Bilet.obliczCeneBiletu(pasazer, postojeZPolaczenia.get(st1).getStacja(), postojeZPolaczenia.get(st2).getStacja(), postojeZPolaczenia))
                        .stacjaOdjazd(postojeZPolaczenia.get(st1).getStacja())
                        .stacjaPrzyjazd(postojeZPolaczenia.get(st2).getStacja())
                        .polaczenie(pol)
                        .kupujacy(pasazer)
                        .nrWagonu(nrWagonu)
                        .nrMiejsca(nrMiejsca)
                        .build();
                biletRepository.save(bb);
                bilety.add(bb);
                biletBezposredniRepository.save(bb);
                biletybezposrednie.add(bb);

            }
        }


        //test metody Osoba.przejrzyjBilety() - wymaga FetchType.EAGER w Osoba.bilety
        /*pasazerowie=new ArrayList<>();
        for(Osoba o : osobaRepository.findAll()){
            if(o.getRole().contains(TypOsoby.PASAZER))
                pasazerowie.add(o);
        }
        for(Osoba pasazer: pasazerowie) {
            String tresc = pasazer.przejrzyjBilety();
            if(tresc.contains("\n"))
                logger.info(pasazer.getImie() + ": " + tresc);
        }*/

        //generowanie biletow przesiadkowych
        for (int i = 0; i < 50; i++) {
            Osoba pasazer = pasazerowie.get(rand.nextInt(pasazerowie.size()));

            LocalDateTime teraz = LocalDateTime.now().withSecond(0).withNano(0);
            LocalDateTime odjazd = teraz.plusDays(rand.nextInt(14) - 7).plusHours(rand.nextInt(24) - 12).plusMinutes(rand.nextInt(60) - 30);
            LocalDateTime przyjazd = odjazd.plusHours(rand.nextInt(12) + 1).plusMinutes(rand.nextInt(59));
            long roznicaGodzin = ChronoUnit.HOURS.between(odjazd, przyjazd);
            int marginesBledu = 0;
            if (roznicaGodzin < 1) {
                marginesBledu =  30;
            } else if (roznicaGodzin < 4) {
                marginesBledu =  60;
            } else {
                marginesBledu =  120;
            }
            int index= rand.nextInt(stacje.size()-1);
            Stacja stacjaS = stacje.get(index);
            Stacja stacjaE = stacje.get(index+1);
            BiletPrzesiadkowy bp = BiletPrzesiadkowy.builder()
                    .cena(Bilet.obliczCeneBiletu(pasazer, odjazd, przyjazd))
                    .czasOdjazdu(odjazd)
                    .czasPrzyjazdu(przyjazd)
                    .marginesBledu(marginesBledu)
                    .stacjaOdjazd(stacjaS)
                    .stacjaPrzyjazd(stacjaE)
                    .kupujacy(pasazer)
                    .build();
            biletPrzesiadkowyRepository.save(bp);
            biletRepository.save(bp);
            bilety.add(bp);
        }

        for(int i=80;i>0;i--){
            Bilet doAnulowania = bilety.get(rand.nextInt(bilety.size()));
            doAnulowania.anuluj();
            biletRepository.save(doAnulowania);
        }

        for(Postoj postoj: postojRepository.findAll()) {
            postoj.updateRelatedBiletStatus(biletBezposredniRepository);
            postojRepository.save(postoj);
        }



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
