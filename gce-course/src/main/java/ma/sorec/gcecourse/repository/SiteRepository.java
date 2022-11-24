package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long> {

    Site findFirstByNomIgnoreCase(String nom);
}
