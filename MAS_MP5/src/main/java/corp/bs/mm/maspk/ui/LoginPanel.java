package corp.bs.mm.maspk.ui;

import corp.bs.mm.maspk.model.Osoba;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginPanel extends JPanel {
    private final JLabel userLabelDisplay;
    private Osoba selectedOsoba = null;
    private final MainFrame mainFrame;

    public LoginPanel(List<Osoba> pasazerowie, List<Osoba> pracownicy, List<Osoba> obaTypy, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setName("LOGIN");
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(255, 255, 155));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel header = new JLabel("Logowanie");
        header.setForeground(new Color(77, 38, 0));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 32f));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(header);

        add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel userRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userRow.setOpaque(false);

        JLabel userStaticLabel = new JLabel("Zaloguj jako:");
        userLabelDisplay = new JLabel("Brak wybranego użytkownika");
        userLabelDisplay.setFont(new Font("SansSerif", Font.BOLD, 14));
        userLabelDisplay.setForeground(new Color(77, 38, 0));

        userRow.add(userStaticLabel);
        userRow.add(userLabelDisplay);
        add(userRow);
        add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.Y_AXIS));
        comboPanel.setOpaque(false);
        comboPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        comboPanel.add(createRoleComboRow("Pasażerowie", "Pasażer", pasazerowie));
        comboPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        comboPanel.add(createRoleComboRow("Pracownicy", "Pracownik", pracownicy));
        comboPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        comboPanel.add(createRoleComboRow("Obie role", "P+P", obaTypy));

        add(comboPanel);
        add(Box.createVerticalStrut(30));

        JButton loginButton = new JButton("Zaloguj");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> {
            if (selectedOsoba != null) {
                mainFrame.setZalogowanyUser(selectedOsoba);
                mainFrame.showHome();
            } else {
                JOptionPane.showMessageDialog(this, "Wybierz użytkownika przed logowaniem.");
            }
        });
        add(loginButton);
    }

    private JPanel createRoleComboRow(String labelText, String prefix, List<Osoba> osoby) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        row.setOpaque(false);

        JLabel label = new JLabel(labelText);

        osoby.sort((o1, o2) -> (o1.getImie() + " " + o1.getNazwisko())
                .compareToIgnoreCase(o2.getImie() + " " + o2.getNazwisko()));

        JComboBox<Osoba> combo = new JComboBox<>(osoby.toArray(new Osoba[0]));
        combo.setPreferredSize(new Dimension(200, 25));

        combo.addActionListener(e -> {
            Osoba selected = (Osoba) combo.getSelectedItem();
            if (selected != null) {
                userLabelDisplay.setText(prefix + " - " + selected.getImie() + " " + selected.getNazwisko());
                selectedOsoba = selected;
            }
        });

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Osoba o) {
                    setText(o.getImie() + " " + o.getNazwisko());
                }
                return c;
            }
        });

        row.add(label);
        row.add(combo);
        return row;
    }
}
