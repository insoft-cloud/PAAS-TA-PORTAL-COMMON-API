package org.openpaas.paasta.portal.common.api.domain.catalog;


import org.openpaas.paasta.portal.common.api.config.Constants;
import org.openpaas.paasta.portal.common.api.config.JinqSource;
import org.openpaas.paasta.portal.common.api.entity.cc.CatalogCc;
import org.openpaas.paasta.portal.common.api.entity.portal.*;
import org.openpaas.paasta.portal.common.api.repository.cc.CatalogCcRepository;
import org.openpaas.paasta.portal.common.api.repository.portal.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by SEJI on 2018-03-06.
 */
@Service
public class CatalogService {

    private final Logger logger = getLogger(this.getClass());

    @Autowired
    StarterCategoryRepository starterCategoryRepository;

    @Autowired
    BuildpackCategoryRepository buildpackCategoryRepository;

    @Autowired
    ServicepackCategoryRepository servicepackCategoryRepository;

    @Autowired
    StarterServicepackRelationRepository starterServicePackRelationRepository;

    @Autowired
    StarterBuildPackRelationRepository starterBuildPackRelationRepository;

    @Autowired
    CatalogHistoryRepository catalogHistoryRepository;

    @Autowired
    public JinqSource jinqSource;

    @Autowired
    CatalogCcRepository catalogCcRepository;

    /**
     * 앱 템플릿 카탈로그 조회
     *
     * @param no Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getStarterCatalog(int no) {
        logger.info("" + no);
        StarterCategory starterCategory = starterCategoryRepository.findByNo(no);
        try {
            //기존 스타터 서비스 릴레이션 가져오기
            List<Integer> ssrIntList = new ArrayList<>();
            List<StarterServicepackRelation> ssrList = starterServicePackRelationRepository.findByStarterCatalogNo(no);
            for (StarterServicepackRelation ssr : ssrList) {
                ssrIntList.add(ssr.getServicepackCategoryNo());
            }
            starterCategory.setServicePackCategoryNoList(ssrIntList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            /*
             * 기존 스타터 빌드  릴레이션 가져오기
             * -> List로 왜 뽑냐면...Buildpack relation 하나만 들어간다는 전제가 걸려야함
             * -> JPA상에서 에러를 배출...Unique 값이 아닌데 하나만 나오게 했다고 그래서 List로 뽑음
             */
            List<StarterBuildpackRelation> sbr = starterBuildPackRelationRepository.findByStarterCatalogNo(no);
            starterCategory.setBuildPackCategoryNo(sbr.get(0).getBuildpackCategoryNo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>() {{
            put("info", starterCategory);
        }};
    }

    /**
     * 앱 템플릿명 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getStarterNamesList(StarterCategory param) {
        logger.info("getStarterNamesList :: " + param.toString());
        //JinqStream<StarterCategory> streams = jinqSource.streamAllPortal(StarterCategory.class);
        Stream<StarterCategory> streams = starterCategoryRepository.findAll().stream();

        int no = param.getNo();
        String searchKeyword = param.getSearchKeyword();

        if (null != searchKeyword && !"".equals(searchKeyword)) {
            //streams = streams.where(c -> c.getName().contains(searchKeyword) || c.getDescription().contains(searchKeyword) || c.getSummary().contains(searchKeyword));
            streams = streams.filter(c -> c.getName().contains(searchKeyword) || c.getDescription().contains(searchKeyword) || c.getSummary().contains(searchKeyword));
        }

        //streams = streams.sortedDescendingBy(c -> c.getNo());
        streams = streams.sorted(Comparator.comparing(StarterCategory::getNo).reversed());
        //List<StarterCategory> starterCategoryList = streams.toList();
        List<StarterCategory> starterCategoryList2 = streams.collect(Collectors.toList());
        return new HashMap<String, Object>() {{
            put("list", starterCategoryList2);
            //put("list", starterCategoryList);
        }};

    }

    /**
     * 앱 템플릿 목록을 조회한다.
     *
     * @param use String
     * @return Map(자바클래스)
     */
    public Map<String, Object> getStarterList(String use) {
        //JinqStream<StarterCategory> streams = jinqSource.streamAllPortal(StarterCategory.class);
        Stream<StarterCategory> streams = starterCategoryRepository.findAll().stream();
        if (null != use && !"".equals(use)) {
            //streams = streams.where(c -> c.getUseYn().equals(use));
            streams = streams.filter(c -> c.getUseYn().equals(use));
        }
        //streams = streams.sortedDescendingBy(c -> c.getNo());
        streams = streams.sorted(Comparator.comparing(StarterCategory::getNo));
        List<StarterCategory> starterCategoryList = streams.collect(Collectors.toList());
        return new HashMap<String, Object>() {{
            put("list", starterCategoryList);
        }};
    }

