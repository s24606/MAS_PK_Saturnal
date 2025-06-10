package corp.bs.mm.masmp5.ui;
import corp.bs.mm.masmp5.model.*;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ZakupBiletuBezposredniegoPanel extends JPanel {

    private boolean miejscaWymagajaRezerwacji;


    public ZakupBiletuBezposredniegoPanel(MainFrame mainFrame, Polaczenie wybranePolaczenie) {
        Color paleCyan = new Color(155, 255, 255);

        setLayout(new BorderLayout());
        setBackground(paleCyan);
        Font desc = new Font("Arial", Font.BOLD, 12);
        Font value = new Font("Arial", Font.PLAIN, 12);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(paleCyan);

        // Panel formPanel
        JPanel infoPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        infoPanel.setBackground(paleCyan);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)(getPreferredSize().height * 0.6)));

        JLabel przewoznikLabel = new JLabel("Przewoźnik:");
        przewoznikLabel.setFont(desc);
        infoPanel.add(przewoznikLabel);
        JLabel przewoznikValue = new JLabel(wybranePolaczenie.getPociagKursujacy().getPrzewoznik());
        przewoznikValue.setFont(value);
        infoPanel.add(przewoznikValue);

        Postoj postojS = mainFrame.getWyszukanePostojeS().get(wybranePolaczenie.getPolaczenieId());
        Postoj postojE = mainFrame.getWyszukanePostojeE().get(wybranePolaczenie.getPolaczenieId());
        long roznicaMinuty = Duration.between(postojS.getPlanowanyCzasOdjazdu(), postojE.getPlanowanyCzasPrzyjazdu()).toMinutes();
        String czasPrzejazdu = (roznicaMinuty < 60) ? roznicaMinuty + "m" : roznicaMinuty / 60 + "h " + roznicaMinuty % 60 + "m";
        JLabel czasPrzejazduLabel = new JLabel("Czas przejazdu:");
        czasPrzejazduLabel.setFont(desc);
        infoPanel.add(czasPrzejazduLabel);
        JLabel czasPrzejazduValue = new JLabel(czasPrzejazdu);
        czasPrzejazduValue.setFont(value);
        infoPanel.add(czasPrzejazduValue);

        JLabel nrPolaczeniaLabel = new JLabel("Nr połączenia:");
        nrPolaczeniaLabel.setFont(desc);
        infoPanel.add(nrPolaczeniaLabel);
        JLabel nrPolaczeniaValue = new JLabel(wybranePolaczenie.getOznaczeniePolaczenia());
        nrPolaczeniaValue.setFont(value);
        infoPanel.add(nrPolaczeniaValue);

        LocalDateTime odjazd = postojS.getPlanowanyCzasOdjazdu();
        JLabel odjazdLabel = new JLabel("Odjazd:");
        odjazdLabel.setFont(desc);
        infoPanel.add(odjazdLabel);
        JLabel odjazdValue = new JLabel(odjazd.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")));
        odjazdValue.setFont(value);
        infoPanel.add(odjazdValue);

        Stacja stacjaStart = mainFrame.getStacjaStart();
        JLabel odLabel = new JLabel("Od:");
        odLabel.setFont(desc);
        infoPanel.add(odLabel);
        JLabel odValue = new JLabel(stacjaStart.getNazwa());
        odValue.setFont(value);
        infoPanel.add(odValue);

        Stacja stacjaEnd = mainFrame.getStacjaEnd();
        JLabel doLabel = new JLabel("Do:");
        doLabel.setFont(desc);
        infoPanel.add(doLabel);
        JLabel doValue = new JLabel(stacjaEnd.getNazwa());
        doValue.setFont(value);
        infoPanel.add(doValue);

        JLabel cenaLabel = new JLabel("Twoja cena:");
        cenaLabel.setFont(desc);
        infoPanel.add(cenaLabel);
        double cena = Bilet.obliczCeneBiletu(mainFrame.getZalogowanyUser(), postojS.getPlanowanyCzasOdjazdu(), postojE.getPlanowanyCzasPrzyjazdu());
        JLabel cenaValue = new JLabel(cena +" zł");
        cenaValue.setFont(value);
        infoPanel.add(cenaValue);

        // Panel formPanel
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(paleCyan);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)(getPreferredSize().height * 0.6)));

        Pociag kursujacy = wybranePolaczenie.getPociagKursujacy();
        miejscaWymagajaRezerwacji = kursujacy.isObowiazekRezerwacjiMiejsc();

        // Przycisk wstecz
        JButton backToWyniki = new JButton("Wróć do wyszukiwania");
        backToWyniki.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(backToWyniki, BorderLayout.NORTH);
        backToWyniki.addActionListener(e -> {
            mainFrame.getCardLayout().show(mainFrame.getCardsPanel(), "WYNIKI_BEZPOSREDNIE");
        });

        // Grid 2x2 dla wagonu i miejsca
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        gridPanel.setBackground(paleCyan);

        // nagłówki w gridzie
        JLabel wagonHeaderLabel = new JLabel("Wagon", SwingConstants.CENTER);
        wagonHeaderLabel.setBackground(Color.BLUE);
        wagonHeaderLabel.setForeground(Color.WHITE);
        wagonHeaderLabel.setOpaque(true);
        gridPanel.add(wagonHeaderLabel);

        JLabel miejsceHeaderLabel = new JLabel("Miejsce", SwingConstants.CENTER);
        miejsceHeaderLabel.setBackground(Color.BLUE);
        miejsceHeaderLabel.setForeground(Color.WHITE);
        miejsceHeaderLabel.setOpaque(true);
        gridPanel.add(miejsceHeaderLabel);

        // dropboxy z numerami
        HashMap<Integer, Wagon> wagony = posortujWagonyPoNumerze(kursujacy);
        ArrayList<Integer> nrWagonow = new ArrayList<>(wagony.keySet());
        JComboBox<Integer> wagonComboBox = new JComboBox<>(nrWagonow.toArray(new Integer[0]));
        gridPanel.add(wagonComboBox);

        /*wymaga dodania fetchtype.EAGER w relacji wagon-miejsce
        selectedWagon = wagony.get(wagonComboBox.getSelectedItem());
        HashMap<Integer, Miejsce> miejsca = posortujMiejscaPoNumerze(selectedWagon);
        ArrayList<Integer> nrMiejsc = new ArrayList<>(miejsca.keySet());
        JComboBox<Integer> miejsceComboBox = new JComboBox<>(nrMiejsc.toArray(new Integer[0]));*/
        JComboBox<String> miejsceComboBox = new JComboBox<>(new String[]{"1A", "1B", "2A", "2B"});
        gridPanel.add(miejsceComboBox);

        formPanel.add(gridPanel, BorderLayout.CENTER);

        // Panel z checkboxami (pod gridem)
        JPanel typyPanel = new JPanel(new BorderLayout());
        typyPanel.setBackground(paleCyan);

        JLabel udogodnieniaLabel = new JLabel("Udogodnienia:");
        typyPanel.add(udogodnieniaLabel, BorderLayout.NORTH);

        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBackground(paleCyan);

        JCheckBox miejsceRowCheckBox = new JCheckBox("Miejsce na rower");
        miejsceRowCheckBox.setBackground(paleCyan);
        JCheckBox miejsceInwCheckBox = new JCheckBox("Miejsce dla inwalidów");
        miejsceInwCheckBox.setBackground(paleCyan);
        JCheckBox miejsceStoCheckBox = new JCheckBox("Miejsce ze stolikiem");
        miejsceStoCheckBox.setBackground(paleCyan);

        checkboxPanel.add(miejsceRowCheckBox);
        checkboxPanel.add(miejsceInwCheckBox);
        checkboxPanel.add(miejsceStoCheckBox);

        typyPanel.add(checkboxPanel, BorderLayout.CENTER);

        formPanel.add(typyPanel, BorderLayout.SOUTH);

        // Układanie formPanel i infoPanel obok siebie z równą szerokością
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        contentPanel.setBackground(paleCyan);

        // Wrapper dla formPanel żeby ograniczyć jego wysokość
        JPanel formPanelWrapper = new JPanel(new BorderLayout());
        formPanelWrapper.setBackground(paleCyan);
        formPanelWrapper.add(formPanel, BorderLayout.NORTH);
        contentPanel.add(formPanelWrapper);

        // Wrapper dla infoPanel żeby ograniczyć jego wysokość
        JPanel infoPanelWrapper = new JPanel(new BorderLayout());
        infoPanelWrapper.setBackground(paleCyan);
        infoPanelWrapper.add(infoPanel, BorderLayout.NORTH);
        contentPanel.add(infoPanelWrapper);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Panel przycisku na dole, na środku
        JPanel confirmPanel = new JPanel();
        confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.Y_AXIS));
        confirmPanel.setBackground(paleCyan);
        JCheckBox rezerwowaneMiejsce = new JCheckBox("Czy chcesz zarezerwować konkretne miejsce?");
        rezerwowaneMiejsce.setBackground(paleCyan);
        rezerwowaneMiejsce.setSelected(true);
        rezerwowaneMiejsce.addActionListener(e -> {
            miejsceComboBox.setEnabled(rezerwowaneMiejsce.isSelected());
            wagonComboBox.setEnabled(rezerwowaneMiejsce.isSelected());
            miejsceInwCheckBox.setEnabled(rezerwowaneMiejsce.isSelected());
            miejsceRowCheckBox.setEnabled(rezerwowaneMiejsce.isSelected());
            miejsceStoCheckBox.setEnabled(rezerwowaneMiejsce.isSelected());
        });
        if(!miejscaWymagajaRezerwacji) {
            confirmPanel.add(rezerwowaneMiejsce);
        }
        JButton confirmButton = new JButton("Potwierdź zakup");
        confirmButton.addActionListener(e ->{
            if(postojS.getFaktycznyCzasOdjazdu()!=null)
                JOptionPane.showMessageDialog(this, "Czas rezerwacji miejsc na przejazd z "+stacjaStart.getNazwa()+" w ramach połączenia "+wybranePolaczenie.getOznaczeniePolaczenia()+" minął.");
            else {
                try {
                    if (!rezerwowaneMiejsce.isSelected()) {
                        BiletBezposredni kupionyBilet = BiletBezposredni.builder()
                                .cena(cena)
                                .stacjaOdjazd(stacjaStart)
                                .stacjaPrzyjazd(stacjaEnd)
                                .polaczenie(wybranePolaczenie)
                                .kupujacy(mainFrame.getZalogowanyUser())
                                .build();
                        mainFrame.getBiletRepository().save(kupionyBilet);
                        mainFrame.getBiletBezposredniRepository().save(kupionyBilet);

                        JOptionPane.showMessageDialog(this, "Bilet został kupiony! Możesz go znaleźć w sekcji \"Moje bilety\"");
                        mainFrame.showHome();
                    }

                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(this, exc);
                }
            }
        });

        confirmPanel.add(confirmButton);

        JPanel confirmWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        confirmWrapper.setBackground(paleCyan);
        confirmWrapper.add(confirmPanel);

        mainPanel.add(confirmWrapper, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    public HashMap<Integer, Wagon> posortujWagonyPoNumerze(Pociag pociag) {
        ArrayList<Wagon> posortowaneWagony = new ArrayList<>(pociag.getWagony());
        posortowaneWagony.sort(Comparator.comparingInt(Wagon::getNrWagonu));
        HashMap<Integer,Wagon> mapa = new HashMap<>();
        for(Wagon w:posortowaneWagony)
            mapa.put(w.getNrWagonu(),w);
        return mapa;
    }

    public HashMap<Integer, Miejsce> posortujMiejscaPoNumerze(Wagon wagon) {
        ArrayList<Miejsce> posortowaneMiejsca = new ArrayList<>(wagon.getMiejsca());
        posortowaneMiejsca.sort(Comparator.comparingInt(Miejsce::getNrMiejsca));
        HashMap<Integer,Miejsce> mapa = new HashMap<>();
        for(Miejsce w:posortowaneMiejsca)
            mapa.put(w.getNrMiejsca(),w);
        return mapa;
    }

}