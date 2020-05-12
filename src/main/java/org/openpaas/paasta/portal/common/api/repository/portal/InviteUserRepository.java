package org.openpaas.paasta.portal.common.api.repository.portal;

import org.openpaas.paasta.portal.common.api.entity.portal.InviteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InviteUserRepository extends JpaRepository<InviteUser, Integer> {
    InviteUser findById(Integer id);

    List<InviteUser> findByUserIdAndOrgGuid(String userId, String orgId);

    List<InviteUser> findByTokenAndGubunNot(String token, String gubun);

    List<InviteUser> findByInvitenameAndGubun (String invitename, String gubun);

    List<InviteUser> findByUserIdAndGubun(String userId, String gubun);

    InviteUser findFirstByUserIdAndOrgGuid(String userId, String orgId);
}
