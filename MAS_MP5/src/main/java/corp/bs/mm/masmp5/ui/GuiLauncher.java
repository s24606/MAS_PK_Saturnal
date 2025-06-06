package corp.bs.mm.masmp5.ui;

import corp.bs.mm.masmp5.model.Polaczenie;
import corp.bs.mm.masmp5.model.Stacja;
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

    public GuiLauncher(StacjaRepository stacjaRepo, PolaczenieRepository polaczenieRepo) {
        this.stacjaRepo = stacjaRepo;
        this.polaczenieRepo = polaczenieRepo;
    }

    @Override
    public void run(String... args) {
        List<String> stacje = StreamSupport.stream(stacjaRepo.findAll().spliterator(), false)
                .map(Stacja::getNazwa)
                .toList();

        SwingUtilities.invokeLater(() -> new MainFrame(stacje));
    }
}