    /**
     * 앱 개발환경 카탈로그 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getBuildPackCatalogList(BuildpackCategory param) {
        logger.info("getBuildPackCatalogList :: " + param.toString());
        //JinqStream<BuildpackCategory> streams = jinqSource.streamAllPortal(BuildpackCategory.class);
        Stream<BuildpackCategory> streams = buildpackCategoryRepository.findAll().stream();

        int no = param.getNo();
        String searchKeyword = param.getSearchKeyword();

        if (null != searchKeyword && !"".equals(searchKeyword)) {
            streams = streams.filter(c -> c.getName().contains(searchKeyword) || c.getDescription().contains(searchKeyword) || c.getSummary().contains(searchKeyword));
        }

        if (no != 0) {
            streams = streams.filter(c -> c.getNo() == no);
        }

        //streams = streams.sortedDescendingBy(c -> c.getNo());
        streams = streams.sorted(Comparator.comparing(BuildpackCategory::getNo).reversed());
        //List<BuildpackCategory> buildpackCategoryList = streams.toList();
        List<BuildpackCategory> buildpackCategoryList = streams.collect(Collectors.toList());
        return new HashMap<String, Object>() {{
            put("list", buildpackCategoryList);
        }};
    }

    /**
     * 앱 개발환경 카탈로그 목록을 조회한다.
     *
     * @param use String
     * @return Map(자바클래스)
     */
    public Map<String, Object> getBuildPackList(String use) {
        //JinqStream<BuildpackCategory> streams = jinqSource.streamAllPortal(BuildpackCategory.class);
        Stream<BuildpackCategory> streams = buildpackCategoryRepository.findAll().stream();
        if (null != use && !"".equals(use)) {
            //streams = streams.where(c -> c.getUseYn().equals(use));
            streams = streams.filter(c -> c.getUseYn().equals(use));
        }
        //streams = streams.sortedDescendingBy(c -> c.getNo());
        streams = streams.sorted(Comparator.comparing(BuildpackCategory::getNo).reversed());
        List<BuildpackCategory> starterCategoryList = streams.collect(Collectors.toList());
        return new HashMap<String, Object>() {{
            put("list", starterCategoryList);
        }};
    }

    public Map<String, Object> getPacks(String searchKeyword) {

        //JinqStream<StarterCategory> streams = jinqSource.streamAllPortal(StarterCategory.class);
        Stream<StarterCategory> streams = starterCategoryRepository.findAll().stream();
        //JinqStream<BuildpackCategory> streams2 = jinqSource.streamAllPortal(BuildpackCategory.class);
        Stream<BuildpackCategory> streams2 = buildpackCategoryRepository.findAll().stream();
        if (null != searchKeyword && !"".equals(searchKeyword)) {
            //streams = streams.where(c -> c.getName().contains(searchKeyword) || c.getDescription().contains(searchKeyword) || c.getSummary().contains(searchKeyword));
            streams = streams.filter(c -> c.getName().contains(searchKeyword) || c.getDescription().contains(searchKeyword) || c.getSummary().contains(searchKeyword));
            //streams2 = streams2.where(c -> c.getName().contains(searchKeyword) || c.getDescription().contains(searchKeyword) || c.getSummary().contains(searchKeyword));
            streams2 = streams2.filter(c -> c.getName().contains(searchKeyword) || c.getDescription().contains(searchKeyword) || c.getSummary().contains(searchKeyword));
        }

        //streams = streams.sortedDescendingBy(c -> c.getNo());
        streams = streams.sorted(Comparator.comparing(StarterCategory::getNo).reversed());
        //streams2 = streams2.sortedDescendingBy(c -> c.getNo());
        streams2 = streams2.sorted(Comparator.comparing(BuildpackCategory::getNo).reversed());
        List<StarterCategory> starterCategoryList = streams.collect(Collectors.toList());
        List<BuildpackCategory> buildpackCategoryList = streams2.collect(Collectors.toList());
        return new HashMap<String, Object>() {{
            put("TemplateList", starterCategoryList);
            put("BuildPackList", buildpackCategoryList);
        }};
    }

