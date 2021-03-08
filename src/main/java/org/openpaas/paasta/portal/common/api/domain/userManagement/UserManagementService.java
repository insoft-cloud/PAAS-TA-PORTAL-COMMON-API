package org.openpaas.paasta.portal.common.api.domain.userManagement;


import org.jinq.orm.stream.JinqStream;
import org.openpaas.paasta.portal.common.api.config.Constants;
import org.openpaas.paasta.portal.common.api.config.JinqSource;
import org.openpaas.paasta.portal.common.api.config.dataSource.PortalConfig;
import org.openpaas.paasta.portal.common.api.domain.common.CommonService;
import org.openpaas.paasta.portal.common.api.entity.portal.UserDetail;
import org.openpaas.paasta.portal.common.api.entity.uaa.Users;
import org.openpaas.paasta.portal.common.api.repository.cc.UsersCcRepository;
import org.openpaas.paasta.portal.common.api.repository.portal.UserDetailRepository;
import org.openpaas.paasta.portal.common.api.repository.uaa.UsersRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by SEJI on 2018-03-08.
 */
@Transactional
@Service
public class UserManagementService {
    private final Logger logger = getLogger(this.getClass());

//    private final UserService userService;

    @Autowired
    PortalConfig portalConfig;

    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    UsersCcRepository usersCcRepository;

//    @Autowired
//    public UserManagementService(UserService userService) {
//        this.userService = userService;
//    }

    @Autowired
    JinqSource jinqSource;

    @Autowired
    CommonService commonService;

    /**
     * 사용자 정보 목록을 조회한다.
     *
     * @return Map(자바클래스)
     */
    public Map<String, Object> getUserInfoList(String filter, UserDetail detail) {

//        JinqStream<Users> userStream = jinqSource.streamAllUAA(Users.class);
    	Stream<Users> userStream = usersRepository.findAll().stream();
//        JinqStream<UserDetail> streams = jinqSource.streamAllPortal(UserDetail.class);
    	Stream<UserDetail> streams = userDetailRepository.findAll().stream();
        if(filter.equals("All"))
//        	streams = streams.sortedDescendingBy(c -> c.getUserName());
        	streams = streams.sorted(Comparator.comparing(UserDetail::getUserName).reversed());
        

//        List<UserDetail> userDetailList = streams.toList();
        List<UserDetail> userDetailList = streams.collect(Collectors.toList());;
//        userStream = userStream.sortedBy(user -> user.getUserName());
        userStream = userStream.sorted(Comparator.comparing(Users::getUserName));
//        userStream = userStream.sortedBy(user -> user.getActive());
        userStream = userStream.sorted(Comparator.comparing(Users::getActive));
        List<UserDetail> userDetailLists = new ArrayList<UserDetail>();
        String searchKeyword = detail.getSearchKeyword();
        userStream.forEach(user -> {
            try {
                if(!(filter.equals("All") || user.getActive().equals(filter))){
                    return;
                }
                Optional<UserDetail> result = userDetailList.stream().filter(user_detail -> user_detail.getUserId().equals(user.getUserName())).findFirst();

                if(result.equals(Optional.empty())){
                    UserDetail newUser = new UserDetail();
                    newUser.setUserId(user.getEmail());
                    newUser.setActive(user.getActive().equals("t") ? "Y" : "N");
                    newUser.setUserName(user.getUserName());
                    newUser.setStatus("1");
                    newUser.setTellPhone("-");
                    newUser.setAdminYn("N");
                    newUser.setUserGuid(user.getId());
                    if (null != detail.getSearchKeyword() && !"".equals(detail.getSearchKeyword())) {
                        if(newUser.getUserName().contains(searchKeyword)||newUser.getUserId().contains(searchKeyword)){
                            userDetailLists.add(newUser);
                        }
                    }else {
                        userDetailLists.add(newUser);
                    }

                }else {
                    result.get().setUserGuid(user.getId());
                    if(result.get().getUserName() == null){
                        result.get().setUserName(user.getUserName());
                    }
                    if (null != detail.getSearchKeyword() && !"".equals(detail.getSearchKeyword())) {
                        if(result.get().getUserName().contains(searchKeyword)||result.get().getUserId().contains(searchKeyword)){
                            userDetailLists.add(result.get());
                        }
                    }else {
                        userDetailLists.add(result.get());
                    }

                }
            }catch (Exception e){

            }
        });
        if (null != detail.getSearchKeyword() && !"".equals(detail.getSearchKeyword())) {
            String keyword = detail.getSearchKeyword();
            List<UserDetail> filterLists = userDetailLists.stream().filter(d -> d.getUserId().contains(keyword) || d.getUserName().contains(keyword)).collect(Collectors.toList());
            return new HashMap<String, Object>() {{
                put("list", filterLists);
            }};
        }

        return new HashMap<String, Object>() {{
            put("list", userDetailLists);
        }};

    }

    public Map<String, Object> getUserInfo(String userid) {
//        JinqStream<UserDetail> streams = jinqSource.streamAllPortal(UserDetail.class);
        Stream<UserDetail> streams = userDetailRepository.findAll().stream();
        if (null != userid && !"".equals(userid)) {
//            streams = streams.where(d -> d.getUserId().equals(userid));
            streams = streams.filter(user -> userid.equals(user.getUserId()));
        }
//        List<UserDetail> userDetailList = streams.cotoList();
        List<UserDetail> userDetailList = streams.collect(Collectors.toList());

        return new HashMap<String, Object>() {{
            put("list", setUserGuid(userDetailList));
        }};
    }

    private List<UserDetail> setUserGuid(List<UserDetail> details) {
        List<Users> users = usersRepository.findAll();
        for (UserDetail userDetail : details) {
            for (Users user : users) {
                if (userDetail.getUserId().equals(user.getUserName())) {
                    userDetail.setUserGuid(user.getId());
                }
            }

        }
        return details;
    }

    /**
     * 사용자에게 운영자 권한을 부여한다.
     */
    public Map<String, Object> updateOperatingAuthority(String userId) {
        UserDetail userDetail = userDetailRepository.findByUserId(userId);
        userDetail.setAdminYn(!userDetail.getAdminYn().equals("Y") ? "Y" : "N");
        userDetailRepository.save(userDetail);
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 사용자를 삭제한다.
     */
    public Map<String, Object> deleteUserAccount(String userId) {
        userDetailRepository.deleteByUserId(userId);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 사용자 포탈 접속 성공 유무 수정
     */
    public Map<String, Object> UpdateUserActive(String userId, UserDetail _userDetail) {
        UserDetail userDetail = userDetailRepository.findByUserId(userId);
        if(userDetail == null || userDetail.getUserId().equals("admin")){
            return new HashMap<String, Object>() {{
                put("RESULT", Constants.RESULT_STATUS_FAIL);
            }};
        }
        userDetail.setActive(_userDetail.getActive());
        userDetail = userDetailRepository.save(userDetail);
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }



}

