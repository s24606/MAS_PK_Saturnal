package corp.bs.mm.masmp5.ui;

import corp.bs.mm.masmp5.repository.StacjaRepository;
import corp.bs.mm.masmp5.model.Stacja;


import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class WyszukiwarkaPolaczenPanel extends JPanel {

    public WyszukiwarkaPolaczenPanel(StacjaRepository stacjaRepo) {
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
        List<String> nazwyStacji = stacjaRepo.findByOrderByNazwaAsc()
                .stream()
                .map(Stacja::getNazwa)
                .toList();
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

        // Wyszukaj po czasie...
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel timeLabel = new JLabel("Wyszukaj po czasie...");
        formPanel.add(timeLabel, gbc);

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


        String[] dni = new String[31];
        for (int i = 1; i <= 31; i++) {
            dni[i-1] = String.valueOf(i);
        }
        JComboBox<String> dayCombo = new JComboBox<>(dni);
        dayCombo.setPreferredSize(new Dimension(30, 25));

        String[] miesiace = new String[12];
        for (int i = 1; i <= 12; i++) {
            miesiace[i-1] = String.valueOf(i);
        }
        JComboBox<String> monthCombo = new JComboBox<>(miesiace);
        monthCombo.setPreferredSize(new Dimension(30, 25));

        String[] lata = new String[2];
        lata[0] = String.valueOf(LocalDateTime.now().getYear());
        lata[1] = String.valueOf(LocalDateTime.now().getYear()+1);
        JComboBox<String> yearCombo = new JComboBox<>(lata);
        yearCombo.setPreferredSize(new Dimension(60, 25));

        LocalDateTime now = LocalDateTime.now();
        dayCombo.setSelectedItem(String.valueOf(now.getDayOfMonth()));
        monthCombo.setSelectedItem(String.valueOf(now.getMonthValue()));
        yearCombo.setSelectedItem(String.valueOf(now.getYear()));

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

        // resetujemy gbc jeśli dalej coś dodajesz
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        add(formPanel);

        add(Box.createRigidArea(new Dimension(0, 15)));

        // Przyciski
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setBackground(paleCyan);
        JButton btnDirect = new JButton("Wyszukaj połączenia bezpośrednie");
        JButton btnTransfer = new JButton("Wyszukaj połączenia przesiadkowe");
        buttonsPanel.add(btnDirect);
        buttonsPanel.add(btnTransfer);

        add(buttonsPanel);
    }
}
