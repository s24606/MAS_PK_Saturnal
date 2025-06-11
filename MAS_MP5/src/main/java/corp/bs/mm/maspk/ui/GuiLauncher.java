package corp.bs.mm.maspk.ui;

import corp.bs.mm.maspk.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class GuiLauncher implements CommandLineRunner {

    private final StacjaRepository stacjaRepo;
    private final PolaczenieRepository polaczenieRepo;
    private final OsobaRepository osobaRepo;
    private final BiletRepository biletRepository;
    private final BiletBezposredniRepository biletBezposredniRepository;


    public GuiLauncher(StacjaRepository stacjaRepo, PolaczenieRepository polaczenieRepo,
                       OsobaRepository osobaRepo,
                       BiletRepository biletRepository,
                       BiletBezposredniRepository biletBezposredniRepository) {
        this.stacjaRepo = stacjaRepo;
        this.polaczenieRepo = polaczenieRepo;
        this.osobaRepo = osobaRepo;
        this.biletRepository = biletRepository;
        this.biletBezposredniRepository = biletBezposredniRepository;
    }

    @Override
    public void run(String... args) {
        SwingUtilities.invokeLater(() ->
                new MainFrame(stacjaRepo, osobaRepo, polaczenieRepo, biletRepository, biletBezposredniRepository)
        );
    }
}

