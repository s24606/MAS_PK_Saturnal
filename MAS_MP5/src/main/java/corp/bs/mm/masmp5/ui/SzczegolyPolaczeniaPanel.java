package corp.bs.mm.masmp5.ui;

import corp.bs.mm.masmp5.enums.TypOsoby;
import corp.bs.mm.masmp5.model.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class SzczegolyPolaczeniaPanel extends JPanel {

    private Polaczenie wybranePolaczenie;

    public SzczegolyPolaczeniaPanel(MainFrame mainFrame, Polaczenie wybranePolaczenie) {
        this.wybranePolaczenie=wybranePolaczenie;

        Color paleCyan = new Color(155, 255, 255);
        setLayout(new BorderLayout());
        setBackground(paleCyan);

        // Panel dla szczegółów
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(paleCyan);

        // Panel infoPanel - na górze detailsPanel
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        infoPanel.setBackground(paleCyan);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel przewoznikLabel = new JLabel("Przewoźnik:");
        przewoznikLabel.setFont(new Font("Arial", Font.BOLD, 12));
        infoPanel.add(przewoznikLabel);
        JLabel przewoznikValue = new JLabel(wybranePolaczenie.getPociagKursujacy().getPrzewoznik());
        przewoznikValue.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(przewoznikValue);

        // Czas przejazdu
        Postoj postojS = mainFrame.getWyszukanePostojeS().get(wybranePolaczenie.getPolaczenieId());
        Postoj postojE = mainFrame.getWyszukanePostojeE().get(wybranePolaczenie.getPolaczenieId());
        long roznicaMinuty = Duration.between(postojS.getPlanowanyCzasOdjazdu(), postojE.getPlanowanyCzasPrzyjazdu()).toMinutes();
        String czasPrzejazdu = (roznicaMinuty < 60) ? roznicaMinuty + "m" : roznicaMinuty / 60 + "h " + roznicaMinuty % 60 + "m";
        JLabel czasPrzejazduLabel = new JLabel("Czas przejazdu:");
        czasPrzejazduLabel.setFont(new Font("Arial", Font.BOLD, 12));
        infoPanel.add(czasPrzejazduLabel);
        JLabel czasPrzejazduValue = new JLabel(czasPrzejazdu);
        czasPrzejazduValue.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(czasPrzejazduValue);

        // Numer połączenia
        JLabel nrPolaczeniaLabel = new JLabel("Nr połączenia:");
        nrPolaczeniaLabel.setFont(new Font("Arial", Font.BOLD, 12));
        infoPanel.add(nrPolaczeniaLabel);
        JLabel nrPolaczeniaValue = new JLabel(wybranePolaczenie.getOznaczeniePolaczenia());
        nrPolaczeniaValue.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(nrPolaczeniaValue);

        // Termin
        LocalDateTime odjazd = postojS.getPlanowanyCzasOdjazdu();
        JLabel odjazdLabel = new JLabel("Termin:");
        odjazdLabel.setFont(new Font("Arial", Font.BOLD, 12));
        infoPanel.add(odjazdLabel);
        JLabel odjazdValue = new JLabel(odjazd.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")));
        odjazdValue.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(odjazdValue);

        detailsPanel.add(infoPanel);

        // Grid 1x4 dla dostępnych udogodnień
        JPanel gridPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        gridPanel.setBackground(paleCyan);

        JLabel udogodnieniaLabel = new JLabel("Dostępne udogodnienia", SwingConstants.CENTER);
        udogodnieniaLabel.setBackground(Color.BLUE);
        udogodnieniaLabel.setForeground(Color.WHITE);
        udogodnieniaLabel.setOpaque(true);
        gridPanel.add(udogodnieniaLabel);

        JCheckBox bikePlaceCheckBox = new JCheckBox("Miejsce na rower");
        bikePlaceCheckBox.setEnabled(false);  // Nieinteraktywny
        bikePlaceCheckBox.setBackground(paleCyan);
        gridPanel.add(bikePlaceCheckBox);
        JCheckBox disabilityPlaceCheckBox = new JCheckBox("Miejsce dla inwalidów");
        disabilityPlaceCheckBox.setEnabled(false);  // Nieinteraktywny
        disabilityPlaceCheckBox.setBackground(paleCyan);
        gridPanel.add(disabilityPlaceCheckBox);
        JCheckBox tablePlaceCheckBox = new JCheckBox("Miejsce ze stolikiem");
        tablePlaceCheckBox.setEnabled(false);  // Nieinteraktywny
        tablePlaceCheckBox.setBackground(paleCyan);
        gridPanel.add(tablePlaceCheckBox);

        detailsPanel.add(gridPanel);

        // Przycisk "Kup Bilet"
        JButton kupBiletButton = new JButton("Kup Bilet");
        kupBiletButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (mainFrame.getZalogowanyUser() != null)
            if (mainFrame.getZalogowanyUser().getRole().contains(TypOsoby.PASAZER))
                detailsPanel.add(kupBiletButton);
        kupBiletButton.addActionListener(e -> {
            if(postojS.getFaktycznyCzasOdjazdu()!=null)
                JOptionPane.showMessageDialog(this, "Czas rezerwacji miejsc na przejazd z "+ postojS.getStacja().getNazwa()+" w ramach połączenia "+wybranePolaczenie.getOznaczeniePolaczenia()+" minął.");
            else {
            Component[] components = mainFrame.getCardsPanel().getComponents();
            for (Component c : components) {
                if ("ZAKUP_BILETU".equals(c.getName())) {
                    mainFrame.getCardsPanel().remove(c);
                    break;
                }
            }
            ZakupBiletuBezposredniegoPanel zakupBiletuPanel = new ZakupBiletuBezposredniegoPanel(mainFrame, wybranePolaczenie);
            mainFrame.getCardsPanel().add(zakupBiletuPanel, "ZAKUP_BILETU");
            mainFrame.getCardLayout().show(mainFrame.getCardsPanel(), "ZAKUP_BILETU");
            }
        });

        // Lewa część - lista postojów
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(paleCyan);

        // Przycisk wstecz
        JButton backToWyniki = new JButton("Wróć do wyszukiwania");
        backToWyniki.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(backToWyniki, BorderLayout.NORTH);
        backToWyniki.addActionListener(e -> {
            mainFrame.getCardLayout().show(mainFrame.getCardsPanel(), "WYNIKI_BEZPOSREDNIE");
        });

        // Tabela z postojami
        String[] columnNames = {"Stacja", "Przyjazd", "Odjazd"};
        List<Postoj> postoje = wybranePolaczenie.getPostoje();
        postoje.sort(new Comparator<Postoj>() {
            @Override
            public int compare(Postoj p1, Postoj p2) {
                LocalDateTime czasOdjazdu1 = p1.getPlanowanyCzasOdjazdu();
                LocalDateTime czasOdjazdu2 = p2.getPlanowanyCzasOdjazdu();
                // Jeśli czasOdjazdu1 jest null, traktujemy go jako "większy"
                if (czasOdjazdu1 == null && czasOdjazdu2 != null) {
                    return 1; // p1 (null) idzie na koniec
                }
                // Jeśli czasOdjazdu2 jest null, traktujemy go jako "większy"
                if (czasOdjazdu1 != null && czasOdjazdu2 == null) {
                    return -1; // p2 (null) idzie na koniec
                }
                return czasOdjazdu1.compareTo(czasOdjazdu2);
            }
        });
        Object[][] data = new Object[postoje.size()][3];
        for (int i = 0; i < postoje.size(); i++) {
            Postoj postoj = postoje.get(i);
            data[i][0] = postoj.getStacja().getNazwa();
            if(postoj.getPlanowanyCzasPrzyjazdu()!=null)
                data[i][1] = postoj.getPlanowanyCzasPrzyjazdu().format(DateTimeFormatter.ofPattern("HH:mm"));
            else
                data[i][1] = "X";
            if(postoj.getPlanowanyCzasOdjazdu()!=null)
                data[i][2] = postoj.getPlanowanyCzasOdjazdu().format(DateTimeFormatter.ofPattern("HH:mm"));
            else
                data[i][2] = "X";
        }

        // Tworzenie tabeli
        JTable table = new JTable(data, columnNames) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                // Sprawdzamy, czy jest to stacja początkowa lub końcowa
                Postoj postoj = postoje.get(row);
                Postoj postojS = mainFrame.getWyszukanePostojeS().get(wybranePolaczenie.getPolaczenieId());
                Postoj postojE = mainFrame.getWyszukanePostojeE().get(wybranePolaczenie.getPolaczenieId());
                if (postoj.getStacja().equals(postojS.getStacja()) || postoj.getStacja().equals(postojE.getStacja())) {
                    c.setBackground(Color.BLUE);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(getBackground());
                    c.setForeground(getForeground());
                }
                return c;
            }
        };
        JScrollPane scrollPane = new JScrollPane(table);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Dodanie paneli do głównego kontenera
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, detailsPanel);
        splitPane.setDividerLocation(400);
        splitPane.setEnabled(false);
        add(splitPane, BorderLayout.CENTER);
    }
}
