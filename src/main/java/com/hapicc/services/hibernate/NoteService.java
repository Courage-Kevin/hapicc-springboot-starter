package com.hapicc.services.hibernate;

import com.hapicc.pojo.JqGridResult;
import com.hapicc.pojo.hibernate.Note;

import java.util.Map;

public interface NoteService {

    Note save(Note note) throws Exception;

    Note update(Long id, Note note);

    void delete(Long id);

    Note get(Long id);

    JqGridResult list(Map<String, String> params) throws Exception;

    Note getNoteSimpleInfo(Long id);
}
