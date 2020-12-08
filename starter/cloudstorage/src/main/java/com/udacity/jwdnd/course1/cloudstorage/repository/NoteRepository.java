package com.udacity.jwdnd.course1.cloudstorage.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.udacity.jwdnd.course1.cloudstorage.entities.Note;

@Mapper
@Repository
public interface NoteRepository {
    @Select("SELECT * FROM NOTES")
    List<Note> findAll();

    @Select("SELECT * FROM NOTES WHERE userid = #{userid}")
    List<Note> findAllByUserId(int userid);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteid}")
    Note findBy(int noteid);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid}")
    int update(Note note);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{notetitle}, #{notedescription}, #{userid})")
    int save(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid} AND userid = ${userid}")
    int delete(Integer noteid,Integer userid);

}