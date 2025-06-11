package corp.bs.mm.maspk.ui;

import corp.bs.mm.maspk.model.*;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class PotwierdzenieZakupuBiletuPanel extends JPanel {

    public PotwierdzenieZakupuBiletuPanel(MainFrame mainFrame, BiletBezposredni bilet) {
        setLayout(new BorderLayout());
        Color paleCyan = new Color(155, 255, 255);
        setBackground(paleCyan);

        // Nagłówek
        JLabel headerLabel = new JLabel("Bilet został kupiony", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        headerLabel.setForeground(Color.BLUE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Panel z informacjami
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(paleCyan);
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(paleCyan);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Polaczenie polaczenie = bilet.getPolaczenie();
        Postoj postojS = mainFrame.getWyszukanePostojeS().get(polaczenie.getPolaczenieId());
        Stacja stacjaStart = bilet.getStacjaOdjazd();

        int row = 0;

        addLabelValuePair(infoPanel, gbc, row++, "Oznaczenie połączenia:",
                polaczenie.getOznaczeniePolaczenia());
        addLabelValuePair(infoPanel, gbc, row++, "Data odjazdu:",
                postojS.getPlanowanyCzasOdjazdu().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")));
        addLabelValuePair(infoPanel, gbc, row++, "Stacja początkowa:",
                stacjaStart.getNazwa());
        addLabelValuePair(infoPanel, gbc, row++, "Peron:",
                String.valueOf(postojS.getNrPeronu()));

        if(bilet.getNrMiejsca() != null) {
            addLabelValuePair(infoPanel, gbc, row++, "Nr wagonu:",
                    String.valueOf(bilet.getNrWagonu()));
            addLabelValuePair(infoPanel, gbc, row++, "Nr miejsca:",
                    String.valueOf(bilet.getNrMiejsca()));
        }

        addLabelValuePair(infoPanel, gbc, row++, "Cena biletu:",
                String.format("%.2f zł", bilet.getCena()));

        // Dodanie panelu z informacjami do panelu centrującego
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.gridx = 0;
        centerGbc.gridy = 0;
        centerGbc.anchor = GridBagConstraints.NORTH;
        centerGbc.weighty = 1.0;
        centerGbc.insets = new Insets(10, 0, 0, 0);
        centerPanel.add(infoPanel, centerGbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void addLabelValuePair(JPanel panel, GridBagConstraints gbc, int row,
                                   String labelText, String valueText) {

        Font labelFont = new Font("Arial", Font.BOLD, 12);
        Font valueFont = new Font("Arial", Font.PLAIN, 12);

        // Nazwy informacji o bilecie w lewej kolumnie
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5;
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        panel.add(label, gbc);

        // Informacje o bilecie w prawej kolumnie
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.5;
        JLabel value = new JLabel(valueText);
        value.setFont(valueFont);
        panel.add(value, gbc);
    }
}