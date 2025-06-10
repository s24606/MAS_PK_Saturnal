package corp.bs.mm.masmp5.ui;

import corp.bs.mm.masmp5.enums.TypOsoby;
import corp.bs.mm.masmp5.model.Polaczenie;
import corp.bs.mm.masmp5.model.Postoj;
import corp.bs.mm.masmp5.model.Stacja;
import corp.bs.mm.masmp5.repository.StacjaRepository;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.time.LocalDateTime;

public class WynikiWyszukiwaniaBezposredniegoPanel extends JPanel {
    public WynikiWyszukiwaniaBezposredniegoPanel(MainFrame mainFrame) {

        Stacja stacjaStart = mainFrame.getStacjaStart();
        Stacja stacjaEnd = mainFrame.getStacjaEnd();
        LocalDateTime terminStart = mainFrame.getTerminStart();
        LocalDateTime terminEnd = mainFrame.getTerminEnd();
        StacjaRepository stacjaRepo = mainFrame.getStacjaRepo();
        Color paleCyan = new Color(155, 255, 255);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(paleCyan);

        // Nagłówek
        LocalDateTime termin = terminStart != null ? terminStart : terminEnd;
        JLabel header = new JLabel("Połączenia " + stacjaStart.getNazwa() + " - " + stacjaEnd.getNazwa() +
                " na " + termin.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(header);

        add(Box.createRigidArea(new Dimension(0, 15)));

        // Panele dla stacji początkowej i końcowej
        JPanel stacjePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        stacjePanel.setBackground(paleCyan);
        stacjePanel.add(new JLabel("Stacja początkowa:"));
        List<Stacja> listaStacji = (List<Stacja>) stacjaRepo.findAll();
        List<String> nazwyStacji = new ArrayList<>();
        for (Stacja stacja : listaStacji) {
            nazwyStacji.add(stacja.getNazwa());
        }
        JComboBox<String> comboStart = new JComboBox<>(nazwyStacji.toArray(new String[0]));
        stacjePanel.add(comboStart);
        comboStart.setSelectedIndex(nazwyStacji.indexOf(stacjaStart.getNazwa()));
        stacjePanel.add(new JLabel("Stacja końcowa:"));
        JComboBox<String> comboEnd = new JComboBox<>(nazwyStacji.toArray(new String[0]));
        stacjePanel.add(comboEnd);
        comboEnd.setSelectedIndex(nazwyStacji.indexOf(stacjaEnd.getNazwa()));

        add(stacjePanel);

        // Panel z czasem
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        timePanel.setBackground(paleCyan);
        timePanel.add(new JLabel("Wyszukaj po czasie:"));

        // Radio buttons
        JRadioButton rbOdjazdu = new JRadioButton("Odjazdu");
        rbOdjazdu.setBackground(paleCyan);
        JRadioButton rbPrzyjazdu = new JRadioButton("Przyjazdu");
        rbPrzyjazdu.setBackground(paleCyan);
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(rbOdjazdu);
        radioGroup.add(rbPrzyjazdu);

        if(terminStart!=null&&terminEnd==null)
            rbOdjazdu.setSelected(true);
        else
            rbPrzyjazdu.setSelected(true);
        timePanel.add(rbOdjazdu);
        timePanel.add(rbPrzyjazdu);

        // Day, Month, Year combo boxes
        String[] dni = new String[31];
        for (int i = 1; i <= 31; i++) {
            dni[i-1] = String.valueOf(i);
        }
        JComboBox<String> dayCombo = new JComboBox<>(dni);
        dayCombo.setPreferredSize(new Dimension(40, 25));

        String[] miesiace = new String[12];
        for (int i = 1; i <= 12; i++) {
            miesiace[i-1] = String.valueOf(i);
        }
        JComboBox<String> monthCombo = new JComboBox<>(miesiace);
        monthCombo.setPreferredSize(new Dimension(40, 25));

        String[] lata = new String[2];
        lata[0] = String.valueOf(LocalDateTime.now().getYear());
        lata[1] = String.valueOf(LocalDateTime.now().getYear() + 1);
        JComboBox<String> yearCombo = new JComboBox<>(lata);
        yearCombo.setPreferredSize(new Dimension(60, 25));

        //przypisanie wartości comboboxom daty
        dayCombo.setSelectedIndex(termin.getDayOfMonth()-1);
        monthCombo.setSelectedIndex(termin.getMonthValue()-1);
        yearCombo.setSelectedIndex(termin.getYear()==LocalDateTime.now().getYear()?0:1);

        // Czas (Spinner)
        SpinnerDateModel timeModel = new SpinnerDateModel();
        JSpinner timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(Date.from((termin.atZone(ZoneId.systemDefault())).toInstant()));

        // Dodanie wszystkich elementów do panelu
        timePanel.add(dayCombo);
        timePanel.add(monthCombo);
        timePanel.add(yearCombo);
        timePanel.add(timeSpinner);

        add(timePanel);

        // Przyciski
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setBackground(paleCyan);
        JButton btnBezposrednie = new JButton("Wyszukaj połączenia bezpośrednie");
        JButton btnPrzesiadkowe = new JButton("Wyszukaj połączenia przesiadkowe");

        btnBezposrednie.addActionListener(e -> {
            if(comboStart.getSelectedIndex()==comboEnd.getSelectedIndex()){
                JOptionPane.showMessageDialog(this, "Wybierz różne stacje początkową i końcową.");
            }else {
                Component[] components = mainFrame.getCardsPanel().getComponents();
                for (Component c : components) {
                    if ("WYNIKI_BEZPOSREDNIE".equals(c.getName())) {
                        mainFrame.getCardsPanel().remove(c);
                        break;
                    }
                }

                int rok = Integer.parseInt((String) yearCombo.getSelectedItem());
                LocalTime time = ((Date) timeSpinner.getValue()).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalTime();
                LocalDateTime terminResult = termin.
                        withYear(rok).
                        withMonth(monthCombo.getSelectedIndex() + 1).
                        withDayOfMonth(dayCombo.getSelectedIndex() + 1).
                        withHour(time.getHour()).
                        withMinute(time.getMinute()).
                        withSecond(0).
                        withNano(0);
                mainFrame.setAtrybutyWyszukiwania(
                        listaStacji.get(comboStart.getSelectedIndex()),
                        listaStacji.get(comboEnd.getSelectedIndex()),
                        (rbOdjazdu.isSelected() ? terminResult : null),
                        (rbPrzyjazdu.isSelected() ? terminResult : null)
                );
                // Tworzymy nowy panel z wynikami
                WynikiWyszukiwaniaBezposredniegoPanel wynikiPanel = new WynikiWyszukiwaniaBezposredniegoPanel(mainFrame);
                wynikiPanel.setName("WYNIKI_BEZPOSREDNIE");
                mainFrame.getCardsPanel().add(wynikiPanel, "WYNIKI_BEZPOSREDNIE");


                mainFrame.getCardLayout().show(mainFrame.getCardsPanel(), "WYNIKI_BEZPOSREDNIE");

                // Odświeżamy layout
                mainFrame.getCardsPanel().revalidate();
                mainFrame.getCardsPanel().repaint();
            }
        });

        btnPrzesiadkowe.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funkcja obecnie niedostępna.");
        });

