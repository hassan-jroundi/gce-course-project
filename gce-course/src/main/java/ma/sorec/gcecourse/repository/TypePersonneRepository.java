package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.RelanceFacturation;
import ma.sorec.gcecourse.data.TypePersonne;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypePersonneRepository extends JpaRepository<TypePersonne, Long> {
}
