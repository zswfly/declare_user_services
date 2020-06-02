package com.zsw.services;

import com.zsw.daos.CompanyMapper;
import com.zsw.daos.UserMapper;
import com.zsw.entitys.CompanyEntity;
import com.zsw.entitys.DepartmentUserEntity;
import com.zsw.entitys.UserEntity;
import com.zsw.entitys.user.LoginTemp;
import com.zsw.entitys.user.UserDto;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.UserServiceStaticWord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by zhangshaowei on 2020/4/16.
 */
@Service
public class UserServiceImpl implements IUserService,Serializable{

    private static final long serialVersionUID = -4721228136111002555L;

    @Autowired
    private IDBService dbService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CompanyMapper companyMapper;


    @Autowired
    private IDepartmentService departmentService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void updateRememberToken(Integer userId, String rememberToken) throws Exception {
        UserEntity userEntity = this.dbService.get(UserEntity.class,userId);
        userEntity.setRememberToken(rememberToken);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public UserEntity getUser(UserEntity param) throws Exception{
        return this.dbService.get(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public UserEntity resetPassWord(UserEntity param,String resetPassWord) throws Exception {
        UserEntity entity = this.dbService.get(param);
        if (entity != null) {
            entity.setLoginPwd(resetPassWord);
            entity.setUpdateUser(entity.getId());
            entity.setUpdateTime(new Timestamp(new Date().getTime()));
        }
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void newUser(UserDto userDto, Integer currentUserId,Integer departmentId)throws Exception {

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto,userEntity);
        userEntity.setId(null);
        userEntity.setStatus(CommonStaticWord.Normal_Status_0);
        userEntity.setCreateUser(currentUserId);
        userEntity.setCreateTime(new Timestamp(new Date().getTime()));
        userEntity.setUpdateUser(currentUserId);
        userEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(userEntity);

        //TODO test
        userEntity = this.dbService.get(userEntity) ;

        DepartmentUserEntity departmentUserEntity = new DepartmentUserEntity();
        departmentUserEntity.setDepartmentId(departmentId);
        departmentUserEntity.setUserId(userEntity.getId());
        departmentUserEntity.setCreateTime(new Timestamp(new Date().getTime()));
        departmentUserEntity.setCreateTime(new Timestamp(new Date().getTime()));
        departmentUserEntity.setUpdateUser(currentUserId);
        departmentUserEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.departmentService.saveDepartmentUserEntity(departmentUserEntity);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public UserEntity updateUser(UserDto userDto, Integer currentUserId) throws Exception{
        userDto.setLoginPwd(null);
        if(userDto == null
                || userDto.getId() == null
                ){
            throw new Exception("参数错误");
        }
        UserEntity userEntity = this.dbService.get(UserEntity.class,userDto.getId());

        if(userEntity == null) throw new Exception("没有该用户id");

        if(userDto.getStatus() == CommonStaticWord.Ban_Status_1){
            List<Integer> userIds = new ArrayList<>();
            userIds.add(userDto.getId());
            Map<String, Object > param = new HashMap<>();
            param.put("userIds",userIds);
            List<Integer> managerIds = this.companyMapper.checkCompanyManagerIds(param);
            if((managerIds!=null && managerIds.contains(userDto.getId()))
                    || userDto.getId() == currentUserId
                    )userDto.setStatus(userEntity.getStatus());
            //不能禁用manager用户 , 当前用户自己
        }

        //TODO test
        userDto.setLoginPwd(userEntity.getLoginPwd());

        BeanUtils.copyProperties(userDto,userEntity);

        userEntity.setUpdateUser(currentUserId);
        userEntity.setUpdateTime(new Timestamp(new Date().getTime()));

        return userEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void batchBan(List<Integer> ids, String type, Integer currentUserId,Integer currentCompanyId) throws Exception{
        int status =CommonStaticWord.Normal_Status_0;
        List<Integer> managerIds = null;
        if( CommonStaticWord.Status_ban.equals(type)){
            status = CommonStaticWord.Ban_Status_1;
            Map<String, Object > param = new HashMap<>();
            param.put("userIds",ids);
            managerIds = this.companyMapper.checkCompanyManagerIds(param);

        }
        if(managerIds != null && managerIds.size() >0){
            Iterator<Integer> it = ids.iterator();
            while(it.hasNext()) {
                Integer value = it.next();
                if (managerIds.contains(value)) {
                    it.remove();
                }
            }
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("currentUserId",currentUserId);
        paramMap.put("status",status);
        if(currentCompanyId != null && currentCompanyId > 0)
            paramMap.put("companyId",currentCompanyId);
        paramMap.put("ids",ids);

        this.userMapper.batchBan(paramMap);


//        List<UserEntity> list = this.dbService.findBy(UserEntity.class,"id",ids);
//
//        for (UserEntity item : list){
//            if(item.getId() != currentUserId
//                    && (managerIds != null && !managerIds.contains(item.getId()))){
//                item.setStatus(status);
//                item.setUpdateUser(currentUserId);
//                item.setUpdateTime(new Timestamp(new Date().getTime()));
//            }
//        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<UserEntity> getUsersByIds(List<Integer> ids) throws Exception{
        return this.dbService.findBy(UserEntity.class,"id",ids);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<UserDto> usersPage(Map<String, Object> paramMap) throws Exception{
        return this.userMapper.usersPage(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer usersPageCount(Map<String, Object> paramMap) throws Exception {
        return this.userMapper.usersPageCount(paramMap);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public synchronized String checkUserExist(UserDto userDto) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        if(userDto.getId() == null){
            if (StringUtils.isBlank(userDto.getLoginPwd())
                    ||userDto.getLoginPwd().length() < 8
                    ) stringBuilder.append("密码少于8位") ;

            UserEntity userEntity = new UserEntity();

            userEntity.setPhone(userDto.getPhone());
            if( this.dbService.get(userEntity) != null
                    ) stringBuilder.append("电话号码已存在");

            userEntity.setPhone(null);
            userEntity.setEmail(userDto.getEmail());
            if( this.dbService.get(userEntity) != null
                    )  stringBuilder.append("Email地址已被使用");
        }else{
            UserEntity param = new UserEntity();
            List<UserEntity> resultList = null;
            param.setPhone(userDto.getPhone());
            resultList = this.dbService.find(param);
            for(UserEntity result :resultList){
                result = this.dbService.get(param);
                if( result != null && result.getId() != userDto.getId() ) {
                    stringBuilder.append("电话号码已存在");
                    break;
                }
            }

            param.setPhone(null);
            param.setEmail(userDto.getEmail());
            resultList = this.dbService.find(param);
            for(UserEntity result :resultList){
                result = this.dbService.get(param);
                if( result != null && result.getId() != userDto.getId() ) {
                    stringBuilder.append("Email地址已被使用");
                    break;
                }
            }
        }

        return stringBuilder.toString();
    }

}
//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
//    public synchronized String checkUserExist(UserDto userDto,Integer currentCompanyId, Integer departmentId) throws Exception {
//        Map<String, Object> paramMap = new HashMap<>();
//
//
//        if(userDto.getId() != null && userDto.getId() >0 ){
//            Map<String, Object> paramMapTemp = new HashMap<>();
//            paramMapTemp.put("companyId",currentCompanyId);
//            paramMapTemp.put("userId",userDto.getId());
//            Integer resultTemp = this.userMapper.usersPageCount(paramMapTemp);
//            if(resultTemp == null || resultTemp < 1){
//                return "更新错误,当前公司下没该用户";
//            }
//            paramMap.put("notUserId",userDto.getId());
//        }
//
//        if(departmentId != null && departmentId > 0)
//            paramMap.put("departmentId",departmentId);
//        if(currentCompanyId != null && currentCompanyId > 0)
//            paramMap.put("companyId",currentCompanyId);
//
//        paramMap.put("userName",userDto.getUserName());
//        paramMap.put("userPhone",userDto.getPhone());
//        paramMap.put("userEmail",userDto.getEmail());
//
//
//        Map<String,String> result = this.userMapper.checkUserExist(paramMap);
//        String userName = result.get("user_name");
//        String userPhone = result.get("user_name");
//        String userEmail = result.get("user_email");
//        StringBuilder stringBuilder = new StringBuilder();
//        if(result != null &&
//                (
//                    (StringUtils.isNotBlank(userName)&&StringUtils.isNotEmpty(userName))
//                    ||(StringUtils.isNotBlank(userPhone)&&StringUtils.isNotEmpty(userPhone))
//                    ||(StringUtils.isNotBlank(userEmail)&&StringUtils.isNotEmpty(userEmail))
//                )
//        ){
//
//            List<String> userNames = Arrays.asList(userName.split(","));
//            List<String> phones = Arrays.asList(userPhone.split(","));
//            List<String>  emails = Arrays.asList(userEmail.split(","));
//            if(userNames.contains(userDto.getUserName())){
//                stringBuilder.append("当前用户名已存在;");
//            }
//            if(phones.contains(userDto.getPhone())){
//                stringBuilder.append("当前手机号码已存在;");
//            }
//            if(emails.contains(userDto.getEmail())){
//                stringBuilder.append("当前邮箱已存在;");
//            }
//        }
//
//        return stringBuilder.toString();
//    }