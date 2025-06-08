package corp.bs.mm.masmp5.ui;

import corp.bs.mm.masmp5.model.Polaczenie;
import corp.bs.mm.masmp5.model.Stacja;
import corp.bs.mm.masmp5.repository.PolaczenieRepository;
import corp.bs.mm.masmp5.repository.StacjaRepository;

import javax.swing.*;
import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

        rbOdjazdu.setSelected(terminStart!=null);
        rbPrzyjazdu.setSelected(terminEnd!=null);
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
        JButton btnDirect = new JButton("Wyszukaj połączenia bezpośrednie");
        JButton btnTransfer = new JButton("Wyszukaj połączenia przesiadkowe");

        btnDirect.addActionListener(e -> {
            Component[] components = mainFrame.getCardsPanel().getComponents();
            for (Component c : components) {
                if ("WYNIKI_BEZPOSREDNIE".equals(c.getName())) {
                    mainFrame.getCardsPanel().remove(c);
                    break;
                }
            }
            WynikiWyszukiwaniaBezposredniegoPanel wynikiPanel = new WynikiWyszukiwaniaBezposredniegoPanel(mainFrame);
            wynikiPanel.setName("WYNIKI_BEZPOSREDNIE");
            mainFrame.getCardsPanel().add(wynikiPanel, "WYNIKI_BEZPOSREDNIE");
            mainFrame.getCardLayout().show(mainFrame.getCardsPanel(), "WYNIKI_BEZPOSREDNIE");

            mainFrame.getCardsPanel().revalidate();
            mainFrame.getCardsPanel().repaint();
        });

        buttonsPanel.add(btnDirect);
        buttonsPanel.add(btnTransfer);

        add(buttonsPanel);

        // GridLayout
        JPanel gridPanel = new JPanel(new GridLayout(6, 5, 0,0)); // Wąskie odstępy
        gridPanel.setBackground(new Color(83, 145, 234));
        gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));







        ArrayList<Polaczenie> wyszukanePolaczenia = mainFrame.findPolaczeniaForStacje();






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
            Polaczenie wyswietlanePolaczenie = wyszukanePolaczenia.get(i);
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

                if (j == 3) {
                    JLabel przewoznik = new JLabel(wyswietlanePolaczenie.getPociagKursujacy().getPrzewoznik());
                    przewoznik.setHorizontalAlignment(SwingConstants.CENTER);
                    przewoznik.setAlignmentX(Component.CENTER_ALIGNMENT);
                    cellPanel.add(przewoznik);
                }

                // ostatnia kolumna (indeks 4) - dodajemy przyciski
                if (j == 4) {
                    JButton detailsButton = new JButton("Szczegóły");
                    detailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    cellPanel.add(detailsButton);

                    if (mainFrame.getZalogowanyUser() != null) {
                        JButton buyTicketButton = new JButton("Kup Bilet");
                        cellPanel.add(buyTicketButton);
                    }
                }

                gridPanel.add(cellPanel);
            }
        }

        add(gridPanel);

        add(Box.createRigidArea(new Dimension(0, 15)));

        // Tworzymy panel poziomy z dwoma przyciskami
        JPanel horizontalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 0));  // FlowLayout z lewym wyrównaniem
        horizontalPanel.setBackground(paleCyan);
        JButton btnEarlier = new JButton("Wyszukaj wcześniejsze połączenia");
        JButton btnLater = new JButton("Wyszukaj późniejsze połączenia");
        btnLater.setPreferredSize(
                new Dimension(btnEarlier.getPreferredSize().width,
                btnLater.getPreferredSize().height)
        );
        horizontalPanel.add(btnEarlier);
        horizontalPanel.add(btnLater);
        add(horizontalPanel);

    }
}
