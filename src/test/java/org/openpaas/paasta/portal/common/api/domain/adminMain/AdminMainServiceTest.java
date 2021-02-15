package org.openpaas.paasta.portal.common.api.domain.adminMain;

import org.apache.commons.collections4.map.HashedMap;
import org.jinq.jpa.JinqJPAStreamProvider;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.common.api.entity.cc.OrganizationsCc;
import org.openpaas.paasta.portal.common.api.entity.cc.SpacesCc;
import org.openpaas.paasta.portal.common.api.entity.cc.SpacesToCc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by indra on 2018-06-27.
 */
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminMainServiceTest {


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    AdminMainService adminMainService;

    OrganizationsCc organizationsCc;

    SpacesCc spacesCc;

    SpacesToCc spacesToCc;

    List<OrganizationsCc> organizationsCcList;

    List<SpacesCc> spacesCcList;

    Map resultMap;
    Map getTotalSpaceListResultMap;

    private static EntityManagerFactory entityManagerFactory;
    private static JinqJPAStreamProvider streams;
    private static EntityManager ccEm;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setTestData();

    }

    private void setTestData() {
        Date d = new Date();

        resultMap = new HashedMap();

        //testGetTotalCountList
        organizationsCcList = new ArrayList<OrganizationsCc>();
        organizationsCc = new OrganizationsCc();
        organizationsCc.setId(1);
        organizationsCc.setGuid("4fa133ee-d49f-4a8f-b56c-c691f8bd8674");
        organizationsCc.setCreatedAt(d);
        organizationsCc.setUpdatedAt(d);
        organizationsCc.setName("test-org");
        organizationsCc.setBillingEnabled(false);
        organizationsCc.setQuotaDefinitionId("1");
        organizationsCc.setStatus("active");
        organizationsCc.setDefaultIsolationSegmentGuid(null);
        organizationsCcList.add(organizationsCc);
        resultMap.put("list", organizationsCcList);

        //testGetTotalSpaceList
        getTotalSpaceListResultMap = new HashedMap();
        spacesCcList = new ArrayList<SpacesCc>();
        spacesCc = new SpacesCc();
        spacesCc.setApplicationCount(1);
        spacesCc.setId(1);
        spacesCc.setName("space");
        spacesCcList.add(spacesCc);
        getTotalSpaceListResultMap.put("list", spacesCcList);


        spacesToCc = new SpacesToCc();
        spacesToCc.setCreatedAt(new Date());
        spacesToCc.setAllowSsh(true);
        spacesToCc.setGuid("guid");
        spacesToCc.setId(1);
        spacesToCc.setIsolationSegmentGuid("isolationSegmentGuid");
        spacesToCc.setOrganizationId(1);
        spacesToCc.setSpaceId(1);
        spacesToCc.setName("name");
        spacesToCc.setUpdatedAt(new Date());
        spacesToCc.setUserId(1);


    }

    @Test
    public void testGetParameter(){

        organizationsCc.getId();
        organizationsCc.getGuid();
        organizationsCc.getCreatedAt();
        organizationsCc.getUpdatedAt();
        organizationsCc.getName();
        organizationsCc.getBillingEnabled();
        organizationsCc.getQuotaDefinitionId();
        organizationsCc.getStatus();
        organizationsCc.getDefaultIsolationSegmentGuid();
        organizationsCc.toString();

        spacesCc.getApplicationCount();
        spacesCc.getId();
        spacesCc.getName();
        spacesCc.toString();


        spacesToCc.getCreatedAt();
        spacesToCc.getAllowSsh();
        spacesToCc.getGuid();
        spacesToCc.getId();
        spacesToCc.getIsolationSegmentGuid();
        spacesToCc.getOrganizationId();
        spacesToCc.getSpaceId();
        spacesToCc.getName();
        spacesToCc.getUpdatedAt();
        spacesToCc.getUserId();
        spacesToCc.toString();

    }

    @Test
    public void testGetTotalCountList() throws Exception {
        thrown.expect(NullPointerException.class);

        Map<String, Object> result = adminMainService.getTotalCountList();
        Assert.assertEquals(resultMap, result);
    }

    @Test
    public void testGetTotalOrganizationList() throws Exception {
        thrown.expect(NullPointerException.class);

        Map<String, Object> result = adminMainService.getTotalOrganizationList();
        Assert.assertEquals(resultMap, result);
    }

    @Test
    public void testGetTotalSpaceList() throws Exception {
        thrown.expect(NullPointerException.class);

        Map<String, Object> result = adminMainService.getTotalSpaceList("1");
        Assert.assertEquals(getTotalSpaceListResultMap, result);
    }
}
