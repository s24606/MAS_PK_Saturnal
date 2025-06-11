package corp.bs.mm.maspk.repository;

import corp.bs.mm.maspk.model.Stacja;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StacjaRepository extends CrudRepository<Stacja, Long> {
    List<Stacja> findByOrderByNazwaAsc();
}
