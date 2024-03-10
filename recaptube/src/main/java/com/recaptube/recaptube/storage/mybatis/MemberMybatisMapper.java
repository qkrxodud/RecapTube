package com.recaptube.recaptube.storage.mybatis;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@MybatisMapperScan
public interface MemberMybatisMapper {
    MemberMybatisDto selectMemberByLdapId(@Param("ldapId") String ldapId);
}
