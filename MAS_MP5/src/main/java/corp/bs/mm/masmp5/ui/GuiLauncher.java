package corp.bs.mm.masmp5.ui;

import corp.bs.mm.masmp5.model.Osoba;
import corp.bs.mm.masmp5.model.Polaczenie;
import corp.bs.mm.masmp5.model.Stacja;
import corp.bs.mm.masmp5.repository.OsobaRepository;
import corp.bs.mm.masmp5.repository.PolaczenieRepository;
import corp.bs.mm.masmp5.repository.StacjaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class GuiLauncher implements CommandLineRunner {

    private final StacjaRepository stacjaRepo;
    private final PolaczenieRepository polaczenieRepo;
    private final OsobaRepository osobaRepo;


    public GuiLauncher(StacjaRepository stacjaRepo, PolaczenieRepository polaczenieRepo,
                       OsobaRepository osobaRepo) {
        this.stacjaRepo = stacjaRepo;
        this.polaczenieRepo = polaczenieRepo;
        this.osobaRepo = osobaRepo;
    }

    @Override
    public void run(String... args) {
        SwingUtilities.invokeLater(() ->
                new MainFrame(stacjaRepo, osobaRepo, polaczenieRepo)
        );
    }
}

