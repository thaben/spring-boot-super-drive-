package com.udacity.jwdnd.course1.cloudstorage.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.udacity.jwdnd.course1.cloudstorage.entities.Files;

@Mapper
@Repository
public interface FilesRepository {

    @Select("SELECT * FROM FILES")
    List<Files> findAll();

    @Select("SELECT * FROM FILES WHERE fileid = #{fileid}")
    Files findBy(int fileid);

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<Files> findByUserId(Integer userid);

    @Select("SELECT * FROM FILES WHERE filename = #{filename} AND userid = #{userId}")
    List<Files>  findByFileNameAndUserId(String filename,Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, filedata, userid) VALUES (#{filename}, #{contenttype}, #{filesize}, #{filedata}, #{userid})")
    @Options(useGeneratedKeys=true, keyProperty="fileid", keyColumn="fileid")
    int save(Files file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileid}")
    int delete(int fileid);
}