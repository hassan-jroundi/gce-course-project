package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Piste;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PisteRepository extends JpaRepository<Piste, Long> {

    List<Piste> findAllBySiteNom(String nomSite);
}
