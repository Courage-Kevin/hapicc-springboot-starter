package com.hapicc.repository;

import com.hapicc.pojo.hibernate.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, Long>, JpaSpecificationExecutor<Note> {

    // 执行自定义 SQL，HQL
    @Query("select new Note(id, title, createdAt) from Note where id = :id")
    Note findNoteSimpleInfo(@Param("id") Long id);
}
