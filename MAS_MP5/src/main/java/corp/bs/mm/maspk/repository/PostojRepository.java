package corp.bs.mm.maspk.repository;

import corp.bs.mm.maspk.model.Polaczenie;
import corp.bs.mm.maspk.model.Postoj;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostojRepository extends CrudRepository<Postoj, Long> {
    @Query("""
        SELECT COUNT(p) 
        FROM Postoj p 
        WHERE p.polaczenie.polaczenieId = :polaczenieId 
          AND p.planowanyCzasPrzyjazdu IS NULL
          AND (:excludeId IS NULL OR p.postojId <> :excludeId)
        """)
    long czyJestJednaStacjaPoczatkowa(
            @Param("polaczenieId") Long polaczenieId,
            @Param("excludeId") Long excludeId
    );

    List<Postoj> findByPolaczenie(Polaczenie polaczenie);
}
