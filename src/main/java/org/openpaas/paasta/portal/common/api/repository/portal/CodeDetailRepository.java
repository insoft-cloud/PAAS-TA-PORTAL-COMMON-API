package org.openpaas.paasta.portal.common.api.repository.portal;

import lombok.ToString;
import org.openpaas.paasta.portal.common.api.entity.portal.CodeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by SEJI on 2018-02-20.
 */
@Repository
public interface CodeDetailRepository extends JpaRepository<CodeDetail, Integer> {
    int countByGroupId(String groupId);

    List<CodeDetail> findAllByNo(int no);


    List<CodeDetail> findByGroupId(String groudId);


    List<CodeDetail> findAllByGroupId(String groudid);

    @Query("SELECT c FROM CodeDetail  AS c where c.groupId=:groudid and c.useYn='Y' ")
    List<CodeDetail> finadAllbyGroupId(@Param("groudid") String groudid);

    @Query("SELECT a.value FROM CodeDetail AS a where a.groupId=:groudid and a.key = 'max_size' ")
    int findByMaxValue(@Param("groudid") String groudid);

}