        buttonsPanel.add(btnBezposrednie);
        buttonsPanel.add(btnPrzesiadkowe);

        add(buttonsPanel);


        ArrayList<Polaczenie> wyszukanePolaczenia = mainFrame.findPolaczeniaForStacje();
        if(!wyszukanePolaczenia.isEmpty()) {
            // GridLayout
            JPanel gridPanel = new JPanel(new GridLayout(6, 5, 0, 0));
            gridPanel.setBackground(new Color(83, 145, 234));
            gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            Polaczenie pierwszeWidoczne = mainFrame.getBestPolaczenie();
            int indexPierwszegoWidocznego = 0;
            for (int i = 0; i < wyszukanePolaczenia.size(); i++) {
                if (wyszukanePolaczenia.get(i).equals(pierwszeWidoczne))
                    indexPierwszegoWidocznego = i;
            }
            int widocznailosc = 5;
            if (wyszukanePolaczenia.size() - widocznailosc < indexPierwszegoWidocznego) {
                indexPierwszegoWidocznego = wyszukanePolaczenia.size() - widocznailosc;
                if (indexPierwszegoWidocznego < 0)
                    indexPierwszegoWidocznego = 0;
            }

            // Pierwszy rząd - Nagłówki
            ArrayList<String> gridHeaders = new ArrayList<>(List.of(
                    "Nr połączenia", "Data odjazdu", "Czas", "Przewoźnik", "Akcje"
            ));
            for (String gridHeader : gridHeaders) {
                JLabel headerLabel = new JLabel(gridHeader, SwingConstants.CENTER);
                headerLabel.setForeground(Color.WHITE);
                gridPanel.add(headerLabel);
            }

            // Dodanie lini między komórkami i wypełnienie komórek danymi (w tym przyciskami)
            for (int i = 0; i < 5 && i < wyszukanePolaczenia.size(); i++) {
                Polaczenie wyswietlanePolaczenie = wyszukanePolaczenia.get(i + indexPierwszegoWidocznego);
                for (int j = 0; j < 5; j++) {
                    JPanel cellPanel = new JPanel();
                    cellPanel.setBackground(new Color(225, 255, 255));
                    cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    // Ustawiamy layout na BoxLayout, aby przyciski były jeden pod drugim
                    cellPanel.setLayout(new BoxLayout(cellPanel, BoxLayout.Y_AXIS));

                    if (j == 0) {
                        JLabel oznaczenie = new JLabel(wyswietlanePolaczenie.getOznaczeniePolaczenia());
                        oznaczenie.setHorizontalAlignment(SwingConstants.CENTER);
                        oznaczenie.setAlignmentX(Component.CENTER_ALIGNMENT);
                        cellPanel.add(oznaczenie);
                    }

                    if (j == 1) {
                        LocalDateTime odjazd = mainFrame.getWyszukanePostojeS()
                                .get(wyswietlanePolaczenie.getPolaczenieId())
                                .getPlanowanyCzasOdjazdu();
                        JLabel terminOdjazdu = new JLabel(odjazd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                        terminOdjazdu.setHorizontalAlignment(SwingConstants.CENTER);
                        terminOdjazdu.setAlignmentX(Component.CENTER_ALIGNMENT);
                        cellPanel.add(terminOdjazdu);
                    }

                    if (j == 2) {
                        Postoj postojS = mainFrame.getWyszukanePostojeS().get(wyswietlanePolaczenie.getPolaczenieId());
                        Postoj postojE = mainFrame.getWyszukanePostojeE().get(wyswietlanePolaczenie.getPolaczenieId());
                        long roznicaMinuty = Duration.between(postojS.getPlanowanyCzasOdjazdu(), postojE.getPlanowanyCzasPrzyjazdu()).toMinutes();
                        String czasStr;
                        if (roznicaMinuty < 60) {
                            czasStr = roznicaMinuty + "m";
                        } else {
                            long godziny = roznicaMinuty / 60;
                            long minuty = roznicaMinuty % 60;
                            czasStr = godziny + "h " + minuty + "m";
                        }
                        JLabel terminOdjazdu = new JLabel(czasStr);
                        terminOdjazdu.setHorizontalAlignment(SwingConstants.CENTER);
                        terminOdjazdu.setAlignmentX(Component.CENTER_ALIGNMENT);
                        cellPanel.add(terminOdjazdu);
                    }

                    if (j == 3) {
                        JLabel przewoznik = new JLabel(wyswietlanePolaczenie.getPociagKursujacy().getPrzewoznik());
                        przewoznik.setHorizontalAlignment(SwingConstants.CENTER);
                        przewoznik.setAlignmentX(Component.CENTER_ALIGNMENT);
                        cellPanel.add(przewoznik);
                    }

                    if (j == 4) {
                        JPanel buttonRow = new JPanel();
                        buttonRow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
                        buttonRow.setOpaque(false);

                        JButton detailsButton = new JButton("Szczegóły");
                        detailsButton.setFont(detailsButton.getFont().deriveFont(10f));
                        detailsButton.setMargin(new Insets(2, 4, 2, 4));
                        buttonRow.add(detailsButton);

                        detailsButton.addActionListener(e -> {
                            Component[] components = mainFrame.getCardsPanel().getComponents();
                            for (Component c : components) {
                                if ("SZCZEGOLY_BILETU".equals(c.getName())) {
                                    mainFrame.getCardsPanel().remove(c);
                                    break;
                                }
                            }
                            SzczegolyPolaczeniaPanel szczegolyPolaczeniaPanel  = new SzczegolyPolaczeniaPanel (mainFrame, wyswietlanePolaczenie);
                            mainFrame.getCardsPanel().add(szczegolyPolaczeniaPanel, "SZCZEGOLY_BILETU");
                            mainFrame.getCardLayout().show(mainFrame.getCardsPanel(), "SZCZEGOLY_BILETU");
                        });

                        if (mainFrame.getZalogowanyUser() != null)
                            if (mainFrame.getZalogowanyUser().getRole().contains(TypOsoby.PASAZER)) {
                                JButton buyTicketButton = new JButton("Bilet");
                                buyTicketButton.setFont(buyTicketButton.getFont().deriveFont(10f));
                                detailsButton.setMargin(new Insets(2, 4, 2, 4));
                                buttonRow.add(buyTicketButton);

                                buyTicketButton.addActionListener(e -> {
                                    Postoj postojS = mainFrame.getWyszukanePostojeS().get(wyswietlanePolaczenie.getPolaczenieId());
                                    if(postojS.getFaktycznyCzasOdjazdu()!=null)
                                        JOptionPane.showMessageDialog(this, "Czas rezerwacji miejsc na przejazd z "+ postojS.getStacja().getNazwa()+" w ramach połączenia "+wyswietlanePolaczenie.getOznaczeniePolaczenia()+" minął.");
                                    else {
                                        Component[] components = mainFrame.getCardsPanel().getComponents();
                                        for (Component c : components) {
                                            if ("ZAKUP_BILETU".equals(c.getName())) {
                                                mainFrame.getCardsPanel().remove(c);
                                                break;
                                            }
                                        }
                                        ZakupBiletuBezposredniegoPanel zakupBiletuPanel = new ZakupBiletuBezposredniegoPanel(mainFrame, wyswietlanePolaczenie);
                                        mainFrame.getCardsPanel().add(zakupBiletuPanel, "ZAKUP_BILETU");
                                        mainFrame.getCardLayout().show(mainFrame.getCardsPanel(), "ZAKUP_BILETU");
                                    }
                                });
                            }

                        cellPanel.add(buttonRow);
                    }

                    gridPanel.add(cellPanel);
                }
            }

            add(gridPanel);

            add(Box.createRigidArea(new Dimension(0, 15)));

            // Przyciski do wcześniejszych i późniejszych połączeń
            JPanel horizontalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 0));  // FlowLayout z lewym wyrównaniem
            horizontalPanel.setBackground(paleCyan);
            JButton btnEarlier = new JButton("Wyszukaj wcześniejsze połączenia");
            JButton btnLater = new JButton("Wyszukaj późniejsze połączenia");

