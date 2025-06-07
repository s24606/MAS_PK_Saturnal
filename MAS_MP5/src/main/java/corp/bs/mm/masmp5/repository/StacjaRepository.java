package corp.bs.mm.masmp5.repository;

import corp.bs.mm.masmp5.model.Stacja;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StacjaRepository extends CrudRepository<Stacja, Long> {
    List<Stacja> findByOrderByNazwaAsc();
}
