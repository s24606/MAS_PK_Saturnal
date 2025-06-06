package corp.bs.mm.masmp5.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private JPanel cardsPanel;
    private CardLayout cardLayout;

    public MainFrame(List<String> stacje) {
        setTitle("Saturnal");
        setSize(700, 450);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        JPanel homePanel = new JPanel();
        homePanel.setBackground(new Color(224, 255, 255));
        homePanel.add(new JLabel("Witaj na stronie głównej!"));

        JPanel searchPanel = new WyszukiwarkaPolaczenPanel(stacje);

        cardsPanel.add(homePanel, "HOME");
        cardsPanel.add(searchPanel, "SEARCH");

        // Tworzymy pasek nawigacji - ten sam dla wszystkich widoków
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(224, 255, 255));
        navBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JButton btnHome = new JButton("Strona główna");
        btnHome.addActionListener(e -> cardLayout.show(cardsPanel, "HOME"));

        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtons.setBackground(new Color(224, 255, 255));
        JButton btnKonto = new JButton("konto");
        JButton btnWyloguj = new JButton("Wyloguj się");
        rightButtons.add(btnKonto);
        rightButtons.add(btnWyloguj);

        navBar.add(btnHome, BorderLayout.WEST);
        navBar.add(rightButtons, BorderLayout.EAST);

        // Układ JFrame: navbar na górze, pod spodem cardsPanel
        setLayout(new BorderLayout());
        add(navBar, BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);

        JButton btnGoSearch = new JButton("Wyszukiwarka");
        btnGoSearch.addActionListener(e -> cardLayout.show(cardsPanel, "SEARCH"));
        homePanel.add(btnGoSearch);

        setVisible(true);
    }
}