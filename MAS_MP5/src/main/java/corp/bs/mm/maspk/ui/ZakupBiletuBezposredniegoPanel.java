package corp.bs.mm.maspk.ui;
import corp.bs.mm.maspk.enums.TypMiejsca;
import corp.bs.mm.maspk.model.*;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ZakupBiletuBezposredniegoPanel extends JPanel {

    private boolean miejscaWymagajaRezerwacji;
    private Polaczenie wybranePolaczenie;

    private JComboBox<String> miejsceComboBox;
    private JComboBox<Integer> wagonComboBox;

    private JCheckBox miejsceRowCheckBox;
    private JCheckBox miejsceInwCheckBox;
    private JCheckBox miejsceStoCheckBox;


    public ZakupBiletuBezposredniegoPanel(MainFrame mainFrame, Polaczenie wybranePolaczenie) {

        this.wybranePolaczenie=wybranePolaczenie;

        Color paleCyan = new Color(155, 255, 255);
        setLayout(new BorderLayout());
        setBackground(paleCyan);
        Font desc = new Font("Arial", Font.BOLD, 12);
        Font value = new Font("Arial", Font.PLAIN, 12);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(paleCyan);

        // Prawy panel - informacje o przejeździe
        JPanel infoPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        infoPanel.setBackground(paleCyan);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)(getPreferredSize().height * 0.6)));

        // Przewoźnik
        JLabel przewoznikLabel = new JLabel("Przewoźnik:");
        przewoznikLabel.setFont(desc);
        infoPanel.add(przewoznikLabel);
        JLabel przewoznikValue = new JLabel(wybranePolaczenie.getPociagKursujacy().getPrzewoznik());
        przewoznikValue.setFont(value);
        infoPanel.add(przewoznikValue);

        //Oznaczenie połączenia
        JLabel nrPolaczeniaLabel = new JLabel("Nr połączenia:");
        nrPolaczeniaLabel.setFont(desc);
        infoPanel.add(nrPolaczeniaLabel);
        JLabel nrPolaczeniaValue = new JLabel(wybranePolaczenie.getOznaczeniePolaczenia());
        nrPolaczeniaValue.setFont(value);
        infoPanel.add(nrPolaczeniaValue);

        //Odjazd
        Postoj postojS = mainFrame.getWyszukanePostojeS().get(wybranePolaczenie.getPolaczenieId());
        Postoj postojE = mainFrame.getWyszukanePostojeE().get(wybranePolaczenie.getPolaczenieId());
        LocalDateTime odjazd = postojS.getPlanowanyCzasOdjazdu();
        JLabel odjazdLabel = new JLabel("Odjazd:");
        odjazdLabel.setFont(desc);
        infoPanel.add(odjazdLabel);
        JLabel odjazdValue = new JLabel(odjazd.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")));
        odjazdValue.setFont(value);
        infoPanel.add(odjazdValue);

        // Czas Przejazdu
        long roznicaMinuty = Duration.between(postojS.getPlanowanyCzasOdjazdu(), postojE.getPlanowanyCzasPrzyjazdu()).toMinutes();
        String czasPrzejazdu = (roznicaMinuty < 60) ? roznicaMinuty + "m" : roznicaMinuty / 60 + "h " + roznicaMinuty % 60 + "m";
        JLabel czasPrzejazduLabel = new JLabel("Czas przejazdu:");
        czasPrzejazduLabel.setFont(desc);
        infoPanel.add(czasPrzejazduLabel);
        JLabel czasPrzejazduValue = new JLabel(czasPrzejazdu);
        czasPrzejazduValue.setFont(value);
        infoPanel.add(czasPrzejazduValue);

        // Stacja początkowa
        Stacja stacjaStart = mainFrame.getStacjaStart();
        JLabel odLabel = new JLabel("Od:");
        odLabel.setFont(desc);
        infoPanel.add(odLabel);
        JLabel odValue = new JLabel(stacjaStart.getNazwa());
        odValue.setFont(value);
        infoPanel.add(odValue);

        // Stacja końcowa
        Stacja stacjaEnd = mainFrame.getStacjaEnd();
        JLabel doLabel = new JLabel("Do:");
        doLabel.setFont(desc);
        infoPanel.add(doLabel);
        JLabel doValue = new JLabel(stacjaEnd.getNazwa());
        doValue.setFont(value);
        infoPanel.add(doValue);

        // Cena
        JLabel cenaLabel = new JLabel("Twoja cena:");
        cenaLabel.setFont(desc);
        infoPanel.add(cenaLabel);
        double cena = Bilet.obliczCeneBiletu(mainFrame.getZalogowanyUser(), postojS.getPlanowanyCzasOdjazdu(), postojE.getPlanowanyCzasPrzyjazdu());
        JLabel cenaValue = new JLabel(cena +" zł");
        cenaValue.setFont(value);
        infoPanel.add(cenaValue);

        // lewy panel - przycisk wstecz i wybór miejsca
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(paleCyan);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)(getPreferredSize().height * 0.6)));

        Pociag kursujacy = wybranePolaczenie.getPociagKursujacy();
        miejscaWymagajaRezerwacji = kursujacy.isObowiazekRezerwacjiMiejsc();

        // Przycisk wstecz
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(paleCyan);
        JButton backToWyniki = new JButton("Wróć do wyszukiwania");
        backToWyniki.setPreferredSize(new Dimension(backToWyniki.getPreferredSize().width, backToWyniki.getPreferredSize().height));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, backToWyniki.getPreferredSize().height, 0));
        buttonPanel.add(backToWyniki);

        formPanel.add(buttonPanel, BorderLayout.NORTH);
        backToWyniki.addActionListener(e -> {
            mainFrame.getCardLayout().show(mainFrame.getCardsPanel(), "WYNIKI_BEZPOSREDNIE");
        });

        // Grid 2x2 dla wagonu i miejsca
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        gridPanel.setBackground(paleCyan);

        // Nagłówki w gridzie
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

        // Dropboxy z numerami
        HashMap<Integer, Wagon> wagony = posortujWagonyPoNumerze(kursujacy);
        ArrayList<Integer> nrWagonow = new ArrayList<>(wagony.keySet());
        wagonComboBox = new JComboBox<>(nrWagonow.toArray(new Integer[0]));
        gridPanel.add(wagonComboBox);

        miejsceComboBox = new JComboBox<>();
        gridPanel.add(miejsceComboBox);
        wagonComboBox.addActionListener(e -> {
            defineMiejsceCombo();
        });

        formPanel.add(gridPanel, BorderLayout.CENTER);

        // CheckBox rezerwacji miejsca
        JPanel rezerwacjaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rezerwacjaPanel.setBackground(paleCyan);
        JCheckBox rezerwowaneMiejsce = new JCheckBox("Chcę zarezerwować konkretne miejsce");
        rezerwowaneMiejsce.setBackground(paleCyan);
        rezerwowaneMiejsce.setSelected(true);
        if(!miejscaWymagajaRezerwacji) {
            rezerwacjaPanel.add(rezerwowaneMiejsce);
        }

        // Panel filtrowania typu miejsc
        JPanel typyPanel = new JPanel(new BorderLayout());
        typyPanel.setBackground(paleCyan);

        JLabel udogodnieniaLabel = new JLabel("Udogodnienia:");
        typyPanel.add(udogodnieniaLabel, BorderLayout.NORTH);

        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBackground(paleCyan);

        miejsceRowCheckBox = new JCheckBox("Miejsce na rower");
        miejsceInwCheckBox = new JCheckBox("Miejsce dla inwalidów");
        miejsceStoCheckBox = new JCheckBox("Miejsce ze stolikiem");
        for(JCheckBox jcb: new ArrayList<>(Arrays.asList
                (miejsceRowCheckBox, miejsceInwCheckBox, miejsceStoCheckBox))) {
            jcb.setBackground(paleCyan);
            jcb.setSelected(false);
            jcb.addActionListener(e -> {
                defineMiejsceCombo();
            });
            checkboxPanel.add(jcb);
        }

        typyPanel.add(checkboxPanel, BorderLayout.CENTER);

        rezerwowaneMiejsce.addActionListener(e -> {
            miejsceComboBox.setEnabled(rezerwowaneMiejsce.isSelected());
            wagonComboBox.setEnabled(rezerwowaneMiejsce.isSelected());
            miejsceInwCheckBox.setEnabled(rezerwowaneMiejsce.isSelected());
            miejsceRowCheckBox.setEnabled(rezerwowaneMiejsce.isSelected());
            miejsceStoCheckBox.setEnabled(rezerwowaneMiejsce.isSelected());
        });

        // Panel zawierający checkbox rezerwacji i udogodnienia
        JPanel bottomFormPanel = new JPanel(new BorderLayout());
        bottomFormPanel.setBackground(paleCyan);
        bottomFormPanel.add(rezerwacjaPanel, BorderLayout.NORTH);
        bottomFormPanel.add(typyPanel, BorderLayout.CENTER);

        formPanel.add(bottomFormPanel, BorderLayout.SOUTH);

        // Układanie formPanel i infoPanel obok siebie z równą szerokością
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        contentPanel.setBackground(paleCyan);

        // Wrapper dla formPanel
        JPanel formPanelWrapper = new JPanel(new BorderLayout());
        formPanelWrapper.setBackground(paleCyan);
        formPanelWrapper.add(formPanel, BorderLayout.NORTH);
        formPanelWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        contentPanel.add(formPanelWrapper);

        // Wrapper dla infoPanel
        JPanel infoPanelWrapper = new JPanel(new BorderLayout());
        infoPanelWrapper.setBackground(paleCyan);
        infoPanelWrapper.add(infoPanel, BorderLayout.SOUTH);
        infoPanelWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 90, 0));
        contentPanel.add(infoPanelWrapper);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Panel przycisku potwierdzenia
        JPanel confirmPanel = new JPanel();
        confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.Y_AXIS));
        confirmPanel.setBackground(paleCyan);

        JButton confirmButton = new JButton("Potwierdź zakup");
        confirmButton.addActionListener(e ->{
            Component[] components = mainFrame.getCardsPanel().getComponents();
            for (Component c : components) {
                if ("POTWIERDZENIE_ZAKUPU_BILETU".equals(c.getName())) {
                    mainFrame.getCardsPanel().remove(c);
                    break;
                }
            }
            if(postojS.getFaktycznyCzasOdjazdu()!=null)
                JOptionPane.showMessageDialog(this, "Czas rezerwacji miejsc na przejazd z "+stacjaStart.getNazwa()+" w ramach połączenia "+wybranePolaczenie.getOznaczeniePolaczenia()+" minął.");
            else {
                try {
                    if (!rezerwowaneMiejsce.isSelected()) {
                        int iloscZarezerwowanychBiletowBezRezerwacji=0, pojemnoscCalegoSkladu=0;
                        for(BiletBezposredni bb: wybranePolaczenie.getBiletBezposrednie())
                            if(bb.getNrMiejsca()==null && bb.getNrWagonu()==null)
                                iloscZarezerwowanychBiletowBezRezerwacji++;
                        for(Wagon wagon:kursujacy.getWagony())
                            pojemnoscCalegoSkladu+=wagon.getPojemnosc()-wagon.getMiejsca().size();
                        // konsekwencja slabego designu metody getPojemnosc()
                        if(iloscZarezerwowanychBiletowBezRezerwacji+1>pojemnoscCalegoSkladu)
                        {
                            JOptionPane.showMessageDialog(this, "Nie można kupić biletu bez rezerwacji miejsca - zbyt duże obciążenie wybranego połączenia");
                        } else {
                            BiletBezposredni kupionyBilet = BiletBezposredni.builder()
                                    .cena(cena)
                                    .stacjaOdjazd(stacjaStart)
                                    .stacjaPrzyjazd(stacjaEnd)
                                    .polaczenie(wybranePolaczenie)
                                    .kupujacy(mainFrame.getZalogowanyUser())
                                    .build();
                            mainFrame.getBiletRepository().save(kupionyBilet);
                            mainFrame.getBiletBezposredniRepository().save(kupionyBilet);
                            PotwierdzenieZakupuBiletuPanel potwierdzenieZakupuBiletuPanel = new PotwierdzenieZakupuBiletuPanel(mainFrame, kupionyBilet);
                            mainFrame.getCardsPanel().add(potwierdzenieZakupuBiletuPanel, "POTWIERDZENIE_ZAKUPU_BILETU");
                            mainFrame.getCardLayout().show(mainFrame.getCardsPanel(), "POTWIERDZENIE_ZAKUPU_BILETU");
                        }
                    } else {
                        if (Objects.equals(miejsceComboBox.getSelectedItem(), "")) {
                            JOptionPane.showMessageDialog(this, "Wybierz miejsce do zarezerwowania");
                        } else {
                            BiletBezposredni kupionyBilet = BiletBezposredni.builder()
                                    .cena(cena)
                                    .stacjaOdjazd(stacjaStart)
                                    .stacjaPrzyjazd(stacjaEnd)
                                    .nrWagonu((Integer) wagonComboBox.getSelectedItem())
                                    .nrMiejsca(Integer.parseInt((String) miejsceComboBox.getSelectedItem()))
                                    .polaczenie(wybranePolaczenie)
                                    .kupujacy(mainFrame.getZalogowanyUser())
                                    .build();
                            mainFrame.getBiletRepository().save(kupionyBilet);
                            mainFrame.getBiletBezposredniRepository().save(kupionyBilet);


                            PotwierdzenieZakupuBiletuPanel potwierdzenieZakupuBiletuPanel = new PotwierdzenieZakupuBiletuPanel(mainFrame, kupionyBilet);
                            mainFrame.getCardsPanel().add(potwierdzenieZakupuBiletuPanel, "POTWIERDZENIE_ZAKUPU_BILETU");
                            mainFrame.getCardLayout().show(mainFrame.getCardsPanel(), "POTWIERDZENIE_ZAKUPU_BILETU");
                        }
                    }

                } catch (Exception exc) {
                    String msg=String.valueOf(exc);
                    if(String.valueOf(exc).contains("Naruszenie ograniczenia Klucza Głównego lub Indeksu Unikalnego: " +
                            "\"PUBLIC.CONSTRAINT_INDEX_8 ON PUBLIC.BILET_BEZPOSREDNI(" +
                            "NR_WAGONU NULLS FIRST, NR_MIEJSCA NULLS FIRST, POLACZENIE_ID NULLS FIRST)"))
                        msg="Wybrane miejsce jest zajęte, spróbuj wybrac inne";
                    JOptionPane.showMessageDialog(this, msg);
                }
            }
        });

        confirmPanel.add(confirmButton);
        int buttonHeight = confirmButton.getPreferredSize().height;
        confirmPanel.add(Box.createVerticalStrut((int)(buttonHeight * 1.5)));
        JPanel confirmWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        confirmWrapper.setBackground(paleCyan);
        confirmWrapper.add(confirmPanel);

        mainPanel.add(confirmWrapper, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        defineMiejsceCombo();

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

    private void defineMiejsceCombo(){
        ArrayList<Integer> miejscaNumery = new ArrayList<>();
        ArrayList<Wagon> wagony = new ArrayList<>(wybranePolaczenie.getPociagKursujacy().getWagony());
        Integer selectedWagonNumber = (Integer) wagonComboBox.getSelectedItem();
        if (selectedWagonNumber == null) {
            selectedWagonNumber=1;
        }
        // Ustalenie które miejsca są zajęte w wybranym wagonie na podstawie istniejących biletów
        ArrayList<Integer> zajeteWTymWagonie = new ArrayList<>();
        for(BiletBezposredni bb: wybranePolaczenie.getBiletBezposrednie()){
            if(Objects.equals(bb.getNrWagonu(), selectedWagonNumber))
                zajeteWTymWagonie.add(bb.getNrMiejsca());
        }

        // Ustalenie które numery miejsc w wybranym wagonie są wolne oraz spełniają wymagania filtrowania udogodnień
        try {
            ArrayList<Miejsce> miejsca = new ArrayList<>(wagony.get(selectedWagonNumber - 1).getMiejsca());
            for (int i = 1; i <= miejsca.size()-1; i++)
                if(!zajeteWTymWagonie.contains(i)) {
                    ArrayList<TypMiejsca> udogodnienia = miejsca.get(i).getTypy();
                    if(!miejsceInwCheckBox.isSelected() || udogodnienia.contains(TypMiejsca.INWALIDA))
                        if(!miejsceRowCheckBox.isSelected() || udogodnienia.contains(TypMiejsca.ROWEROWE))
                            if(!miejsceStoCheckBox.isSelected() || udogodnienia.contains(TypMiejsca.STOLIK))
                                miejscaNumery.add(i);
                }
        }catch (Exception ex){
            JOptionPane.showMessageDialog(this, ex);
        }
        // Wstawienie dostępnych numerów miejsc do ComboBoxa
        miejsceComboBox.removeAllItems();
        if(miejscaNumery.isEmpty())
            miejsceComboBox.addItem("");
        for (Integer miejsce : miejscaNumery) {
            miejsceComboBox.addItem(String.valueOf(miejsce));
        }
        miejsceComboBox.setSelectedIndex(0);
        miejsceComboBox.revalidate();
        miejsceComboBox.repaint();
    }
}