package org.openpaas.paasta.portal.common.api.repository.cc;

import org.openpaas.paasta.portal.common.api.entity.cc.OrganizationsTolCc;
import org.openpaas.paasta.portal.common.api.entity.portal.ServicepackCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by indra on 2018-02-06.
 */
@Repository
public interface OrgCcRepository extends JpaRepository<OrganizationsTolCc, Integer> {
    static String findByGuid(String guid) { return guid; }

    @Query("SELECT org FROM OrganizationsTolCc AS org where org.guid=:guid")
    OrganizationsTolCc getOrg(@Param("guid") String guid);
}
