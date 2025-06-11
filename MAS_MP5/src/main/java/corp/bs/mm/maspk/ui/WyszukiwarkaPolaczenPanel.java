package corp.bs.mm.maspk.ui;

import corp.bs.mm.maspk.repository.StacjaRepository;
import corp.bs.mm.maspk.model.Stacja;


import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WyszukiwarkaPolaczenPanel extends JPanel {
    private MainFrame mainFrame;

    public WyszukiwarkaPolaczenPanel(StacjaRepository stacjaRepo, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        mainFrame.setAtrybutyWyszukiwania(null, null,null,null);
        Color paleCyan = new Color(155, 255, 255);
        setBackground(paleCyan);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Nagłówek
        JLabel header = new JLabel("WYSZUKIWARKA POŁĄCZEŃ");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 32f));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(header);

        add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(paleCyan);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Stacja początkowa
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Stacja początkowa"), gbc);

        gbc.gridx = 1;
        List<Stacja> listaStacji = (List<Stacja>) stacjaRepo.findAll();
        List<String> nazwyStacji = new ArrayList<>();
        nazwyStacji.add("");
        for (Stacja stacja : listaStacji) {
            nazwyStacji.add(stacja.getNazwa());
        }
        JComboBox<String> comboStart = new JComboBox<>(nazwyStacji.toArray(new String[0]));
        comboStart.setPreferredSize(new Dimension(200, 25));
        formPanel.add(comboStart, gbc);

        // Stacja końcowa
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Stacja Końcowa"), gbc);

        gbc.gridx = 1;
        JComboBox<String> comboEnd = new JComboBox<>(nazwyStacji.toArray(new String[0]));
        comboEnd.setPreferredSize(new Dimension(200, 25));
        formPanel.add(comboEnd, gbc);

        // Wyszukaj po czasie
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel timeLabel = new JLabel("Wyszukaj po czasie...");
        formPanel.add(timeLabel, gbc);
        LocalDateTime termin = LocalDateTime.now();

        gbc.fill = GridBagConstraints.HORIZONTAL;


        // Radio buttony
        gbc.gridy = 3;
        JPanel radioButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        radioButtons.setBackground(paleCyan);
        JRadioButton rbOdjazdu = new JRadioButton("Odjazdu");
        JRadioButton rbPrzyjazdu = new JRadioButton("Przyjazdu");
        rbOdjazdu.setBackground(paleCyan);
        rbPrzyjazdu.setBackground(paleCyan);

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(rbOdjazdu);
        radioGroup.add(rbPrzyjazdu);

        rbOdjazdu.setSelected(true);
        radioButtons.add(rbOdjazdu);
        radioButtons.add(rbPrzyjazdu);
        formPanel.add(radioButtons, gbc);


        // comboBoxy - dni
        String[] dni = new String[31];
        for (int i = 1; i <= 31; i++) {
            dni[i-1] = String.valueOf(i);
        }
        JComboBox<String> dayCombo = new JComboBox<>(dni);
        dayCombo.setPreferredSize(new Dimension(30, 25));

        // comboBoxy - miesiace
        String[] miesiace = new String[12];
        for (int i = 1; i <= 12; i++) {
            miesiace[i-1] = String.valueOf(i);
        }
        JComboBox<String> monthCombo = new JComboBox<>(miesiace);
        monthCombo.setPreferredSize(new Dimension(30, 25));

        // comboBoxy - lata
        String[] lata = new String[2];
        lata[0] = String.valueOf(LocalDateTime.now().getYear());
        lata[1] = String.valueOf(LocalDateTime.now().getYear()+1);
        JComboBox<String> yearCombo = new JComboBox<>(lata);
        yearCombo.setPreferredSize(new Dimension(60, 25));

        LocalDateTime now = LocalDateTime.now();
        dayCombo.setSelectedItem(String.valueOf(now.getDayOfMonth()));
        monthCombo.setSelectedItem(String.valueOf(now.getMonthValue()));
        yearCombo.setSelectedItem(String.valueOf(now.getYear()));

        // spinner - godzina
        SpinnerDateModel timeModel = new SpinnerDateModel();
        JSpinner timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date());

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        JPanel dateTimePanel = new JPanel(new GridLayout(1, 4, 10, 0));
        dateTimePanel.add(dayCombo);
        dateTimePanel.add(monthCombo);
        dateTimePanel.add(yearCombo);
        dateTimePanel.add(timeSpinner);
        dateTimePanel.setBackground(paleCyan);
        formPanel.add(dateTimePanel, gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        add(formPanel);

        add(Box.createRigidArea(new Dimension(0, 15)));

        // Przyciski
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setBackground(paleCyan);
        JButton btnBezposrednie = new JButton("Wyszukaj połączenia bezpośrednie");
        JButton btnPrzesiadkowe = new JButton("Wyszukaj połączenia przesiadkowe");

        btnBezposrednie.addActionListener(e -> {
            if (comboStart.getSelectedIndex() == 0 || comboEnd.getSelectedIndex() == 0 || comboStart.getSelectedIndex()==comboEnd.getSelectedIndex())
            {
                JOptionPane.showMessageDialog(this, "Wybierz różne stacje początkową i końcową.");
            }
            else{
                Component[] components = mainFrame.getCardsPanel().getComponents();
                for (Component c : components) {
                    if ("WYNIKI_BEZPOSREDNIE".equals(c.getName())) {
                        mainFrame.getCardsPanel().remove(c);
                        break;
                    }
                }

                int rok = Integer.parseInt((String) yearCombo.getSelectedItem());
                LocalTime localTime = ((Date) timeSpinner.getValue()).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalTime();
                LocalDateTime terminResult= termin.
                        withYear(rok).
                        withMonth(monthCombo.getSelectedIndex()+1).
                        withDayOfMonth(dayCombo.getSelectedIndex()+1).
                        withHour(localTime.getHour()).
                        withMinute(localTime.getMinute()).
                        withSecond(0).
                        withNano(0);
                mainFrame.setAtrybutyWyszukiwania(
                        listaStacji.get(comboStart.getSelectedIndex() - 1),
                        listaStacji.get(comboEnd.getSelectedIndex() - 1),
                        (rbOdjazdu.isSelected() ? terminResult : null),
                        (rbPrzyjazdu.isSelected() ? terminResult : null)
                );
                WynikiWyszukiwaniaBezposredniegoPanel wynikiPanel = new WynikiWyszukiwaniaBezposredniegoPanel(mainFrame);
                wynikiPanel.setName("WYNIKI_BEZPOSREDNIE");
                mainFrame.getCardsPanel().add(wynikiPanel, "WYNIKI_BEZPOSREDNIE");
                mainFrame.getCardLayout().show(mainFrame.getCardsPanel(), "WYNIKI_BEZPOSREDNIE");
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
    }
}
