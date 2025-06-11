package corp.bs.mm.masmp5.ui;

import corp.bs.mm.masmp5.enums.TypOsoby;
import corp.bs.mm.masmp5.model.*;
import corp.bs.mm.masmp5.repository.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MainFrame extends JFrame {

    private final StacjaRepository stacjaRepo;
    private final OsobaRepository osobaRepo;
    private final PolaczenieRepository polaczenieRepo;
    private final BiletRepository biletRepository;
    private final BiletBezposredniRepository biletBezposredniRepository;

    // Atrybuty do wyszukiwania
    private Stacja stacjaStart;
    private Stacja stacjaEnd;
    @Setter
    private LocalDateTime terminStart;
    @Setter
    private LocalDateTime terminEnd;
    private Polaczenie bestPolaczenie;
    HashMap<Long, Postoj> wyszukanePostojeS = new HashMap<>();
    HashMap<Long, Postoj> wyszukanePostojeE = new HashMap<>();


    private Osoba zalogowanyUser;
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private JPanel navBar;

    public MainFrame(StacjaRepository stacjaRepo,
                     OsobaRepository osobaRepo,
                     PolaczenieRepository polaczenieRepo,
                     BiletRepository biletRepository,
                     BiletBezposredniRepository biletBezposredniRepository) {

        this.stacjaRepo = stacjaRepo;
        this.osobaRepo = osobaRepo;
        this.polaczenieRepo = polaczenieRepo;
        this.biletRepository = biletRepository;
        this.biletBezposredniRepository = biletBezposredniRepository;

        this.zalogowanyUser = null;

        stacjaStart=null;
        stacjaEnd=null;
        terminStart=null;
        terminEnd=null;
        bestPolaczenie=null;

        setTitle("Saturnal");
        setSize(700, 450);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.add(new JPanel(), "KONTO");

        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBackground(new Color(255, 255, 155));

        // Nagłówek
        JLabel headerLabel = new JLabel("Witaj na stronie głównej!");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        headerLabel.setAlignmentX(CENTER_ALIGNMENT);
        homePanel.add(headerLabel);


        cardsPanel.add(homePanel, "HOME");

        // NAVBAR
        navBar = new JPanel(new BorderLayout());
        updateNavBar();

        setLayout(new BorderLayout());
        add(navBar, BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);

        // Przycisk do ekranu wyszukiwania
        JButton btnGoSearch = new JButton("Wyszukiwarka");
        btnGoSearch.setAlignmentX(CENTER_ALIGNMENT);
        btnGoSearch.addActionListener(e -> {
            for (Component c : cardsPanel.getComponents()) {
                if ("SEARCH".equals(c.getName())) {
                    cardsPanel.remove(c);
                    break;
                }
            }
            JPanel searchPanel = new WyszukiwarkaPolaczenPanel(stacjaRepo, this);
            searchPanel.setName("SEARCH");
            cardsPanel.add(searchPanel, "SEARCH");
            cardLayout.show(cardsPanel, "SEARCH");
            cardsPanel.revalidate();
            cardsPanel.repaint();
        });
        homePanel.add(Box.createVerticalStrut(20));
        homePanel.add(btnGoSearch);

        cardLayout.show(cardsPanel, "HOME");

        setVisible(true);
    }


    private void updateNavBar() {
        navBar.removeAll();
        Color bgColor = new Color(224, 224, 224);
        navBar.setBackground(bgColor);
        navBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Przycisk do strony głównej
        JButton btnHome = new JButton("Strona główna");
        btnHome.addActionListener(e -> cardLayout.show(cardsPanel, "HOME"));

        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtons.setBackground(bgColor);

        if (zalogowanyUser != null) {
            // Przycisk KONTO (dla zalogowanych)
            JButton btnKonto = new JButton("Konto");
            btnKonto.addActionListener(e -> {
                if (zalogowanyUser != null) {
                    JPanel kontoPanel = new KontoPanel(zalogowanyUser);
                    Component[] components = cardsPanel.getComponents();
                    for (Component c : components) {
                        if (c.getName() != null && c.getName().equals("KONTO")) {
                            cardsPanel.remove(c);
                            break;
                        }
                    }
                    kontoPanel.setName("KONTO");
                    cardsPanel.add(kontoPanel, "KONTO");
                    cardLayout.show(cardsPanel, "KONTO");
                    cardsPanel.revalidate();
                    cardsPanel.repaint();
                }
            });

            // Przycisk WYLOGUJ (dla zalogowanych)
            JButton btnWyloguj = new JButton("Wyloguj się");

            btnWyloguj.addActionListener(e -> {
                zalogowanyUser = null;
                JOptionPane.showMessageDialog(this, "Wylogowano.");
                cardLayout.show(cardsPanel, "HOME");
                updateNavBar();
            });

            rightButtons.add(btnKonto);
            rightButtons.add(btnWyloguj);
        } else {
            JButton btnZaloguj = new JButton("Zaloguj się");
            JButton btnZarejestruj = new JButton("Zarejestruj się");

            // Przycisk ZALOGUJ (dla niezalogowanych)
            btnZaloguj.addActionListener(e -> {
                List<Osoba> wszyscy = (List<Osoba>) osobaRepo.findAll();

                List<Osoba> pasazerowie = wszyscy.stream()
                        .filter(o -> o.getRole().contains(TypOsoby.PASAZER)
                                && !o.getRole().contains(TypOsoby.PRACOWNIK))
                        .collect(Collectors.toCollection(ArrayList::new));

                List<Osoba> pracownicy = wszyscy.stream()
                        .filter(o -> o.getRole().contains(TypOsoby.PRACOWNIK)
                                && !o.getRole().contains(TypOsoby.PASAZER))
                        .collect(Collectors.toCollection(ArrayList::new));

                List<Osoba> obaTypy = wszyscy.stream()
                        .filter(o -> new HashSet<>(o.getRole()).containsAll(List.of(TypOsoby.PASAZER, TypOsoby.PRACOWNIK)))
                        .collect(Collectors.toCollection(ArrayList::new));

                for (Component c : cardsPanel.getComponents()) {
                    if ("LOGIN".equals(c.getName())) {
                        cardsPanel.remove(c);
                        break;
                    }
                }
                LoginPanel loginPanel = new LoginPanel(pasazerowie, pracownicy, obaTypy, this);
                cardsPanel.add(loginPanel, "LOGIN");
                cardLayout.show(cardsPanel, "LOGIN");
            });

            rightButtons.add(btnZaloguj);
            rightButtons.add(btnZarejestruj);
        }

        navBar.add(btnHome, BorderLayout.WEST);
        navBar.add(rightButtons, BorderLayout.EAST);

        navBar.revalidate();
        navBar.repaint();
    }

    public void setZalogowanyUser(Osoba osoba) {
        this.zalogowanyUser = osoba;
        updateNavBar();
    }

    public void showHome() {
        cardLayout.show(cardsPanel, "HOME");
    }

    public void setAtrybutyWyszukiwania(Stacja stacjaStart, Stacja stacjaEnd,
                         LocalDateTime terminStart,
                         LocalDateTime terminEnd){
        this.stacjaStart=stacjaStart;
        this.stacjaEnd=stacjaEnd;
        this.terminStart=terminStart;
        this.terminEnd=terminEnd;
    }

    public ArrayList<Polaczenie> findPolaczeniaForStacje() {
        //sporzadzenie map postojów poczatkowych po ich nrPolaczen
        ArrayList<Polaczenie> polaczeniaS = new ArrayList<>();
        HashMap<Long, Postoj> mapaAllPostojS = new HashMap<>();
        for(Postoj postoj: stacjaStart.getPostoje()){
            polaczeniaS.add(postoj.getPolaczenie());
            mapaAllPostojS.put(postoj.getPolaczenie().getPolaczenieId(),postoj);
        }

        //sporzadzenie mapy postojów końcowych, ale tylko tych w ramach połaczeń obecnych w postojach początkowych
        ArrayList<Polaczenie> matchingPolaczenia = new ArrayList<>();
        HashMap<Long, Postoj> mapaPostojE = new HashMap<>();
        for(Postoj postojEnd: stacjaEnd.getPostoje()) {
            if (polaczeniaS.contains(postojEnd.getPolaczenie())) {
                Postoj postojStart = mapaAllPostojS.get(postojEnd.getPolaczenie().getPolaczenieId());
                if (postojStart.getPlanowanyCzasOdjazdu() != null && postojEnd.getPlanowanyCzasPrzyjazdu() != null)
                    if (postojStart.getPlanowanyCzasOdjazdu().isBefore(postojEnd.getPlanowanyCzasPrzyjazdu())) {
                        matchingPolaczenia.add(postojEnd.getPolaczenie());
                        mapaPostojE.put(postojEnd.getPolaczenie().getPolaczenieId(), postojEnd);
                    }
            }
        }

        //sporzadzenie mapy postojow początkowych, ale tylko połączeń które przejeżdżają przez obie stacje
        HashMap<Long, Postoj> mapaPostojS = new HashMap<>();
        for(Postoj postojS: stacjaStart.getPostoje())
            if(matchingPolaczenia.contains(postojS.getPolaczenie()))
                mapaPostojS.put(postojS.getPolaczenie().getPolaczenieId(),postojS);


        // określenie najblizszego polaczenie na podstawie najblizszego postoju do wybranego terminu odjazdu/przyjazdu
        HashMap <Long, Postoj> refMap;
        LocalDateTime terminDocelowy;
        if(terminStart!=null) {
            terminDocelowy = terminStart;
            refMap = mapaPostojS;
        }
        else {
            terminDocelowy = terminEnd;
            refMap = mapaPostojE;
        }
        Postoj najblizszyPostoj = znajdzNajblizszyPostoj(refMap, terminDocelowy, this.terminStart!=null);
        Long najbliszePolaczenieId = null;
        for (Map.Entry<Long, Postoj> entry : refMap.entrySet()) {
            if (entry.getValue().equals(najblizszyPostoj)) {
                najbliszePolaczenieId = entry.getKey();
            }
        }

        // przygotowanie posortowanej listy wyszukanych połączeń
        ArrayList<Postoj> sortedPostoje = sortujPostojePoCzasie(mapaPostojS, true);
        refMap = mapaPostojS;
        ArrayList<Polaczenie> sortedPolaczenia = new ArrayList<>();
        for(Postoj pos : sortedPostoje){
            Long polaczenieId = null;
            for (Map.Entry<Long, Postoj> entry : refMap.entrySet()) {
                if (entry.getValue().equals(pos)) {
                    polaczenieId = entry.getKey();
                }
            }
            Polaczenie szukane = null;
            for(Polaczenie pol : matchingPolaczenia) {
                if (Objects.equals(pol.getPolaczenieId(), polaczenieId))
                    szukane = pol;
                //przypisanie najbliszego polaczenie na podstawie wczesniej znalezionego Id
                if (Objects.equals(pol.getPolaczenieId(), najbliszePolaczenieId))
                    this.bestPolaczenie = pol;
            }
            sortedPolaczenia.add(szukane);
        }

        this.wyszukanePostojeS=mapaPostojS;
        this.wyszukanePostojeE=mapaPostojE;

        return sortedPolaczenia;
    }

    public static ArrayList<Postoj> sortujPostojePoCzasie(HashMap<Long, Postoj> mapaPostojow, boolean sortByOdjazdT) {
        ArrayList<Postoj> sortedPostoje = new ArrayList<>(mapaPostojow.values());
        sortedPostoje.sort((postoj1, postoj2) -> {
            LocalDateTime czasOdjazdu1;
            LocalDateTime czasOdjazdu2;
            if(sortByOdjazdT) {
                czasOdjazdu1 = (postoj1.getPlanowanyCzasOdjazdu() != null) ? postoj1.getPlanowanyCzasOdjazdu() : LocalDateTime.MAX;
                czasOdjazdu2 = (postoj2.getPlanowanyCzasOdjazdu() != null) ? postoj2.getPlanowanyCzasOdjazdu() : LocalDateTime.MAX;
            } else {
                czasOdjazdu1 = (postoj1.getPlanowanyCzasPrzyjazdu() != null) ? postoj1.getPlanowanyCzasPrzyjazdu() : LocalDateTime.MAX;
                czasOdjazdu2 = (postoj2.getPlanowanyCzasPrzyjazdu() != null) ? postoj2.getPlanowanyCzasPrzyjazdu() : LocalDateTime.MAX;
            }
            long roznica1 = Math.abs(Duration.between(LocalDateTime.MIN, czasOdjazdu1).toMinutes());
            long roznica2 = Math.abs(Duration.between(LocalDateTime.MIN, czasOdjazdu2).toMinutes());
            return Long.compare(roznica1, roznica2);
        });

        return sortedPostoje;
    }

    public static Postoj znajdzNajblizszyPostoj(HashMap<Long, Postoj> mapaPostojow, LocalDateTime terminDocelowy, boolean valueByOdjazd) {
        ArrayList<Postoj> sortedPostoje = new ArrayList<>(mapaPostojow.values());
        Postoj najblizszyPostoj = null;
        long minimalnaRoznica = Long.MAX_VALUE;

        for (Postoj postoj : sortedPostoje) {
            LocalDateTime czasXjazdu;
            if(valueByOdjazd)
                czasXjazdu = (postoj.getPlanowanyCzasOdjazdu() != null) ? postoj.getPlanowanyCzasOdjazdu() : LocalDateTime.MAX;
            else
                czasXjazdu = (postoj.getPlanowanyCzasPrzyjazdu() != null) ? postoj.getPlanowanyCzasPrzyjazdu() : LocalDateTime.MAX;
            long roznica = Math.abs(Duration.between(terminDocelowy, czasXjazdu).toMinutes());
            if (roznica < minimalnaRoznica) {
                minimalnaRoznica = roznica;
                najblizszyPostoj = postoj;
            }
        }
        return najblizszyPostoj;
    }
}