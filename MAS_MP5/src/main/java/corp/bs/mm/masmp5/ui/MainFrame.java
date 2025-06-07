package corp.bs.mm.masmp5.ui;

import corp.bs.mm.masmp5.enums.TypOsoby;
import corp.bs.mm.masmp5.model.Osoba;
import corp.bs.mm.masmp5.model.Postoj;
import corp.bs.mm.masmp5.model.Stacja;
import corp.bs.mm.masmp5.repository.OsobaRepository;
import corp.bs.mm.masmp5.repository.PolaczenieRepository;
import corp.bs.mm.masmp5.repository.StacjaRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {

    @Getter
    private final StacjaRepository stacjaRepo;
    private final OsobaRepository osobaRepo;
    private final PolaczenieRepository polaczenieRepo;

    //atrybuty do wyszukiwania
    @Getter
    private Stacja stacjaStart;
    @Getter
    private Stacja stacjaEnd;
    private Postoj postojStart;
    private Postoj postojEnd;
    @Getter
    private LocalDateTime terminStart;
    @Getter
    private LocalDateTime terminEnd;
    private LocalDateTime displayTerminPrev;
    private LocalDateTime displayTerminNext;


    @Getter
    private Osoba zalogowanyUser;
    @Getter
    private JPanel cardsPanel;
    @Getter
    private CardLayout cardLayout;
    private JPanel navBar;

    public MainFrame(StacjaRepository stacjaRepo,
                     OsobaRepository osobaRepo,
                     PolaczenieRepository polaczenieRepo) {

        this.stacjaRepo = stacjaRepo;
        this.osobaRepo = osobaRepo;
        this.polaczenieRepo = polaczenieRepo;

        this.zalogowanyUser = null;

        stacjaStart=null;
        stacjaEnd=null;
        postojStart=null;
        postojEnd=null;
        terminStart=null;
        terminEnd=null;
        displayTerminPrev=null;
        displayTerminNext=null;

        setTitle("Saturnal");
        setSize(700, 450);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.add(new JPanel(), "KONTO");

        JPanel homePanel = new JPanel();
        homePanel.setBackground(new Color(255, 255, 155));
        homePanel.add(new JLabel("Witaj na stronie głównej!"));

        JPanel searchPanel = new WyszukiwarkaPolaczenPanel(stacjaRepo, this);

        cardsPanel.add(homePanel, "HOME");
        cardsPanel.add(searchPanel, "SEARCH");

        // NAVBAR tworzony dynamicznie
        navBar = new JPanel(new BorderLayout());
        updateNavBar(); // <- pierwsze załadowanie

        setLayout(new BorderLayout());
        add(navBar, BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);

        JButton btnGoSearch = new JButton("Wyszukiwarka");
        btnGoSearch.addActionListener(e -> cardLayout.show(cardsPanel, "SEARCH"));
        homePanel.add(btnGoSearch);

        cardLayout.show(cardsPanel, "HOME");

        setVisible(true);
    }

    /**
     * Tworzy i aktualizuje pasek nawigacji w zależności od stanu zalogowania
     */
    private void updateNavBar() {
        navBar.removeAll();
        Color bgColor = new Color(224, 224, 224);
        navBar.setBackground(bgColor);
        navBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JButton btnHome = new JButton("Strona główna");
        btnHome.addActionListener(e -> cardLayout.show(cardsPanel, "HOME"));

        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtons.setBackground(bgColor);

        if (zalogowanyUser != null) {
            JButton btnKonto = new JButton("Konto");
            btnKonto.addActionListener(e -> {
                if (zalogowanyUser != null) {
                    JPanel kontoPanel = new KontoPanel(zalogowanyUser);

                    // Usuń starą wersję karty "KONTO", jeśli istnieje
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

            JButton btnWyloguj = new JButton("Wyloguj się");

            btnWyloguj.addActionListener(e -> {
                zalogowanyUser = null;
                JOptionPane.showMessageDialog(this, "Wylogowano.");

                // wróć na stronę główną
                cardLayout.show(cardsPanel, "HOME");

                updateNavBar(); // odśwież navbar
            });

            rightButtons.add(btnKonto);
            rightButtons.add(btnWyloguj);
        } else {
            JButton btnZaloguj = new JButton("Zaloguj się");
            JButton btnZarejestruj = new JButton("Zarejestruj się");

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

                // Usuń starą kartę LOGIN, jeśli istnieje
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
}