package com.udacity.jwdnd.course1.cloudstorage.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;

@Mapper
@Repository
public interface CredentialsRepository {

    @Select("SELECT * FROM CREDENTIALS")
    List<Credential> findAll();

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    List<Credential> findByUserId(Integer userid);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    Credential findBy(Integer credentialid);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys=true, keyProperty="credentialid", keyColumn="credentialid")
    int save(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid} AND userid = #{userId}")
    int deleteByCredentialIdAndUserId(Integer credentialid,Integer userId);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    int delete(Integer credentialid);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password = #{password} WHERE credentialid = #{credentialid}")
    int update(Credential credential);
}