    /**
     * 서비스 카탈로그 목록을 조회한다.
     *
     * @param use String
     * @return Map(자바클래스)
     */
    public Map<String, Object> getServicePackList(String use) {
        //JinqStream<ServicepackCategory> streams = jinqSource.streamAllPortal(ServicepackCategory.class);
        Stream<ServicepackCategory> streams = servicepackCategoryRepository.findAll().stream();
        logger.info(use);
        if (null != use && !"".equals(use)) {
            //streams = streams.where(c -> c.getUseYn().equals(use));
            streams = streams.filter(c -> c.getUseYn().equals(use));
        }
        //streams = streams.sortedDescendingBy(c -> c.getNo());
        streams = streams.sorted(Comparator.comparing(ServicepackCategory::getNo).reversed());
        List<ServicepackCategory> starterCategoryList = streams.collect(Collectors.toList());
        return new HashMap<String, Object>() {{
            put("list", starterCategoryList);
        }};
    }


    /**
     * 서비스 카탈로그 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getServicePackCatalogList(ServicepackCategory param) {
        logger.info("getServicePackCatalogList :: " + param.toString());
        //JinqStream<ServicepackCategory> streams = jinqSource.streamAllPortal(ServicepackCategory.class);
        Stream<ServicepackCategory> streams = servicepackCategoryRepository.findAll().stream();
        int no = param.getNo();
        String searchKeyword = param.getSearchKeyword();
        if (null != searchKeyword && !"".equals(searchKeyword)) {
            //streams = streams.where(c -> c.getName().contains(searchKeyword) || c.getDescription().contains(searchKeyword) || c.getSummary().contains(searchKeyword));
            streams = streams.filter(c -> c.getName().contains(searchKeyword) || c.getDescription().contains(searchKeyword) || c.getSummary().contains(searchKeyword));
        }

        if (no != 0) {
            //streams = streams.where(c -> c.getNo() == no);
            streams = streams.filter(c -> c.getNo() == no);
        }

        //streams = streams.sortedDescendingBy(c -> c.getNo());
        streams = streams.sorted(Comparator.comparing(ServicepackCategory::getNo).reversed());
        //List<ServicepackCategory> servicePackCatalogList = streams.toList();
        List<ServicepackCategory> servicePackCatalogList = streams.collect(Collectors.toList());

        return new HashMap<String, Object>() {{
            put("list", servicePackCatalogList);
        }};
    }

    /**
     * 앱 템플릿 카탈로그 개수를 조회한다.
     *
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public int getStarterCatalogCount(StarterCategory param) {
        logger.info("getStarterCatalogCount :: " + param.toString());
        //JinqStream<StarterCategory> streams = jinqSource.streamAllPortal(StarterCategory.class);
        Stream<StarterCategory> streams = starterCategoryRepository.findAll().stream();

        int startPackCnt = 0;
        String name = param.getName();

        if (null != name && !"".equals(name)) {
            //streams = streams.where(c -> c.getName().equals(name));
            streams = streams.filter(c -> c.getName().equals(name));
            //streams = streams.sortedDescendingBy(c -> c.getNo());
            streams = streams.sorted(Comparator.comparing(StarterCategory::getNo).reversed());
            List<StarterCategory> starterCategoryList = streams.collect(Collectors.toList());
            startPackCnt = starterCategoryList.size();
        }
        return startPackCnt;
    }

    /**
     * 앱 개발환경 카탈로그 개수를 조회한다.
     *
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public int getBuildPackCatalogCount(BuildpackCategory param) {
        logger.info("getBuildPackCatalogCount :: " + param.toString());
        //JinqStream<BuildpackCategory> streams = jinqSource.streamAllPortal(BuildpackCategory.class);
        Stream<BuildpackCategory> streams = buildpackCategoryRepository.findAll().stream();

        int buildPackCnt = 0;
        String name = param.getName();

        if (null != name && !"".equals(name)) {
            //streams = streams.where(c -> c.getName().equals(name));
            streams = streams.filter(c -> c.getName().equals(name));
            //streams = streams.sortedDescendingBy(c -> c.getNo());
            streams = streams.sorted(Comparator.comparing(BuildpackCategory::getNo).reversed());
            List<BuildpackCategory> buildpackCategoryList = streams.collect(Collectors.toList());
            buildPackCnt = buildpackCategoryList.size();
        }


        return buildPackCnt;
    }

    /**
     * 서비스 카탈로그 개수를 조회한다.
     *
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public int getServicePackCatalogCount(ServicepackCategory param) {
        logger.info("getServicePackCatalogCount :: " + param.toString());
        //JinqStream<ServicepackCategory> streams = jinqSource.streamAllPortal(ServicepackCategory.class);
        Stream<ServicepackCategory> streams = servicepackCategoryRepository.findAll().stream();

        int servicePackCnt = 0;
        String name = param.getName();

        if (null != name && !"".equals(name)) {
            //streams = streams.where(c -> c.getName().equals(name));
            streams = streams.filter(c -> c.getName().equals(name));
            //streams = streams.sortedDescendingBy(c -> c.getNo());
            streams = streams.sorted(Comparator.comparing(ServicepackCategory::getNo).reversed());
            List<ServicepackCategory> servicepackCategoryList = streams.collect(Collectors.toList());
            servicePackCnt = servicepackCategoryList.size();
        }

        return servicePackCnt;
    }

    /**
     * 앱 템플릿 카탈로그를 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> insertStarterCatalog(StarterCategory param) {
        logger.info("insertStarterCatalog :: " + param.toString());
        starterCategoryRepository.save(param);
        logger.info("insertStarterCatalog :: NO2 " + param.getNo());
        try {
            //스타터서비스 릴레이션 저장
            for (int servicepackCategoryNo : param.getServicePackCategoryNoList()) {
                StarterServicepackRelation ssr = new StarterServicepackRelation();
                ssr.setStarterCatalogNo(param.getNo());
                ssr.setServicepackCategoryNo(servicepackCategoryNo);
                starterServicePackRelationRepository.save(ssr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //스타터 빌드팩 릴레이션 저장
            StarterBuildpackRelation sbr = new StarterBuildpackRelation();
            sbr.setBuildpackCategoryNo(param.getBuildPackCategoryNo());
            sbr.setStarterCatalogNo(param.getNo());

            starterBuildPackRelationRepository.save(sbr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 앱 개발환경 카탈로그를 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> insertBuildPackCatalog(BuildpackCategory param) {
        logger.info("insertBuildPackCatalog :: " + param.toString());
        buildpackCategoryRepository.save(param);
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 서비스 카탈로그를 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> insertServicePackCatalog(ServicepackCategory param) {
        logger.info("insertServicePackCatalog :: " + param.toString());
        param.setParameter(jsonCheck(param.getParameter()));
        param.setAppBindParameter(jsonCheck(param.getAppBindParameter()));
        servicepackCategoryRepository.save(param);
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    private String jsonCheck(String str) {
        String original = str;
        str = str.replace("{", "").replace("}", "");
        String[] params1 = str.split(",");

        if (params1.length == 0) {
            return "";
        } else {
            if (str.indexOf(":") <= 0) {
                return "";
            } else {
                return original;
            }
        }
    }

    /**
     * 앱 개발환경 카탈로그를 수정한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> updateStarterCatalog(StarterCategory param) {
        logger.info("updateStarterCatalog :: " + param.toString());
        //Upate Starter Catalog
        starterCategoryRepository.save(param);

        try {
            //기존 스타터서비스 릴레이션 삭제
            logger.info("updateStarterCatalog before start service relation remove");
            List<StarterServicepackRelation> ssrList = starterServicePackRelationRepository.findByStarterCatalogNo(param.getNo());
            for (StarterServicepackRelation ssr : ssrList) {
                logger.info("ssrList " + ssr.toString());
                starterServicePackRelationRepository.deleteById(ssr.getNo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            logger.info("updateStarterCatalog before start build relation remove");
            //기존 스타터서비스 릴레이션 삭제
            List<StarterBuildpackRelation> sbrList = starterBuildPackRelationRepository.findByStarterCatalogNo(param.getNo());
            for (StarterBuildpackRelation sbr : sbrList) {
                logger.info("sbrList " + sbr.toString());
                starterBuildPackRelationRepository.deleteById(sbr.getNo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            logger.info("updateStarterCatalog start service relation insert");
            //스타터서비스 릴레이션 저장

            for (int servicepackCategoryNo : param.getServicePackCategoryNoList()) {
                StarterServicepackRelation ssrInsert = new StarterServicepackRelation();
                ssrInsert.setStarterCatalogNo(param.getNo());
                ssrInsert.setServicepackCategoryNo(servicepackCategoryNo);
                logger.info(ssrInsert.toString());
                starterServicePackRelationRepository.save(ssrInsert);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            logger.info("updateStarterCatalog start build relation insert");
            //스타터 빌드팩 릴레이션 저장
            StarterBuildpackRelation sbrInsert = new StarterBuildpackRelation();
            sbrInsert.setBuildpackCategoryNo(param.getBuildPackCategoryNo());
            sbrInsert.setStarterCatalogNo(param.getNo());

            starterBuildPackRelationRepository.save(sbrInsert);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 앱 개발환경 카탈로그를 수정한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> updateBuildPackCatalog(BuildpackCategory param) {
        logger.info("updateBuildPackCatalog :: " + param.toString());

        BuildpackCategory update = buildpackCategoryRepository.findByNo(param.getNo());
        param.setCreated(update.getCreated());
        param.setLastmodified(new Date());
        BuildpackCategory buildpackCategory = buildpackCategoryRepository.save(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 서비스 카탈로그를 수정한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> updateServicePackCatalog(ServicepackCategory param) {
        logger.info("updateServicePackCatalog :: " + param.toString());
        int no = param.getNo();
        ServicepackCategory update = servicepackCategoryRepository.findByNo(no);
        param.setCreated(update.getCreated());
        param.setLastmodified(new Date());
        param.setParameter(jsonCheck(param.getParameter()));
        logger.info("updateServicePackCatalog2 :: " + param.toString());

        ServicepackCategory servicepackCategory = servicepackCategoryRepository.save(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 앱 템플릿 카탈로그를 삭제한다.
     *
     * @param no
     * @return Map(자바클래스)
     */
    public Map<String, Object> deleteStarterCatalog(int no) {
        logger.info("deleteStarterCatalog :: " + no);

        try {
            //기존 스타터서비스 릴레이션 삭제
            logger.info("updateStarterCatalog before start service relation remove");
            List<StarterServicepackRelation> ssrList = starterServicePackRelationRepository.findByStarterCatalogNo(no);
            for (StarterServicepackRelation ssr : ssrList) {
                logger.info("ssrList " + ssr.toString());
                starterServicePackRelationRepository.deleteById(ssr.getNo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            logger.info("updateStarterCatalog before start build relation remove");
            //기존 스타터서비스 릴레이션 삭제
            List<StarterBuildpackRelation> sbrList = starterBuildPackRelationRepository.findByStarterCatalogNo(no);
            for (StarterBuildpackRelation sbr : sbrList) {
                logger.info("sbrList " + sbr.toString());
                starterBuildPackRelationRepository.deleteById(sbr.getNo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        starterCategoryRepository.deleteById(no);
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 앱 개발환경 카탈로그를 삭제한다.
     *
     * @param no
     * @return Map(자바클래스)
     */
    public Map<String, Object> deleteBuildPackCatalog(int no) {
        logger.info("deleteBuildPackCatalog :: " + no);
        buildpackCategoryRepository.deleteById(no);
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 서비스 카탈로그를 삭제한다.
     *
     * @param no
     * @return Map(자바클래스)
     */
    public Map<String, Object> deleteServicePackCatalog(int no) {
        logger.info("deleteServicePackCatalog :: " + no);
        servicepackCategoryRepository.deleteById(no);
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 최신항목을 가져온다.
     */
    public Map<String, Object> getHistory(String userid) {
        List<CatalogHistory> catalogHistories = catalogHistoryRepository.findAllByUserIdOrderByLastmodifiedDesc(userid);
        List<Object> resultHistory = new ArrayList<>();

        try {
            final int starterpacknum = catalogHistories.stream().filter(a -> a.getCatalogType().equals("starter")).findFirst().get().getCatalogNo();
            final StarterCategory starterCategory = starterCategoryRepository.findByNo(starterpacknum);
            if (starterCategory != null) {
                resultHistory.add(starterCategory);
            }
        } catch (Exception e) {
        }

        try {
            final int buildpacknum = catalogHistories.stream().filter(a -> a.getCatalogType().equals("buildPack")).findFirst().get().getCatalogNo();
            final BuildpackCategory buildpackCategory = buildpackCategoryRepository.findByNo(buildpacknum);
            if (buildpackCategory != null) {
                resultHistory.add(buildpackCategory);
            }
        } catch (Exception e) {
        }

        try {
            final int servicepacknum = catalogHistories.stream().filter(a -> a.getCatalogType().equals("servicePack")).findFirst().get().getCatalogNo();
            final ServicepackCategory servicepackCategory = servicepackCategoryRepository.findByNo(servicepacknum);
            if (servicepackCategory != null) {
                resultHistory.add(servicepackCategory);
            }
        } catch (Exception e) {

        }
        return new HashMap<String, Object>() {{
            put("list", resultHistory);
        }};
    }


    /**
     * 릴레이션에 속한 목록을 가져온다.
     */
    public Map<String, Object> getStarterRelation(int no) {
        StarterCategory starterCategory = starterCategoryRepository.findByNo(no);
        List<ServicepackCategory> servicepackCategories = new ArrayList<>();
        //JinqStream<StarterServicepackRelation> streams = jinqSource.streamAllPortal(StarterServicepackRelation.class);
        Stream<StarterServicepackRelation> streams = starterServicePackRelationRepository.findAll().stream();
        //streams = streams.where(c -> c.getStarterCatalogNo() == no);
        streams = streams.filter(c -> c.getStarterCatalogNo() == no);
        //List<Integer> integerlist = streams.select(c -> c.getServicepackCategoryNo()).distinct().toList();
        List<Integer> integerlist = streams.map(StarterServicepackRelation::getServicepackCategoryNo).collect(Collectors.toList());
        for (Integer number : integerlist) {
            ServicepackCategory servicepackCategory = servicepackCategoryRepository.findByNo(number);
            if (servicepackCategory != null) {
                servicepackCategories.add(servicepackCategory);
            }
        }
        StarterBuildpackRelation starterBuildpackRelation = starterBuildPackRelationRepository.findFirstByStarterCatalogNo(no);
        BuildpackCategory buildpackCategory = buildpackCategoryRepository.findByNo(starterBuildpackRelation.getBuildpackCategoryNo());
        return new HashMap<String, Object>() {{
            put("Starter", starterCategory);
            put("Servicepack", servicepackCategories);
            put("Buildpack", buildpackCategory);
        }};
    }

    public Map<String, Object> insertHistroy(CatalogHistory catalog) {
        catalogHistoryRepository.save(catalog);
        return new HashMap<String, Object>() {{
            put("Result", catalog);
        }};
    }

    public List<CatalogCc> getListRoutes() {
        return catalogCcRepository.findAll();
    }


    /**
     * 앱 생성시 라우트 중복을 체크합니다.
     */
    public Map<String, Object> checkRoute(String host) {
        CatalogCc cc = catalogCcRepository.findByHost(host);
        if (cc != null) {
            return new HashMap<String, Object>() {{
                put("RESULT", Constants.RESULT_STATUS_FAIL);
            }};
        }
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }
}
