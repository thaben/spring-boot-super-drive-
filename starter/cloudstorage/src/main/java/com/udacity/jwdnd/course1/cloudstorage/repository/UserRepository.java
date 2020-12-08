package com.udacity.jwdnd.course1.cloudstorage.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.udacity.jwdnd.course1.cloudstorage.entities.MyUser;

@Mapper
@Repository
public interface UserRepository {

    @Select("SELECT * FROM USERS")
    List<MyUser> findAll();

    @Select("SELECT * FROM USERS WHERE userid = #{userid}")
    MyUser findBy(Integer userid);

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    MyUser findByUsername(String username);

    @Insert("INSERT INTO USERS (username, password, salt, firstname, lastname) VALUES (#{username}, #{password}, #{salt}, #{firstname}, #{lastname})")
    @Options(useGeneratedKeys=true, keyProperty="userid", keyColumn="userid")
    Integer save(MyUser user);

    @Update("UPDATE USERS SET username = #{username}, password = #{password}, salt = #{salt}, firstname = #{firstname}, lastname = #{lastname} WHERE userid = #{userid}")
    Boolean update(MyUser user);

    @Delete("DELETE FROM USERS WHERE username = #{username}")
    Boolean delete(String username);


}