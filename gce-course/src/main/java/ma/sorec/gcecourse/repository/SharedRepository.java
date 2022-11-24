package ma.sorec.gcecourse.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedRepository {

    @Query(value = "SELECT SEQ_GCE_ID.nextval FROM dual", nativeQuery = true)
    Long getNextSeqNum();

}
