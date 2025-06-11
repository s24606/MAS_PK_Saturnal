package corp.bs.mm.maspk.ui;

import corp.bs.mm.maspk.model.Osoba;

import javax.swing.*;
import java.awt.*;

public class KontoPanel extends JPanel {

    public KontoPanel(Osoba osoba) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(255, 255, 155));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setName("KONTO");

        JLabel header = new JLabel("Konto - szczegóły");
        header.setForeground(new Color(77, 38, 0));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 32f));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(header);

        add(Box.createRigidArea(new Dimension(0, 20))); // odstęp

        add(createCenteredLabel("Imię: " + osoba.getImie()));
        add(createCenteredLabel("Nazwisko: " + osoba.getNazwisko()));
        add(createCenteredLabel("Email: " + osoba.getEmail()));
        add(createCenteredLabel("Telefon: " + osoba.getTelefon()));
        add(createCenteredLabel("Role: " + osoba.getRole()));

        if (osoba.getRole().stream().anyMatch(r -> r.name().equals("PASAZER"))) {
            add(createCenteredLabel("Ulga: " + osoba.getUlga()));
        }

        if (osoba.getRole().stream().anyMatch(r -> r.name().equals("PRACOWNIK"))) {
            add(createCenteredLabel("Kod służbowy: " + osoba.getKodSluzbowy()));
        }
    }

    // Metoda pomocnicza tworząca wyśrodkowany JLabel z odstepem pod spodem
    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return label;
    }
}