            Dimension buttonSize = new Dimension(btnEarlier.getPreferredSize().width,
                    btnLater.getPreferredSize().height);
            btnLater.setPreferredSize(buttonSize);

            if (indexPierwszegoWidocznego > 0) {
                horizontalPanel.add(btnEarlier);
            } else {
                JLabel emptyLabel = new JLabel();
                emptyLabel.setPreferredSize(buttonSize);
                horizontalPanel.add(emptyLabel);
            }

            if (indexPierwszegoWidocznego < wyszukanePolaczenia.size() - 5) {
                horizontalPanel.add(btnLater);
            } else {
                JLabel emptyLabel = new JLabel();
                emptyLabel.setPreferredSize(buttonSize);
                horizontalPanel.add(emptyLabel);
            }

            btnEarlier.addActionListener(e -> {
                int index = 0;
                for (int i = 0; i < wyszukanePolaczenia.size(); i++) {
                    if (wyszukanePolaczenia.get(i).equals(pierwszeWidoczne))
                        index = i;
                }

                if (index != 0) {

                    index -= 5;
                    if (wyszukanePolaczenia.size() - widocznailosc < index)
                        index = wyszukanePolaczenia.size() - widocznailosc;
                    if (index < 0)
                        index = 0;

                    Component[] components = mainFrame.getCardsPanel().getComponents();
                    for (Component c : components) {
                        if ("WYNIKI_BEZPOSREDNIE".equals(c.getName())) {
                            mainFrame.getCardsPanel().remove(c);
                            break;
                        }
                    }

                    HashMap<Long, Postoj> mapaPostoje;
                    if (mainFrame.getTerminStart() != null) mapaPostoje = mainFrame.getWyszukanePostojeS();
                    else mapaPostoje = mainFrame.getWyszukanePostojeE();

                    ArrayList<Postoj> postoje = MainFrame.sortujPostojePoCzasie(mapaPostoje, mainFrame.getTerminStart()!=null);
                    if (mainFrame.getTerminStart() != null) mainFrame.setTerminStart(postoje.get(index).getPlanowanyCzasOdjazdu());
                    else mainFrame.setTerminEnd(postoje.get(index).getPlanowanyCzasPrzyjazdu());

                    WynikiWyszukiwaniaBezposredniegoPanel wynikiPanel = new WynikiWyszukiwaniaBezposredniegoPanel(mainFrame);
                    wynikiPanel.setName("WYNIKI_BEZPOSREDNIE");
                    mainFrame.getCardsPanel().add(wynikiPanel, "WYNIKI_BEZPOSREDNIE");
                    mainFrame.getCardLayout().show(mainFrame.getCardsPanel(), "WYNIKI_BEZPOSREDNIE");
                    mainFrame.getCardsPanel().revalidate();
                    mainFrame.getCardsPanel().repaint();
                }
            });

            btnLater.addActionListener(e -> {
                int index = 0;
                for (int i = 0; i < wyszukanePolaczenia.size(); i++) {
                    if (wyszukanePolaczenia.get(i).equals(pierwszeWidoczne))
                        index = i;
                }

                if (index < wyszukanePolaczenia.size()-5) {

                    index += 5;
                    if (wyszukanePolaczenia.size() - widocznailosc < index)
                        index = wyszukanePolaczenia.size() - widocznailosc;
                    if (index < 0)
                        index = 0;

                    Component[] components = mainFrame.getCardsPanel().getComponents();
                    for (Component c : components) {
                        if ("WYNIKI_BEZPOSREDNIE".equals(c.getName())) {
                            mainFrame.getCardsPanel().remove(c);
                            break;
                        }
                    }

                    HashMap<Long, Postoj> mapaPostoje;
                    if (mainFrame.getTerminStart() != null) mapaPostoje = mainFrame.getWyszukanePostojeS();
                    else mapaPostoje = mainFrame.getWyszukanePostojeE();

                    ArrayList<Postoj> postoje = MainFrame.sortujPostojePoCzasie(mapaPostoje, mainFrame.getTerminStart()!=null);
                    if (mainFrame.getTerminStart() != null) mainFrame.setTerminStart(postoje.get(index).getPlanowanyCzasOdjazdu());
                    else mainFrame.setTerminEnd(postoje.get(index).getPlanowanyCzasPrzyjazdu());

                    WynikiWyszukiwaniaBezposredniegoPanel wynikiPanel = new WynikiWyszukiwaniaBezposredniegoPanel(mainFrame);
                    wynikiPanel.setName("WYNIKI_BEZPOSREDNIE");
                    mainFrame.getCardsPanel().add(wynikiPanel, "WYNIKI_BEZPOSREDNIE");
                    mainFrame.getCardLayout().show(mainFrame.getCardsPanel(), "WYNIKI_BEZPOSREDNIE");
                    mainFrame.getCardsPanel().revalidate();
                    mainFrame.getCardsPanel().repaint();
                }
            });

            add(horizontalPanel);

        } else {

            JPanel brakPolaczenPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 200, 0));
            brakPolaczenPanel.setBackground(new Color(83, 145, 234));
            int polowaWysokosci = (int) (mainFrame.getHeight() * 0.45);
            brakPolaczenPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), polowaWysokosci));
            JLabel brakPolaczenLabel = new JLabel("Nie znaleziono połączeń bezpośrednich spełniających podane kryteria.");
            brakPolaczenLabel.setForeground(Color.WHITE);
            brakPolaczenLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            brakPolaczenLabel.setFont(new Font("Arial", Font.BOLD, 14));

            brakPolaczenPanel.add(brakPolaczenLabel);
            add(Box.createVerticalStrut(20));
            add(brakPolaczenPanel);
        }
    }
}
